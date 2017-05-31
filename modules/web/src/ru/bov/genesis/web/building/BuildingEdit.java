package ru.bov.genesis.web.building;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.components.charts.Chart;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.data.ListDataProvider;
import com.haulmont.charts.gui.data.MapDataItem;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.joda.time.Period;
import ru.bov.genesis.entity.mainentity.Building;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.services.CellsCk;

import javax.inject.Inject;
import java.util.*;

import static ru.bov.genesis.ToolsFunc.*;
import static ru.bov.genesis.utils.GlobalTools.*;

public class BuildingEdit extends AbstractEditor<Building> {

    @Inject
    private MapViewer googleMapView;

    @Inject
    private FieldGroup fieldGroup1;
    @Inject
    private FieldGroup fieldGroup2;

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
    private DataGrid<CellsCk> cellsCkDataGrid;

    @Inject
    private Chart ganttChart;

    @Inject
    private DateField fieldDateWorkStart;

    @Inject
    private TabSheet tabSheetBuilding;
    @Inject
    private TabSheet.Tab ganttCK;
    @Inject
    private TextField place;
    @Inject
    private CollectionDatasource<KeyValueEntity, Object> countDirectionDs;
    @Inject
    private Label labelCount;

    // функция заполнения графика данными
    private void dynamicFillGantt(List<Employee> list, Period periodWork, Period periodPause) {
        if (list  != null) {
            list = sortEmploye(list);
            if (!list.isEmpty()) {
                ganttChart.setVisible(true);
                ganttChart.setDataProvider(fillGantt(list, periodWork, periodPause));
            }
        }
    }


    public void refreshCountEmployee(UUID item) {
        String valueCount = "";
        countDirectionDs.refresh(ParamsMap.of("id", item));

        Collection<KeyValueEntity> valueEntities = countDirectionDs.getItems();
        for (KeyValueEntity valueEntity : valueEntities) {
            String statusField = valueEntity.getValue("directionName");
            Long countField = valueEntity.getValue("directionCount");
            valueCount += statusField + " - " + String.valueOf(countField) + ", ";
        }
        labelCount.setValue(valueCount);
    }

    private ListDataProvider fillGantt(List<Employee> employees, Period periodWork, Period periodPause) {
        ListDataProvider dataProvider = new ListDataProvider();
        if (!employees.isEmpty()) {
            for (Employee employee : employees) {
                if (employee.getDateWorkStart() != null && employee.getDateWorkEnd() != null)
                    dataProvider.addItem(new MapDataItem(ParamsMap.of("category",
                            shortFIO(employee.getLastName(), employee.getFirstName(), employee.getMiddleName()) + ", " +
                                      employee.getDirection_work().getNameDirecting().toLowerCase(),
                            "segments", calcSegments(employee.getDateWorkStart(), employee.getDateWorkEnd(),
                                    periodWork, periodPause))));

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

    @Override
    public void init(Map<String, Object> params) {

        refreshCountEmployee(((Building) params.get("ITEM")).getId());

        buildingDs.addItemPropertyChangeListener(new Datasource.ItemPropertyChangeListener<Building>() {
            @Override
            public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<Building> e) {
                List<Employee> list = e.getItem().getEmployeeCk();
                if (e.getProperty().equals("periodWork")) {
                    dynamicFillGantt(list, getPeriodFromRuString((String) e.getValue()),
                            getPeriodFromRuString(e.getItem().getPeriodPause()));
                }
                if (e.getProperty().equals("periodPause")) {
                    dynamicFillGantt(list,
                            getPeriodFromRuString(e.getItem().getPeriodWork()),
                            getPeriodFromRuString((String) e.getValue()));
                }
                if (e.getProperty().equals("periodWork") || e.getProperty().equals("periodPause")) {
                    rebuildGroupCk(e.getItem().getEmployeeCk());
                }
                refreshCountEmployee(e.getItem().getId());
            }
        });

        // Пересчитаем вахты в группе СК
        rebuildGroupCk(((Building) params.get("ITEM")).getEmployeeCk());

        // обработка по окончании редактирования строки в таблице сотрудников СК
        dataGridEmployeeCK.addEditorCloseListener(event -> {
            employeeCkDs.getItem().setFieldStatus(statusStr(employeeCkDs.getItem(),
                    employeeCkDs.getItem().getDateWorkStart(),
                    employeeCkDs.getItem().getDateWorkEnd()));
            dynamicFillGantt(buildingDs.getItem().getEmployeeCk(),
                    getPeriodFromRuString(buildingDs.getItem().getPeriodWork()),
                    getPeriodFromRuString(buildingDs.getItem().getPeriodPause()));
        });
        // заполним первоначальные значения графика
        ganttChart.setVisible(false);
        dynamicFillGantt(((Building) params.get("ITEM")).getEmployeeCk(),
                getPeriodFromRuString(((Building) params.get("ITEM")).getPeriodWork()),
                getPeriodFromRuString(((Building) params.get("ITEM")).getPeriodPause()));
        // динамически заполняем график при открытии закладки
        tabSheetBuilding.addSelectedTabChangeListener(event -> {
                if (event.getSelectedTab().getName().equals("ganntCK")) {
                    dynamicFillGantt(buildingDs.getItem().getEmployeeCk(),
                            getPeriodFromRuString(buildingDs.getItem().getPeriodWork()),
                            getPeriodFromRuString(buildingDs.getItem().getPeriodPause()));                }
        });

        // Настройка карт
        //TextField place = (TextField) fieldGroup2.getField("place").getComponent();

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
                    return returnStatusColor(property, entity);
                }
            }
            return "default-status";
        });

    }

    private void rebuildGroupCk(List<Employee> employeeCk) {
        if (employeeCk != null) {
            for (Employee employee : employeeCk) {
                employee.setFieldStatus(statusStr(employee, employee.getDateWorkStart(), employee.getDateWorkEnd()));
            }
        }
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