package ru.bov.genesis.web.employee;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComponentContainer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.Years;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.client.shared.Resolution;
import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.SubStep;
import ru.bov.genesis.web.ToolsFunc;
import ru.bov.genesis.entity.GanttResolutionEnum;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.mainentity.Event;
import ru.bov.genesis.entity.services.StatusEmployeeEnum;
import ru.bov.genesis.entity.services.StorageFiles;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.util.*;

import static ru.bov.genesis.web.ToolsFunc.checkDateExpireTwoMonth;
import static ru.bov.genesis.utils.GlobalTools.fullFIO;
import static ru.bov.genesis.utils.GlobalTools.getPeriodFromRuString;

public class EmployeeEdit extends AbstractEditor<Employee> {

    @Inject
    private DateField dateFieldStart;
    @Inject
    private DateField dateFieldEnd;
    @Inject
    private LookupField lookupPeriodStep;

    @Inject
    private GroupBoxLayout groupBoxCalendar;

    @Inject
    private DateField udCkDateExpire;

    @Inject
    private Embedded imagePhoto;

    @Inject
    private Embedded imageSign;

    @Inject
    private Datasource<Employee> employeeDs;
    @Inject
    private CollectionDatasource eventsDs;
    @Inject
    private DataGrid<Event> eventDataGrid;

    @Inject
    private FileStorageService fileStorageService;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private Label labelNowStatus;

    @Inject
    private Label labelFio;

    @Inject
    private Label labelStatus;

    @Inject
    private Label labelNow;

    @Inject
    private FieldGroup pasportGroup;

    @Inject
    private ExportDisplay exportDisplay;

    @Named("eventDataGrid.edit")
    private EditAction eventTableEdit;
    @Named("eventDataGrid.create")
    private CreateAction eventTableCreate;
    @Named("eventDataGrid.remove")
    private RemoveAction eventTableRemove;

    private Gantt gantt;

    @Override
    public void init(Map<String, Object> params) {

        createGantt();
        ComponentContainer cc = (ComponentContainer) WebComponentsHelper.unwrap(groupBoxCalendar);
        cc.addComponent(gantt);

        udCkDateExpire.setStyleName(checkDateExpireTwoMonth(((Employee) params.get("ITEM")).getUdCkExpire()));

        if (((Employee) params.get("ITEM")).getBuilding() == null) {
            labelStatus.setValue(", не закреплен за объектом.");
        } else {
            labelStatus.setValue(", закреплен за: ");
        }

        Date birth = ((Employee) params.get("ITEM")).getBirthday();
        FieldGroup.FieldConfig config = pasportGroup.getField("ageMan");
        Label labelAge = (Label) config.getComponent();
        labelAge.setStyleName("h2");
        if (birth != null) {
            labelAge.setValue(getAgeMan(birth));
        }

        labelNow.setValue(LocalDate.now().toString("сегодня d MMMM YYYY года"));

        FileDescriptor imageFilePhoto = ((Employee) params.get("ITEM")).getImage_photo();
        FileDescriptor imageFileSign = ((Employee) params.get("ITEM")).getImage_sign();
        loadImage(imagePhoto, imageFilePhoto);
        loadImage(imageSign, imageFileSign);

        employeeDs.addItemPropertyChangeListener(event -> {
            loadImage(imagePhoto, event.getItem().getImage_photo());
            loadImage(imageSign, event.getItem().getImage_sign());
        });

        String fio = "<h1><div align=\"center\">" +
                fullFIO(((Employee) params.get("ITEM")).getLastName(),
                        ((Employee) params.get("ITEM")).getFirstName(),
                        ((Employee) params.get("ITEM")).getMiddleName())
                + "</div></h1>";
        labelFio.setValue(fio);

        dateFieldStart.addValueChangeListener(e -> {
            if (e.getValue() != null)
                gantt.setStartDate((Date) e.getValue());
        });
        dateFieldEnd.addValueChangeListener(e -> {
            if (e.getValue() != null)
                gantt.setEndDate((Date) e.getValue());
        });

        lookupPeriodStep.setValue(GanttResolutionEnum.day);
        lookupPeriodStep.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (e.getValue().equals(GanttResolutionEnum.week)) {
                    gantt.setResolution(Resolution.Week);
                } else if (e.getValue().equals(GanttResolutionEnum.day)) {
                    gantt.setResolution(Resolution.Day);
                }
            }
        });

        employeeDs.addItemChangeListener(new Datasource.ItemChangeListener<Employee>() {
            @Override
            public void itemChanged(Datasource.ItemChangeEvent<Employee> e) {
                int i = 0;
            }
        });
        employeeDs.addItemPropertyChangeListener(new Datasource.ItemPropertyChangeListener<Employee>() {
            @Override
            public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<Employee> e) {
                employeeDs.setAllowCommit(true);
                commit();
                employeeDs.setAllowCommit(false);
            }
        });

        employeeDs.setAllowCommit(false);
        eventsDs.setRefreshOnComponentValueChange(true);

        eventsDs.addItemPropertyChangeListener(event -> {
            //Datasource.CommitMode commitMode = event.getDs().getCommitMode();
            fillGant(employeeDs.getItem().getEvent());
        });

        eventsDs.addCollectionChangeListener(e -> {
            if (e.getItems().size() == 0) return;
            fillGant(employeeDs.getItem().getEvent());
        });

        // Добавление событий
        eventTableCreate.setBeforeActionPerformedHandler(() -> {
            Period periodWork;
            Period periodPause;
            Map<String, Object> values = new HashMap<>();
            List<Event> events = getItem().getEvent();
            if (getItem().getBuilding() == null) {
                periodWork = getPeriodFromRuString("2М");
                periodPause = getPeriodFromRuString("1М");
            } else {
                periodWork = getPeriodFromRuString(getItem().getBuilding().getPeriodWork());
                periodPause = getPeriodFromRuString(getItem().getBuilding().getPeriodPause());
            }
            if (events.size() == 0) {
                Date start = DateTime.now().toDate();
                values.put("startEvent", start);
                values.put("stopEvent", DateTime.now().plus(periodWork).toDate());
                values.put("description", "Вахта");
                values.put("status", StatusEmployeeEnum.fromId("1"));
                values.put("object", employeeDs.getItem().getBuilding());
                eventTableCreate.setInitialValues(values);
            } else {
                events.sort(new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        return o1.getStopEvent().compareTo(o2.getStopEvent());
                    }
                });
                Date start = events.get(events.size() - 1).getStopEvent();
                //Date stop = new DateTime(start).plusMonths(1).toDate();
                values.put("startEvent", start);
                values.put("object", getItem().getBuilding());
                if (events.get(events.size() - 1).getStatus() == StatusEmployeeEnum.fromId("1")) {
                    values.put("stopEvent", new DateTime(start).plus(periodPause).toDate());
                    values.put("description", "Межвахта");
                    values.put("status", StatusEmployeeEnum.fromId("2"));
                } else if (events.get(events.size() - 1).getStatus() == StatusEmployeeEnum.fromId("2")) {
                    values.put("stopEvent", new DateTime(start).plus(periodWork).toDate());
                    values.put("description", "Вахта");
                    values.put("status", StatusEmployeeEnum.fromId("1"));
                }
                eventTableCreate.setInitialValues(values);
            }
            return true;
        });

        eventTableCreate.setAfterCommitHandler(entity -> {
            if (((Event) entity).getObject() != null) {
                fillGant(employeeDs.getItem().getEvent());
            }
        });


    }

    @Override
    protected void postInit() {
        super.postInit();
        eventDataGrid.sort("startEvent", DataGrid.SortDirection.DESCENDING);
        checkAllAgeFields();
        fillGant(getItem().getEvent());
        if (getItem().getFieldStatus() != null)
            labelNowStatus.setValue(getItem().getFieldStatus());

    }

//    @Override
//    protected boolean postCommit(boolean committed, boolean close) {
//        getItem().setFieldStatus(GlobalTools.getStrStatus(getItem().getEvent()));
//        //getDsContext().commit();
//        return super.postCommit(committed, close);
//    }

    // Проверка всех дат на предмет скорого окончания
    private void checkAllAgeFields() {
        Collection<Component> components = this.getComponents();
        for (Component component : components) {
            String className = component.getClass().getCanonicalName();
            if (className.contains("DateField")) {
                String nameComponent = component.getId();
                if (!component.getId().equals("dateFieldStart")
                        && !component.getId().equals("dateFieldEnd")) {
                    String style = checkDateExpireTwoMonth(((DateField) component).getValue());
                    component.setStyleName(style);
                }
            } else if (className.contains("FieldGroup")) {
                List<FieldGroup.FieldConfig> list = ((FieldGroup) component).getFields();
                for (FieldGroup.FieldConfig fieldConfig : list) {
                    String classComp = fieldConfig.getComponent().getClass().getCanonicalName();
                    Component dateField = fieldConfig.getComponent();
                    if (dateField != null && classComp.contains("DateField")) {
                        if (!fieldConfig.getId().equals("birthDay")
                                && !fieldConfig.getId().equals("employmentDate")
                                && !fieldConfig.getId().equals("contractDate")
                                && !fieldConfig.getId().equals("dateAddresRegistration")) {
                            String style = checkDateExpireTwoMonth(((DateField) dateField).getValue());
                            dateField.setStyleName(style);
                        }
                    }
                }
            }
        }
    }

    private void loadImage(Embedded image, FileDescriptor imageFile) {
        if (imageFile == null) {
            image.setVisible(false);
            return;
        } else {
            image.setVisible(true);
        }
        byte[] bytes = null;
        try {
            bytes = fileStorageService.loadFile(imageFile);
        } catch (FileStorageException e) {
            showNotification("Файл не загружен - " + e.getLocalizedMessage(), NotificationType.ERROR);
        }
        if (bytes != null) {
            image.setSource(imageFile.getName(), new ByteArrayInputStream(bytes));
        } else {
            image.setVisible(false);
        }
    }

    public void openFile(Entity item, String columnId) {
        exportDisplay.show(((StorageFiles) item).getFile());
    }


    public Component ageManFunction(Datasource datasource, String fieldId) {
        Label age = componentsFactory.createComponent(Label.class);
        return age;
    }

    public String getAgeMan(Date birthday) {
        LocalDate ageMan = LocalDate.fromDateFields(birthday);
        Years years = Years.yearsBetween(ageMan, LocalDate.now());
        return String.valueOf(years.getYears());
    }

    private void fillGant(List<Event> events) {
        if (gantt == null) return;
        if (events == null) return;
        gantt.removeSteps();
        List<SubStep> stepList = new ArrayList<>();
        Step step = new Step("");
        step.setDescription("");

         for (Event event : events) {
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
            stepList.add(subStep);
        }

        step.addSubSteps(stepList);
        gantt.addStep(step);
    }

    private void createGantt() {
        gantt = new Gantt();
        gantt.setWidth(100, Sizeable.Unit.PERCENTAGE);
        gantt.setHeight(100, Sizeable.Unit.PIXELS);
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
    }
}