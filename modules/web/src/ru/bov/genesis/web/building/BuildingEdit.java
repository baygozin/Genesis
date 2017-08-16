package ru.bov.genesis.web.building;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.AddAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.ExcludeAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComponentContainer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.client.shared.Resolution;
import org.tltv.gantt.client.shared.SubStep;
import ru.bov.genesis.web.ToolsFunc;
import ru.bov.genesis.entity.GanttResolutionEnum;
import ru.bov.genesis.entity.mainentity.Building;
import ru.bov.genesis.entity.mainentity.ClaimCk;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.mainentity.Event;
import ru.bov.genesis.entity.services.StatusEmployeeEnum;
import ru.bov.genesis.utils.GlobalTools;
import ru.bov.genesis.web.BovStep;
import ru.bov.genesis.web.MainGantt;
import ru.bov.genesis.web.TableGanttLayout;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static ru.bov.genesis.utils.GlobalTools.returnStatusColor;

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
    private HierarchicalDatasource<ClaimCk, UUID> claimCkDs;
    @Inject
    private Datasource<Building> buildingDs;

    private CollectionDatasource employeeFilterDs;

    @Inject
    private LookupField typeMap;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private DataGrid<Employee> dataGridEmployeeCK;

    @Inject
    private DateField fieldDateWorkStart;

    @Inject
    private TabSheet tabSheetBuilding;
    @Inject
    private TextField place;
    @Inject
    private CollectionDatasource<KeyValueEntity, Object> countDirectionDs;
    @Inject
    private CollectionDatasource<KeyValueEntity, Object> countDirectionWorkDs;
    @Inject
    private Label labelCount;
    @Inject
    private Label labelNow;
    @Inject
    private LookupField lookupFieldFilter;
    @Inject
    private LookupField lookupFieldGantt;
    @Inject
    private DataManager dataManager;
    @Inject

    private GroupBoxLayout groupBoxGantt;
    private Gantt gantt;
    private TableGanttLayout ganttLayout;

    @Named("dataGridEmployeeCK.edit")
    private EditAction todosEmployeeEdit;
    @Named("dataGridEmployeeCK.add")
    private AddAction todosEmployeeAdd;
    @Named("dataGridEmployeeCK.exclude")
    private ExcludeAction todoEmployeeExclude;

    @Inject
    Metadata metadata;
    @Inject
    private TreeTable<ClaimCk> claimCkTable;
    @Inject
    private CollectionDatasource<Employee, UUID> employeeCkDs;

    // функция заполнения графика данными
    private void dynamicNewFillGantt(Collection<Employee> list) {
        if (list != null && !list.isEmpty()) {
            fillNewGantt(list);
        }
    }

    // Строка с количеством подгрупп специалистов
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

    // Создаем заявку на специалистов
    public void onCreateClaim() {
        ClaimCk claimCk = metadata.create(ClaimCk.class);
        claimCk.setBuilding(getItem());
        getItem().getClaimCk().add(claimCk);
        AbstractEditor editor = openEditor("genesis$ClaimCk.edit.copy1", claimCk, WindowManager.OpenType.DIALOG);
        editor.addCloseListener(actionId -> {
            ClaimCk ck = (ClaimCk) editor.getItem();
            if (ck.getClaimDate() == null) claimCkDs.removeItem(ck);
            claimCkDs.refresh();

        });
    }

    // Создаем вложенную специальность в заявке
    public void onCreateSpec() {
        ClaimCk item = claimCkTable.getSingleSelected();
        ClaimCk claimCk = metadata.create(ClaimCk.class);
        if (item != null) {
            claimCk.setBuilding(getItem());
            if (item.getParentClaim() == null)
                claimCk.setParentClaim(item);
            else
                claimCk.setParentClaim(item.getParentClaim());
            getItem().getClaimCk().add(claimCk);
        }
        AbstractEditor editor = openEditor("genesis$ClaimCk.edit.copy2", claimCk, WindowManager.OpenType.DIALOG);
        editor.addCloseListener(actionId -> {
            ClaimCk ck = (ClaimCk) editor.getItem();
            if (ck.getSpecialty() == null || ck.getNumberSpeciality() == null) claimCkDs.removeItem(ck);
            expandTreeTableClaim(claimCkTable, 1);
            claimCkDs.refresh();
        });
    }

    // Удаляем заявку или специальность
    public void onRemoveClaim() {
        ClaimCk item = claimCkTable.getSingleSelected();
        if (item.getParentClaim() == null) {
            Collection<ClaimCk> claimCkList = claimCkDs.getItems();
            Collection<ClaimCk> collection = new ArrayList<>();
            for (ClaimCk claimCk : claimCkList) if (claimCk.getParentClaim() == item) collection.add(claimCk);
            collection.add(item);
            for (ClaimCk ck : collection) claimCkDs.removeItem(ck);
        } else claimCkDs.removeItem(item);
        claimCkDs.refresh();
    }

    // Генерация колонки с текущим кол-м сотрудников данной специальности
    public Component generateNumberCurrentCell(ClaimCk entity) {
        String numberSpec = "0";
        Label numberMan = componentsFactory.createComponent(Label.class);
        List<ClaimCk> ckList = getItem().getClaimCk();
        Date lastDate = new DateTime().withDate(1, 1, 1).toDate();
        for (ClaimCk claimCk : ckList) {
            if (claimCk.getClaimDate() != null && claimCk.getClaimDate().after(lastDate)) {
                lastDate = claimCk.getClaimDate();
            }
        }
        if (entity.getParentClaim() != null && entity.getParentClaim().getClaimDate().equals(lastDate)) {
            // тут надо вытянуть из списка Ск количество специалистов
            countDirectionDs.refresh(ParamsMap.of("id", getItem().getId() ));
            Collection<KeyValueEntity> valueEntities = countDirectionDs.getItems();
            for (KeyValueEntity valueEntity : valueEntities) {
                String currentSpec = entity.getSpecialty().getNameDirecting();
                String spec = valueEntity.getValue("directionName");
                Long countField = valueEntity.getValue("directionCount");
                if (spec.toUpperCase().equals(entity.getSpecialty().getNameDirecting().toUpperCase())) {
                    numberSpec = String.valueOf(countField);
                    break;
                }
            }
        }
        if (entity.getParentClaim() == null) numberSpec = "";
        if (entity.getParentClaim() != null && !entity.getParentClaim().getClaimDate().equals(lastDate)) numberSpec = "";
        numberMan.setValue(numberSpec);
        return numberMan;
    }

    // Генерация колонки с количеством человек на вахте данной специальности
    public Component generateNumberWorkCell(ClaimCk entity) {
        String numberSpec = "0";
        Label numberMan = componentsFactory.createComponent(Label.class);
        List<ClaimCk> ckList = getItem().getClaimCk();
        Date lastDate = new DateTime().withDate(1, 1, 1).toDate();
        for (ClaimCk claimCk : ckList) { // Находим последнюю заявку
            if (claimCk.getClaimDate() != null && claimCk.getClaimDate().after(lastDate)) {
                lastDate = claimCk.getClaimDate();
            }
        }
        if (entity.getParentClaim() != null && entity.getParentClaim().getClaimDate().equals(lastDate)) {
            // тут надо вытянуть из списка Ск количество специалистов
            countDirectionWorkDs.refresh(ParamsMap.of(
                    "id", getItem().getId(),
                    "direction", entity.getSpecialty().getNameDirecting()));
            Collection<KeyValueEntity> valueEntities = countDirectionWorkDs.getItems();
            for (KeyValueEntity valueEntity : valueEntities) {
                String currentSpec = entity.getSpecialty().getNameDirecting();
                String spec = valueEntity.getValue("directionName");
                Long countField = valueEntity.getValue("directionCount");
                if (spec.toUpperCase().equals(entity.getSpecialty().getNameDirecting().toUpperCase())) {
                    numberSpec = String.valueOf(countField);
                    break;
                }
            }
        }
        if (entity.getParentClaim() == null) numberSpec = "";
        if (entity.getParentClaim() != null && !entity.getParentClaim().getClaimDate().equals(lastDate)) numberSpec = "";


        numberMan.setValue(numberSpec);
        return numberMan;
    }

    // Подбор сотрудников
    public void onRecrutSpec(Component source) {
        ClaimCk item = claimCkTable.getSingleSelected();
        if (item != null && item.getParentClaim() == null) {
            showNotification("Не выбрана специальность");
            return;
        }
        String spec = item.getSpecialty().getNameDirecting();
        openLookup("genesis$Employee.browse",items -> addEmployeeToGroupCk(items),
                WindowManager.OpenType.THIS_TAB, ParamsMap.of("directing", spec));
    }

    // Добавить сотрудника(ов) в группу СК
    private void addEmployeeToGroupCk(Collection<Employee> items) {
        if (!items.isEmpty()) {
            List<Employee> employeeList = new ArrayList<>(items);
            for (Employee employee : employeeList) {
                employee.setBuilding(getItem());
            }
            getItem().getEmployeeCk().addAll(employeeList);
            CommitContext commitContext = new CommitContext(getItem().getEmployeeCk());
            dataManager.commit(commitContext);
            buildingDs.refresh();
            employeeFilterDs.refresh();
            rebuildGroupCk(getItem().getEmployeeCk());
            fillNewGantt(getItem().getEmployeeCk());
        }
    }

    @Override
    public void ready() {
        super.ready();
        expandTreeTableClaim(claimCkTable, 1);
    }

    private void expandTreeTableClaim(TreeTable treeTable, int expandLevelCount) {
        treeTable.expand(getLastClaim().getId());
    }

    private ClaimCk getLastClaim() {
        List<ClaimCk> ckList = getItem().getClaimCk();
        ClaimCk lastCk = new ClaimCk();
        Date lastDate = new DateTime().withDate(1, 1, 1).toDate();
        for (ClaimCk claimCk : ckList) {
            if (claimCk.getClaimDate() != null && claimCk.getClaimDate().after(lastDate)) {
                lastDate = claimCk.getClaimDate();
                lastCk = claimCk;
            }
        }
        return lastCk;
    }

    @Override
    public void init(Map<String, Object> params) {

        todosEmployeeEdit.setAfterCommitHandler(entity -> {
            commit();
        });

        todosEmployeeAdd.setAfterAddHandler(items -> {
            addEmployeeToGroupCk(items);
        });

        todoEmployeeExclude.setAfterRemoveHandler(items -> {
            List<Employee> employeeList = new ArrayList<>(items);
            for (Employee employee : employeeList) {
                employee.setBuilding(null);
            }
            getItem().getEmployeeCk().removeAll(employeeList);
            commit();
            rebuildGroupCk(getItem().getEmployeeCk());
            fillNewGantt(getItem().getEmployeeCk());
        });

        employeeFilterDs = new DsBuilder(getDsContext())
                .setJavaClass(Employee.class)
                //.setViewName(View.MINIMAL)
                .setViewName("employee-view")
                .setId("employeeFilterId")
                .buildCollectionDatasource();

        employeeFilterDs.addCollectionChangeListener(e -> {
            rebuildGroupCk(getItem().getEmployeeCk());
            fillNewGantt(getItem().getEmployeeCk());
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

        // Цвет статуса сотрудника
        dataGridEmployeeCK.addCellStyleProvider((entity, property) -> {
                if (entity != null) {
                    if (property.equals("fieldStatus")) {
                        return returnStatusColor(entity);
                    }
                }
                return "";
        });

        // Выбор специальности в группе СК
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
        claimCkTable.sortBy(claimCkTable.getDatasource().getMetaClass().getPropertyPath("claimDate"), false);
    }

    @Override
    protected boolean preCommit() {
        buildingDs.getItem().setPlaceScale(googleMapView.getZoom());
        //employeeFilterDs.refresh();
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
                if (event.getStatus() == StatusEmployeeEnum.fromId("1")) {
                    subStep.setBackgroundColor(ToolsFunc.colorWork);
                } else if (event.getStatus() == StatusEmployeeEnum.fromId("2")) {
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
        employee = dataManager.reload(employee, "employee-view");
        List<Event> events = employee.getEvent();
        if (events.size() == 0) {
            return "График не задан";
        }
        String stat = GlobalTools.getStrStatus(events);
        return stat;
    }

}