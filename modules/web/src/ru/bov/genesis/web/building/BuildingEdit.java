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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.*;

import static ru.bov.genesis.Func.fullFIO;
import static ru.bov.genesis.Func.shortFIO;

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

    private int lengthCalendar; // Ширина отображаемой диаграммы в месяцах
    private int minusCalc = 0; // Начало расчета сегментов в месяцах от сегодня
    private int plusCalc = 4; // Конец расчета сегментов в месяцах от сегодня
    private String colorWork = "#008800";
    private String colorPause = "#880000";


    // Расчитаем сегменты для диаграммы
    private List<MapDataItem> calcSegments(LocalDate dateStart,
                                           LocalDate dateEnd,
                                           int periodWork,
                                           int periodPause) {
        LocalDate now = new LocalDate();
        LocalDate startCalc = now.minusMonths(minusCalc); // начало расчета сегментов - текущая дата минус "minusCalc"
        LocalDate stopCalc = now.plusMonths(plusCalc);    // конец расчета сегментов - текущая дата плюс "plusCalc"
        List<MapDataItem> segments = new ArrayList<>();

        LocalDate start = dateStart;
        LocalDate stop = dateEnd;
        for (LocalDate dateI = startCalc; dateI.isBefore(stopCalc); ) {
            stop = start.plusMonths(periodWork);
            segments.add(setItemWork(start, stop));
            start = stop;
            stop = start.plusMonths(periodPause);
            segments.add(setItemPause(start.plusDays(1), stop.minusDays(1)));
            start = stop;
            dateI = dateI.plusMonths(3);
        }
        return segments;
    }

    private MapDataItem setItemWork(LocalDate start, LocalDate end) {
        return new MapDataItem(ParamsMap.of("start", start.toString(),
                "end", end.toString(), "task",
                "Вахта", "color", colorWork));
    }

    private MapDataItem setItemPause(LocalDate start, LocalDate end) {
        return new MapDataItem(ParamsMap.of("start", start.toString(),
                "end", end.toString(), "task",
                "Межвахта", "color", colorPause));
    }

    @Override
    public void init(Map<String, Object> params) {

        ListDataProvider dataProvider = new ListDataProvider();
        List<MapDataItem> segments = new ArrayList<>();
        ganttChart.setVisible(false);
        if (!((Building) params.get("ITEM")).getEmployeeCk().isEmpty()) {
            ganttChart.setVisible(true);
            for (Employee employee : ((Building) params.get("ITEM")).getEmployeeCk()) {
                dataProvider.addItem(new MapDataItem(ParamsMap.of("category",
                        shortFIO(employee.getLastName(), employee.getFirstName(), employee.getMiddleName()),
                        "segments", calcSegments(
                                new LocalDate(employee.getDateWorkStart()),
                                new LocalDate(employee.getDateWorkEnd()),
                                2, 1))));
            }
        }

        ganttChart.setDataProvider(dataProvider);


        TextField place = (TextField) fieldGroup.getField("place").getComponent();
        place.addTextChangeListener(new TextInputField.TextChangeListener() {
            @Override
            public void textChange(TextInputField.TextChangeEvent event) {
                Double[] coord = getCoordinates(event.getText());
                googleMapView.setCenter(googleMapView.createGeoPoint(coord[0], coord[1]));
            }
        });
        googleMapView.addMapClickListener(new MapClickListener() {
            @Override
            public void onClick(MapClickEvent event) {
                Double latitude = event.getPosition().getLatitude();
                Double longtitude = event.getPosition().getLongitude();
                String pl1 = String.format("%.6f", latitude).replace(",", ".");
                String pl2 = String.format("%.6f", longtitude).replace(",", ".");
                place.setValue(pl1 + ", " + pl2);
            }
        });

        typeMap.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChanged(ValueChangeEvent e) {
                googleMapView.setMapType(e.getValue().toString());
            }
        });

        Double[] coord = new Double[2];
        coord = getCoordinates(((Building) params.get("ITEM")).getPlace());
        Integer scale = ((Building) params.get("ITEM")).getPlaceScale();
        GeoPoint center = googleMapView.createGeoPoint(coord[0], coord[1]);
        if (scale == null) scale = 15;
        googleMapView.setCenter(center);
        googleMapView.setZoom(scale);

        dataGridEmployeeCK.setEditorEnabled(true);
        dataGridEmployeeCK.addGeneratedColumn("fieldFio", new DataGrid.ColumnGenerator<Employee, String>() {
            @Override
            public String getValue(DataGrid.ColumnGeneratorEvent<Employee> event) {
                return fullFIO(event.getItem().getLastName(), event.getItem().getFirstName(), event.getItem().getMiddleName());
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

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