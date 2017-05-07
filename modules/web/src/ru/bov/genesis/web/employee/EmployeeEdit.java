package ru.bov.genesis.web.employee;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.bov.genesis.Func;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.services.StorageFiles;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public class EmployeeEdit extends AbstractEditor<Employee> {
    @Inject
    private Embedded imagePhoto;

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
    private FieldGroup fieldGroupPersonal;

    @Inject
    private ExportDisplay exportDisplay;

    @Inject
    private CheckBox freeEmployeeField;


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        FileDescriptor imageFile = ((Employee) params.get("ITEM")).getImage();
        loadImage(imageFile);

        employeeDs.addItemPropertyChangeListener(event -> {
            loadImage(event.getItem().getImage());
        });

        String fio = "<h1><div align=\"center\">" +
                Func.fullFIO(((Employee) params.get("ITEM")).getLastName(),
                ((Employee) params.get("ITEM")).getFirstName(),
                ((Employee) params.get("ITEM")).getMiddleName())
                + "</div></h1>";
        labelFio.setValue(fio);

    }

    private void loadImage(FileDescriptor imageFile) {
        if (imageFile == null) {
            imagePhoto.setVisible(false);
            return;
        } else {
            imagePhoto.setVisible(true);
        }
        byte[] bytes = null;
        try {
            bytes = fileStorageService.loadFile(imageFile);
        } catch (FileStorageException e) {
            showNotification("Файл не загружен", NotificationType.HUMANIZED);
        }
        if (bytes != null) {
            imagePhoto.setSource(imageFile.getName(), new ByteArrayInputStream(bytes));
        } else {
            imagePhoto.setVisible(false);
        }
    }

    public void openFile(Entity item, String columnId) {
        //FileDescriptor file = ((Employee) item).getStorageFile()
        exportDisplay.show(((StorageFiles) item).getFile());
        //showNotification("Clicked by " + columnId, NotificationType.HUMANIZED);
    }

}