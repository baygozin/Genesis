package ru.bov.genesis.web.screens;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.services.Directing;
import ru.bov.genesis.entity.services.ManningProperty;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Screenimportexcel extends AbstractWindow {

    @Inject
    private FileUploadField uploadField;

    @Inject
    private CollectionDatasource<Employee, UUID> employeesDs;

    @Inject
    private CollectionDatasource<Directing, UUID> directingsDs;

    @Inject
    private CollectionDatasource<ManningProperty, UUID> manningPropertiesDs;

    @Inject
    Metadata metadata;

    private Workbook workbook;

    private String[] russianMonat = {
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря"
    };


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
            showNotification("Что-то пошло не так...");
        } catch (InvalidFormatException e) {
            showNotification("Это не документ Excel...");
        }

        try {
            loadDatafromWorbook(workbook);
            workbook.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage() + ". Невозможно закрыть книгу...");
        }

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadDatafromWorbook(Workbook workbook) {
        employeesDs.refresh();
        Boolean find = false;
        int intRow = 0;
        int count = workbook.getNumberOfSheets();
        for (int i = 0; i < count; i++) {
            String nameSheet = workbook.getSheetName(i);
            if (nameSheet.toUpperCase().contains("Отчет ШСР".toUpperCase())) {
                count = i;
                find = true;
                break;
            }
        }
        if (!find) {
            showNotification("Отчет ШСР не найден в книге");
            return;
        }

        Sheet sheet = workbook.getSheetAt(count);
        Integer rows = sheet.getPhysicalNumberOfRows();
        Collection<ManningProperty> items = manningPropertiesDs.getItems();
        Collection<Employee> collection = employeesDs.getItems();
        for (int k = 0; k < rows + 10; k++) {
            Row row = sheet.getRow(k);
            if (row == null) {
                intRow = k;
                break;
            }
        }
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i < intRow; i++) {
            Employee employee = new Employee();
            for (ManningProperty item : items) {
                String valueString;
                Row row = sheet.getRow(i);
                int column = Integer.parseInt(item.getColumnExcel()) - 1;
                Cell cell = row.getCell(column);
                if (item.getTypeField().equals("String")) {
                    valueString = getCellString(cell);
                    if (valueString.length() > 255) valueString = valueString.substring(0, 254);
                    employee.setValue(item.getFieldClass(), valueString, true);
                } else if (item.getTypeField().equals("Date")) {
                    employee.setValue(item.getFieldClass(),  getCellData(cell), true);
                } else if (item.getTypeField().equals("FIO")) {
                    List<String> list = Arrays.asList(cell.getStringCellValue().split(" "));
                    employee.setLastName(list.get(0));
                    employee.setFirstName(list.get(1));
                    employee.setMiddleName(list.get(2));
                } else if (item.getTypeField().equals("RegDate")) {
                    employee.setDateAddresRegistration(convertRegDate(cell));
                } else if (item.getTypeField().equals("Association") && item.getFieldClass().equals("direction_work")) {
                    directingsDs.refresh();
                    String direction = getCellString(cell);
                    Collection<Directing> directingList = directingsDs.getItems();
                    Boolean findString = false;
                    for (Directing directing : directingList) {
                        if (direction.equals(directing.getNameDirecting())) {
                            findString = true;
                            employee.setDirection_work(directing);
                            break;
                        }
                    }
                    if (!findString) {
                        Directing directing = metadata.create(Directing.class);
                        directing.setNameDirecting(getCellString(cell));
                        directingsDs.addItem(directing);
                        directingsDs.commit();
                        employee.setDirection_work(directing);
                    }
                }
            }

            employeeList.add(employee);
            // Найдем такого-же гражданина в генезисе
            find = false;
            int x = 0;
            for (Employee employee_1 : collection) {
                if ((employee_1.getLastName() != null && employee_1.getLastName().equals(employee.getLastName())) &&
                        (employee_1.getFirstName() != null && employee_1.getFirstName().equals(employee.getFirstName())) &&
                        (employee_1.getMiddleName() != null && employee_1.getMiddleName().equals(employee.getMiddleName()))) {
                    employeesDs.modifyItem(fillEmployee(employee, employee_1));
                    find = true;
                    break;
                }
                x++;
            }
            // Не нашли - добавим
            if (!find) {
                employeesDs.addItem(employee);
            }
            employeesDs.commit();
        }
        // А сейчас проверим, не уволен-ли какой из сотрудников. Если да, то в архив
        for (Employee empl : new ArrayList<>(employeesDs.getItems())) {
            Boolean fff = false;
            for(Employee empl2 : employeeList) {
                if ((empl.getLastName() != null && empl.getLastName().equals(empl2.getLastName())) &&
                        (empl.getFirstName() != null && empl.getFirstName().equals(empl2.getFirstName())) &&
                        (empl.getMiddleName() != null && empl.getMiddleName().equals(empl2.getMiddleName()))) {
                    fff = true;
                    break;
                }
            }
            if (!fff) {
                empl.setArhiv(Boolean.TRUE);
                employeesDs.modifyItem(empl);
            } else {
                empl.setArhiv(Boolean.FALSE);
                employeesDs.modifyItem(empl);
            }
        }
        employeesDs.commit();
    }

    private Date convertRegDate(Cell cell) {
        String d;
        Date date = null;
        if (cell != null) {
            d = cell.getStringCellValue();
            d = d.substring(d.indexOf(":") + 2);
            if (d.equals("...")) {
                return null;
            }
            DateFormatSymbols russSymbol = new DateFormatSymbols();
            russSymbol.setMonths(russianMonat);
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", russSymbol);;
            try {
                date = format.parse(d);
            } catch (ParseException e) {
                //e.printStackTrace();
                return null;
            }
            return date;

        }
        return null;
    }

    private Employee fillEmployee(Employee src, Employee dst) {
        Field[] fieldsS = src.getClass().getDeclaredFields();
        Field[] fieldsD = dst.getClass().getDeclaredFields();
        for (int j = 2; j < fieldsD.length; j++) {
            fieldsS[j].setAccessible(true);
            fieldsD[j].setAccessible(true);
            try {
                Object obj = fieldsS[j].get(src);
                if (obj != null) {
                    fieldsD[j].set(dst, obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dst;
    }

    private String getCellString(Cell cell) {
        DataFormatter df = new DataFormatter();
        if (cell != null) {
            CellType cellType = cell.getCellTypeEnum();
            if (cellType == CellType.BOOLEAN) {
                return "Boolean";
            } else if (cellType == CellType.NUMERIC) {
                double num = cell.getNumericCellValue();
                String str = new DecimalFormat("#").format(num);
                return str;
            } else if (cellType == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cellType == CellType.BLANK) {
                return "";
            } else if (cellType == CellType.ERROR) {
                return "ERROR";
            } else if (cellType == CellType._NONE) {
                return "_NONE";
            } else if (cellType == CellType.FORMULA) {
                return "FORMULA";
            }
        }
        return "";
    }

    private Date getCellData(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    return null;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    } else break;
                case Cell.CELL_TYPE_STRING:
                    return null;
                case Cell.CELL_TYPE_BLANK:
                    return null;
                case Cell.CELL_TYPE_ERROR:
                    return null;
            }
        }
        return null;
    }

}