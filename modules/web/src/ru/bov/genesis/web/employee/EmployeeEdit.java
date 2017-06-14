package ru.bov.genesis.web.employee;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComponentContainer;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.client.shared.Resolution;
import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.SubStep;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.services.StorageFiles;
import ru.bov.genesis.BovStep;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.util.*;

import static ru.bov.genesis.ToolsFunc.checkDateExpireTwoMonth;
import static ru.bov.genesis.utils.GlobalTools.fullFIO;

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
    private FileStorageService fileStorageService;

    @Named("fieldGroup.image")
    private FileUploadField uploadField;

    @Inject
    private Frame windowActions;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private Label labelFio;

    @Inject
    private Label labelStatus;

    @Inject
    private Label labelNow;

    @Inject
    private Label ageMan;

    @Inject
    private FieldGroup fieldGroupPersonal;

    @Inject
    private FieldGroup pasportGroup;

    @Inject
    private ExportDisplay exportDisplay;

    @Inject
    private CheckBox freeEmployeeField;

    private Gantt gantt;


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        createGantt();
        ComponentContainer cc = (ComponentContainer) WebComponentsHelper.unwrap(groupBoxCalendar);
        cc.addComponent(gantt);

        udCkDateExpire.setStyleName(checkDateExpireTwoMonth(((Employee) params.get("ITEM")).getUdCkExpire()));

        if (((Employee) params.get("ITEM")).getBuilding() == null) {
            labelStatus.setValue(", не закреплен за объектом");
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

        lookupPeriodStep.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                String value = e.getValue().toString();
                if (value.equals("day")) {
                    gantt.setResolution(Resolution.Day);
                } else if (value.equals("week")) {
                    gantt.setResolution(Resolution.Week);
                }
            }
        });

    }

    @Override
    protected void postInit() {
        super.postInit();
        checkAllAteFields();
    }

    private void checkAllAteFields() {
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
            showNotification("Файл не загружен", NotificationType.HUMANIZED);
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

    public void changeDateAction() {
        showNotification("Hello!", NotificationType.TRAY);
    }

    private void createGantt() {
        gantt = new Gantt();

        gantt.setWidth(100, Sizeable.Unit.PERCENTAGE);
        gantt.setHeight(100, Sizeable.Unit.PIXELS);
        gantt.setResizableSteps(true);
        gantt.setMovableSteps(true);
        gantt.setLocale(Locale.forLanguageTag("ru"));
        gantt.setResolution(Resolution.Week);
        Date startDate = LocalDate.now().toDate();
        Date endDate = LocalDate.now().plusYears(1).toDate();
        gantt.setStartDate(startDate);
        gantt.setEndDate(endDate);
        dateFieldStart.setValue(startDate);
        dateFieldEnd.setValue(endDate);

        Step step = new Step();
        step.setStartDate(LocalDate.now().minusMonths(1).toDate());
        step.setEndDate(LocalDate.now().plusMonths(1).toDate());
        step.setDescription("");

        SubStep subStep_1 = new SubStep("Вахта 1");
        subStep_1.setStartDate(LocalDate.now().minusMonths(1).toDate());
        subStep_1.setEndDate(LocalDate.now().plusMonths(1).toDate());
        subStep_1.setDescription("");
        SubStep subStep_2 = new SubStep("Вахта 2");
        subStep_2.setStartDate(LocalDate.now().plusMonths(2).toDate());
        subStep_2.setEndDate(LocalDate.now().plusMonths(4).toDate());
        subStep_2.setDescription("");
        SubStep subStep_3 = new SubStep("Вахта 3");
        subStep_3.setStartDate(LocalDate.now().plusMonths(5).toDate());
        subStep_3.setEndDate(LocalDate.now().plusMonths(7).toDate());
        subStep_3.setDescription("");

        step.addSubStep(subStep_1);
        step.addSubStep(subStep_2);
        step.addSubStep(subStep_3);

        gantt.addStep(step);

//        gantt.addMoveListener(e -> {
//        });
//        gantt.addResizeListener(e -> {
//        });
    }


}