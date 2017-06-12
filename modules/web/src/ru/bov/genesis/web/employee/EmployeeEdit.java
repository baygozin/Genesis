package ru.bov.genesis.web.employee;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.theme.HaloTheme;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.Years;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.services.StorageFiles;

import javax.ejb.Local;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ru.bov.genesis.ToolsFunc.checkDateExpireTwoMonth;
import static ru.bov.genesis.utils.GlobalTools.fullFIO;

public class EmployeeEdit extends AbstractEditor<Employee> {

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


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);


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
                String style = checkDateExpireTwoMonth(((DateField)component).getValue());
                component.setStyleName(style);
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
        //age.setDatasource(datasource, fieldId);
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
}