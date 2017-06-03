package ru.bov.genesis.web.screens;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.services.ManningProperty;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Screenimportexcel extends AbstractWindow {

    @Inject
    private FileUploadField uploadField;

    @Inject
    private FileUploadingAPI fileUploadingAPI;

    @Inject
    private DataSupplier dataSupplier;

    @Inject
    private CollectionDatasource<Employee, UUID> employeesDs;

    @Inject
    private CollectionDatasource<ManningProperty, UUID> manningPropertiesDs;

    private Workbook workbook;

    @Override
    public void init(Map<String, Object> params) {

        uploadField.setShowFileName(false);

        uploadField.addFileUploadErrorListener(event ->
                showNotification("File upload error", NotificationType.HUMANIZED));

        uploadField.addFileUploadSucceedListener(event -> {
            FileDescriptor fd = uploadField.getFileDescriptor();
            testWorkbook(fd);
        });
    }

    private void testWorkbook(FileDescriptor fd) {
        if (!FilenameUtils.getExtension(uploadField.getFileName()).equals("xlsx")) {
            showNotification("Этот файл не является документом Excel...");
            return;
        }
        InputStream is = uploadField.getFileContent();
        try {
            workbook = WorkbookFactory.create(is);
        } catch (IOException e) {
            //e.printStackTrace();
            showNotification("Что-то пошло не так...");
        } catch (InvalidFormatException e) {
            showNotification("Это не документ Excel...");
            //e.printStackTrace();
        }

        int numSheet = workbook.getNumberOfSheets();
        Boolean find = false;
        for (int i = 0; i < numSheet; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String nameSheet = sheet.getSheetName();
            if (nameSheet.contains("Отчет ШСР")) {
                find = true;
                break;
            }
        }
        if (!find) {
            showNotification("Вы загрузили не тот файл Excel!!!!");
            return;
        }
        loadDatafromWorbook(workbook);
    }

    private void loadDatafromWorbook(Workbook workbook) {
        Object value = null;
        Employee employee = new Employee();
        ManningProperty property = new ManningProperty();
        Sheet sheet = workbook.getSheetAt(0);
        Integer rows = sheet.getPhysicalNumberOfRows();
        Collection<ManningProperty> items = manningPropertiesDs.getItems();
        for (int i = 2; i < rows; i++) {
            for (ManningProperty item : items) {
                if (item.getTypeField().equals("String")) {
                    value = sheet.getRow(i).getCell(CellReference.convertColStringToIndex(item.getColumnExcel())).getStringCellValue();
                } if (item.getTypeField().equals("Date")) {
                    value = sheet.getRow(i).getCell(CellReference.convertColStringToIndex(item.getColumnExcel())).getDateCellValue();
                }
                employee.setValue(item.getFieldClass(), value);
            }
            // Добавим данные в таблицу
        }
    }

}