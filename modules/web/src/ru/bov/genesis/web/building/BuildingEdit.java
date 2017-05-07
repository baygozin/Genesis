package ru.bov.genesis.web.building;

import com.haulmont.bali.util.ParamsMap;
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
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public void init(Map<String, Object> params) {

        ListDataProvider dataProvider = new ListDataProvider();
        List<MapDataItem> segments = new ArrayList<>();
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-01",
                "end", "2016-01-14", "task", "Уборка територии", "color", "#b9783f")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-16",
                "end", "2016-01-27", "task", "Отхожий промысел")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-02-05",
                "end", "2016-04-18", "task", "Заготовка дров")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-04-18",
                "end", "2016-04-30", "task", "Бездельничаем")));
        dataProvider.addItem(new MapDataItem(ParamsMap.of("category", "Дядя Петя", "segments", segments)));

        segments = new ArrayList<>();
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-01",
                "end", "2016-01-10", "task", "Варим борЖч", "color", "#cc4748")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-12",
                "end", "2016-01-15", "task", "Лепим пельмени")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-16",
                "end", "2016-02-05", "task", "Пришиваем заплаты")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-02-10",
                "end", "2016-02-18", "task", "Извращаемся")));
        dataProvider.addItem(new MapDataItem(ParamsMap.of("category", "Тетя Мотя", "segments", segments)));

        segments = new ArrayList<>();
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-01", "end", "2016-02-10",
                "task", "Вахта", "color", "#00aa00")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-02-10", "end", "2016-02-28",
                "task", "Межахта", "color", "#990000")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-02-28", "end", "2016-03-30",
                "task", "Вахта", "color", "#00aa00")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-03-30", "end", "2016-04-30",
                "task", "Межвахта", "color", "##990000")));
        dataProvider.addItem(new MapDataItem(ParamsMap.of("category", "сосед Филаретыч", "segments", segments)));

        segments = new ArrayList<>();
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-01-01", "end", "2016-02-10",
                "task", "Вахта", "color", "#990000")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-02-10", "end", "2016-02-28",
                "task", "Межахта", "color", "#00aa00")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-02-28", "end", "2016-03-30",
                "task", "Вахта", "color", "#990000")));
        segments.add(new MapDataItem(ParamsMap.of("start", "2016-03-30", "end", "2016-04-30",
                "task", "Межвахта", "color", "##00aa00")));
        dataProvider.addItem(new MapDataItem(ParamsMap.of("category", "папа Римский", "segments", segments)));

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
                String pl1 = String.format("%.6f", latitude).replace(",",".");
                String pl2 = String.format("%.6f", longtitude).replace(",",".");
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
               return Func.fullFIO(event.getItem().getLastName(), event.getItem().getFirstName(), event.getItem().getMiddleName());
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