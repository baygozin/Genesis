package ru.bov.genesis.web.building;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.components.charts.Chart;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.data.ListDataProvider;
import com.haulmont.charts.gui.data.MapDataItem;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComponentContainer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.client.shared.Resolution;
import org.tltv.gantt.client.shared.SubStep;
import ru.bov.genesis.ToolsFunc;
import ru.bov.genesis.entity.GanttResolutionEnum;
import ru.bov.genesis.entity.mainentity.Building;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.mainentity.Event;
import ru.bov.genesis.entity.services.CellsCk;
import ru.bov.genesis.entity.services.StausEmployeeEnum;
import ru.bov.genesis.utils.GlobalTools;
import ru.bov.genesis.web.BovStep;
import ru.bov.genesis.web.MainGantt;
import ru.bov.genesis.web.TableGanttLayout;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static ru.bov.genesis.ToolsFunc.calcSegments;
import static ru.bov.genesis.utils.GlobalTools.*;

public class BuildingEdit extends AbstractEditor<Building> {

    @Inject
    private DateField dateFieldStart;
    @Inject
    private DateField dateFieldEnd;
    @Inject
    private LookupField lookupPeriodStep;

    @Inject
    private MapViewer googleMapView;

    @Inject
    private FieldGroup fieldGroup1;
    @Inject
    private FieldGroup fieldGroup2;

    @Inject
    private VBoxLayout vBoxCalendar;

    @Inject
    private TabSheet.Tab tabNewGant;
    @Inject
    private Datasource<Building> buildingDs;

    @Inject
    private CollectionPropertyDatasourceImpl<Employee, UUID> employeeCkDs;

    @Inject
    private GroupDatasource<Employee, UUID> employeeGroupDs;

    private CollectionDatasource employeeFilterDs;

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
    @Inject
    private Label labelNow;
    @Inject
    private LookupField lookupFieldFilter;
    @Inject
    private LookupField lookupFieldGantt;
    @Inject
    private GroupBoxLayout groupBoxCalendar;
    @Inject
    private DataManager dataManager;

    @Inject
    private GroupBoxLayout groupBoxGantt;
    private Gantt gantt;
    private TableGanttLayout ganttLayout;

    @Named("dataGridEmployeeCK.edit")
    private EditAction todosEmployeeEdit;

    // функция заполнения графика данными
    private void dynamicNewFillGantt(Collection<Employee> list) {
        if (list != null && !list.isEmpty()) {
            fillNewGantt(list);
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

    @Override
    public void init(Map<String, Object> params) {

        todosEmployeeEdit.setAfterCommitHandler(entity -> {
            employeeFilterDs.refresh();
        });

        employeeFilterDs = new DsBuilder(getDsContext())
                .setJavaClass(Employee.class)
                //.setViewName(View.MINIMAL)
                .setViewName("employee-view")
                .setId("employeeFilterId")
                .buildCollectionDatasource();

        employeeFilterDs.addCollectionChangeListener(e -> {
            if (e.getOperation() == CollectionDatasource.Operation.ADD) {
//                List<Employee> employeeList = e.getItems();
//                for (Employee employee : employeeList) {
//                    employee.setBuilding(buildingDs.getItem());
//                    buildingDs.getItem().getEmployeeCk().add(employee);
//                }
            } else if (e.getOperation() == CollectionDatasource.Operation.REMOVE) {

//                List<Employee> employeeList = e.getItems();
//                if (buildingDs.getItem().getEmployeeCk().contains(employeeList.get(0))) {
//                    buildingDs.getItem().getEmployeeCk().removeAll(employeeList);
//                    for (Employee employee : employeeList) {
//                        employee.setBuilding(null);
//                        dataManager.commit(employee);
//                    }
//                }
            }
            rebuildGroupCk(buildingDs.getItem().getEmployeeCk());
            fillNewGantt(buildingDs.getItem().getEmployeeCk());
        });

        labelNow.setValue(LocalDate.now().toString("сегодня d MMMM YYYY года"));

        refreshCountEmployee(((Building) params.get("ITEM")).getId());

        buildingDs.addItemPropertyChangeListener(new Datasource.ItemPropertyChangeListener<Building>() {
            @Override
            public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<Building> e) {
                Collection<Employee> list = e.getItem().getEmployeeCk();
                rebuildGroupCk(e.getItem().getEmployeeCk());
                refreshCountEmployee(e.getItem().getId());
            }
        });


        // Перечитаем вахты в группе СК
        rebuildGroupCk(((Building) params.get("ITEM")).getEmployeeCk());

        // Настройка карт
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
        //dataGridEmployeeCK.setEditorEnabled(false);

        // Цвет статуса сотрудника
        dataGridEmployeeCK.addCellStyleProvider((entity, property) -> {
                if (entity != null) {
                    if (property.equals("fieldStatus")) {
                        return returnStatusColor(entity);
                    }
                }
                return "";
        });

        lookupFieldFilter.addValueChangeListener(e -> {
            if (e.getValue() != null)
                setEmployeeFiltered(e.getValue().toString());
            else {
                setEmployeeFiltered("");
            };
            lookupFieldGantt.setValue(e.getValue());
        });

        lookupFieldGantt.addValueChangeListener(e ->{
            if (e.getValue() != null) {
                setEmployeeFiltered(e.getValue().toString());
            } else {
                setEmployeeFiltered("");
            }
            lookupFieldFilter.setValue(e.getValue());
        });
        // создаем график работы сотрудников
        createGantt();
        ganttLayout = new TableGanttLayout(gantt);
        ComponentContainer cc = (ComponentContainer) WebComponentsHelper.unwrap(groupBoxGantt);
        cc.addComponent(ganttLayout);
        ganttLayout.setHeight(100, Sizeable.Unit.PERCENTAGE);
        lookupPeriodStep.setValue(GanttResolutionEnum.day);
        lookupPeriodStep.addValueChangeListener(e -> {
                if (e.getValue().equals(GanttResolutionEnum.week)) {
                    gantt.setResolution(Resolution.Week);
                } else if (e.getValue().equals(GanttResolutionEnum.day)) {
                    gantt.setResolution(Resolution.Day);
                }
        });

        dateFieldStart.addValueChangeListener(e -> {
            gantt.setStartDate((Date) e.getValue());
        });

        dateFieldEnd.addValueChangeListener(e -> {
            gantt.setEndDate((Date) e.getValue());
        });

    }

    @Override
    protected void postInit() {
        dataGridEmployeeCK.setDatasource(employeeFilterDs);
        fillCkFilter();
        fillNewGantt(buildingDs.getItem().getEmployeeCk());
        setEmployeeFiltered("");
    }

    @Override
    protected boolean preCommit() {
        buildingDs.getItem().setPlaceScale(googleMapView.getZoom());
        employeeFilterDs.refresh();
        return super.preCommit();
    }

    private void createGantt() {
        gantt = new MainGantt();
        gantt.setSizeFull();
        gantt.setResizableSteps(false);
        gantt.setMovableSteps(false);
        gantt.setLocale(Locale.forLanguageTag("ru"));
        gantt.setResolution(Resolution.Day);
        Date startDate = LocalDate.now().toDate();
        Date endDate = LocalDate.now().plusMonths(6).toDate();
        gantt.setStartDate(startDate);
        gantt.setEndDate(endDate);
        dateFieldStart.setValue(startDate);
        dateFieldEnd.setValue(endDate);
        gantt.addMoveListener(e -> {
            //changeEvent(e.getPreviousStartDate(), e.getPreviousEndDate(), e.getStartDate(), e.getEndDate());
            //showNotification("Перемещение вехи " + e.getStep().getCaption());
        });

        gantt.addResizeListener(e -> {
            //changeEvent(e.getPreviousStartDate(), e.getPreviousEndDate(), e.getStartDate(), e.getEndDate());
            //showNotification("Изменение вехи " + e.getStep().getCaption());
        });

        gantt.addClickListener(e -> {
            if (e.getDetails().isDoubleClick()) {
                //editEvent(e.getStep());
                showNotification(e.getStep().getCaption() + " <-> " +e.getStep().getDescription() +
                        " <-> " + e.getStep().getDescription());
            } else {
                //showNotification("Одинарный клик на вехе " + e.getStep().getCaption());
            }
        });
    }

    private void fillNewGantt(Collection<Employee> employeeCollection){
        if (gantt == null) return;
        if (employeeCollection == null) return;
        gantt.removeSteps();
        for (Employee employee : employeeCollection) {
            BovStep step = new BovStep();
            employee = dataManager.reload(employee, "employee-view");
            step.setDescription("");
            //step.setCaption(employee.getFullName());
            step.setDescription(employee.getFullName());
            step.setDirectEmployee(employee.getDirection_work().getNameDirecting());
            if (employee.getEvent() == null) continue;
            List<Event> eventList = employee.getEvent();
            List<SubStep> subStepList = new ArrayList<>();
            for (Event event : eventList) {
                SubStep subStep = new SubStep(event.getDescription());
                subStep.setStartDate(event.getStartEvent());
                subStep.setEndDate(event.getStopEvent());
                if (event.getDescription() != null) {
                    subStep.setDescription(event.getDescription());
                } else {
                    subStep.setDescription("");
                }
                if (event.getStatus() == StausEmployeeEnum.fromId("1")) {
                    subStep.setBackgroundColor(ToolsFunc.colorWork);
                } else if (event.getStatus() == StausEmployeeEnum.fromId("2")) {
                    subStep.setBackgroundColor(ToolsFunc.colorPause);
                }
                subStepList.add(subStep);
            }
            step.addSubSteps(subStepList);
            gantt.addStep(step);
            ganttLayout.setContainer((Collection) gantt.getSteps());
        }
    }

    private void setEmployeeFiltered(String direct) {
        String qqq;
        UUID uuid = buildingDs.getItem().getId();
        if (!direct.isEmpty()) {
            qqq = "select e from genesis$Employee e " +
                    "where e.direction_work.nameDirecting = '" + direct +"' and e.building.id = '" + uuid + "'";
        } else {
            qqq = "select e from genesis$Employee e " +
                    "where e.building.id = '" + uuid + "'";
        }
        employeeFilterDs.setQuery(qqq);
        employeeFilterDs.refresh();

        dynamicNewFillGantt(employeeFilterDs.getItems());
    }

    private void fillCkFilter() {
        // заполняем простенький фильтр
        Set<String> listDirect = new HashSet<>();
        List<Employee> employeeList = buildingDs.getItem().getEmployeeCk();
        if (employeeList == null) return;
        for (Employee employee : employeeList) {
            // Тут сделать проверку на null
            listDirect.add(employee.getDirection_work().getNameDirecting());
        }
        List<String> list = new ArrayList<>();
        list.addAll(listDirect);
        lookupFieldFilter.setOptionsList(list);
        lookupFieldGantt.setOptionsList(list);
    }

    private void rebuildGroupCk(List<Employee> employeeCk) {
        if (employeeCk != null) {
            for (Employee employee : employeeCk) {
                employee.setFieldStatus(statusStr(employee));
            }
        }
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

    // Возвращает строку со статусом сотрудника
    public String statusStr(Employee employee) {
        if (employee.getBuilding() == null) {
            return "Свободен";
        }
        DateTime now = DateTime.now();
        employee = dataManager.reload(employee, "employee-view");
        List<Event> events = employee.getEvent();
        if (events.size() == 0) {
            return "График не задан";
        }
        return GlobalTools.getStrStatus(events);
    }

}