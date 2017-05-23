package ru.bov.genesis.web.building;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.amcharts.model.Color;
import com.haulmont.charts.gui.components.charts.Chart;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.data.ListDataProvider;
import com.haulmont.charts.gui.data.MapDataItem;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.listeners.click.MapClickListener;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.bov.genesis.Func;
import ru.bov.genesis.entity.mainentity.Building;
import ru.bov.genesis.entity.mainentity.Employee;

import javax.inject.Inject;
import java.util.*;

import org.joda.time.*;

import static ru.bov.genesis.Func.*;

public class BuildingEdit extends AbstractEditor<Building> {

    @Inject
    private MapViewer googleMapView;

    @Inject
    private FieldGroup fieldGroup;

    @Inject
    private Datasource<Building> buildingDs;

    @Inject
    private Datasource<Employee> employeeCkDs;

    @Inject
    private LookupField typeMap;

    @Inject
    private Frame windowActions;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private Table<Employee> groupCkTable;

    @Inject
    private DataGrid<Employee> dataGridEmployeeCK;

    @Inject
    private Chart ganttChart;

    @Inject
    private DateField fieldDateWorkStart;

    @Inject
    private TabSheet tabSheetBuilding;
    @Inject
    private TabSheet.Tab ganttCK;

    private ListDataProvider fillGantt(List<Employee> employees) {
        ListDataProvider dataProvider = new ListDataProvider();
        if (!employees.isEmpty()) {
            for (Employee employee : employees) {
                if (employee.getDateWorkStart() != null)
                if (employee.getDateWorkStart() != null)
                    dataProvider.addItem(new MapDataItem(ParamsMap.of("category",
                            shortFIO(employee.getLastName(), employee.getFirstName(), employee.getMiddleName()) + " - " +
                                      employee.getDirection_work().getNameDirecting(),
                            "segments", calcSegments(employee.getDateWorkStart(), employee.getDateWorkEnd(),
                                    2, 1))));

            }
        }
        return dataProvider;
    }

    private List<Employee> sortEmploye(List<Employee> list) {
        Collections.sort(list, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getDirection_work().getNameDirecting()
                        .compareTo(o2.getDirection_work().getNameDirecting());
            }
        });
        return list;
    }

    // функция заполнения графика данными
    private void dynamicFillGantt(List<Employee> list) {
        if (list  != null) {
            list = sortEmploye(list);
            if (!list.isEmpty()) {
                ganttChart.setVisible(true);
                ganttChart.setDataProvider(fillGantt(list));
                ganttChart.setHeight(String.valueOf((list.size() * 30) + 80) + "px");
            }
        }
    }

    @Override
    public void init(Map<String, Object> params) {


        // обработка по окончании редактирования строки в таблице сотрудников СК
        dataGridEmployeeCK.addEditorCloseListener(event -> {
            employeeCkDs.getItem().setFieldStatus(statusStr(employeeCkDs.getItem(),
                    employeeCkDs.getItem().getDateWorkStart(),
                    employeeCkDs.getItem().getDateWorkEnd()));
            ganttChart.setDataProvider(fillGantt(buildingDs.getItem().getEmployeeCk()));
        });

        // заполним первоначальные значения графика
        ganttChart.setVisible(false);
        //List<Employee> list = ((Building) params.get("ITEM")).getEmployeeCk();
        dynamicFillGantt(((Building) params.get("ITEM")).getEmployeeCk());
        // динамически заполняем график при открытии закладки
        tabSheetBuilding.addSelectedTabChangeListener(event -> {
                if (event.getSelectedTab().getName().equals("ganntCK")) {
                    dynamicFillGantt(buildingDs.getItem().getEmployeeCk());
                }
        });

        // Настройка карт
        TextField place = (TextField) fieldGroup.getField("place").getComponent();

        place.addTextChangeListener(event -> {
            Double[] coord = getCoordinates(event.getText());
            googleMapView.setCenter(googleMapView.createGeoPoint(coord[0], coord[1]));
        });

        googleMapView.addMapClickListener(event -> {
            Double latitude = event.getPosition().getLatitude();
            Double longtitude = event.getPosition().getLongitude();
            String pl1 = String.format("%.6f", latitude).replace(",", ".");
            String pl2 = String.format("%.6f", longtitude).replace(",", ".");
            place.setValue(pl1 + ", " + pl2);
        });

        typeMap.addValueChangeListener(e -> {googleMapView.setMapType(e.getValue().toString());});

        Double[] coord = new Double[2];
        coord = getCoordinates(((Building) params.get("ITEM")).getPlace());
        Integer scale = ((Building) params.get("ITEM")).getPlaceScale();
        GeoPoint center = googleMapView.createGeoPoint(coord[0], coord[1]);
        if (scale == null) scale = 15;
        googleMapView.setCenter(center);
        googleMapView.setZoom(scale);

        // Настройка таблицы сотрудников группы СК
        dataGridEmployeeCK.setEditorEnabled(true);

        // Цвет статуса сотрудника
        dataGridEmployeeCK.addCellStyleProvider((entity, property) -> {
            if ("fieldStatus".equals(property)) {
                if (entity != null) {
                    if (entity.getBuilding() != null) {
                        return Func.returnStatusColor(true, property, entity.getDateWorkStart(), entity.getDateWorkEnd());
                    } else {
                        return Func.returnStatusColor(false, property, entity.getDateWorkStart(), entity.getDateWorkEnd());
                    }
                } else {
                    return "default-status";
                }
            }
            return null;
        });

    }

    @Override
    protected boolean preCommit() {
        buildingDs.getItem().setPlaceScale(googleMapView.getZoom());
        return super.preCommit();
    }

    public Double[] getCoordinates(String str) {
        Double[] coord = new Double[2];
        coord[0] = 57.158550;
        coord[1] = 65.533928;
        if (str == null) return coord;
        String[] geoText = str.split(",");
        if (geoText.length == 2) {
            coord[0] = Double.parseDouble(geoText[0]);
            coord[1] = Double.parseDouble(geoText[1]);
        } else {
            coord[0] = 57.158550;
            coord[1] = 65.533928;
        }
        return coord;
    }

}