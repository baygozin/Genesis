package ru.bov.genesis.web.myphoto;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import ru.bov.genesis.entity.test.MyPhoto;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class MyPhotoEdit extends AbstractEditor<MyPhoto> {

    @Inject
    private Embedded imagePhoto;

    @Inject
    private Datasource<MyPhoto> myPhotoDs;

    @Inject
    private FileStorageService fileStorageService;

    @Named("fieldGroup.image")
    private FileUploadField uploadField;

    @Inject
    private Frame windowActions;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        FileDescriptor imageFile = ((MyPhoto)(MyPhoto) params.get("ITEM")).getImage();
        loadImage(imageFile);

        myPhotoDs.addItemPropertyChangeListener(event -> {
            loadImage(event.getItem().getImage());
        });
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

}