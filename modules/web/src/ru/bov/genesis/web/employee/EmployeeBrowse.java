package ru.bov.genesis.web.employee;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.reports.gui.actions.RunReportAction;
import ru.bov.genesis.entity.mainentity.Employee;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static ru.bov.genesis.utils.GlobalTools.returnStatusCollorTools;
import static ru.bov.genesis.utils.GlobalTools.statusStr;

public class EmployeeBrowse extends AbstractLookup {

    @Inject
    private DataManager dm;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    private GroupTable<Employee> employeesTable;

    @Inject
    private CollectionDatasource<Employee, UUID> employeesDs;

    @Inject
    private Label labelCount;


    @Inject
    private CollectionDatasource<KeyValueEntity, Object> countDs;

    @Inject
    private Button reportButton;

    @Named("employeesTable.edit")
    private EditAction todosEmployeeEdit;

    @Inject
    private CheckBox checkBoxArhiv;

    @WindowParam
    private String directing;
    @Inject
    private CollectionDatasource<KeyValueEntity, Object> countArhivDs;

    @Override
    public void init(Map<String, Object> params) {

        if (directing != null) {
            employeesDs.setQuery("select e from genesis$Employee e where e.direction_work.nameDirecting = :param$directing"
                    + " and e.fieldStatus = 'Свободен'");
        }

        reportButton.setAction(new RunReportAction("report"));

        employeesTable.setSortable(true);
        employeesTable.setColumnSortable("fullName", true);

        employeesTable.setStyleProvider(((entity, property) -> {
            if (entity != null) {
                return returnStatusCollorTools(entity, property);
            } else return "";
        }));

        employeesDs.addItemChangeListener(employees -> {
            itemChange(employees.getItem());
        });

        employeesDs.addItemPropertyChangeListener(employees -> {
            itemChange(employees.getItem());
        });

        todosEmployeeEdit.setAfterCommitHandler(entity -> {
            employeesDs.refresh();
        });

        // Проверка статусов у сотрудников и изменение, если нужно
        refreshStatus();

        // показать архив
        checkBoxArhiv.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChanged(ValueChangeEvent e) {
                if ((Boolean)e.getValue()) {
                    employeesDs.setQuery("select e from genesis$Employee e where e.arhiv = 1");
                } else {
                    employeesDs.setQuery("select e from genesis$Employee e where (e.arhiv is null) or (e.arhiv = 0)");
                }
                employeesDs.refresh();
                refreshStatus();
            }
        });

    }

    private void itemChange(Employee employee) {
        if (employee == null) return;
        employee.setFieldStatus(statusStr(employee));
        //employeesDs.commit();
        refreshStatus();
    }

    // Обновление статуса в labelCount - сколько и кого где находиться
    public void refreshStatus() {
        String valueCount = "";
        Collection<KeyValueEntity> valueEntities;
        if (checkBoxArhiv.getValue()) {
            countArhivDs.refresh();
            valueEntities = countArhivDs.getItems();
        } else {
            countDs.refresh();
            valueEntities = countDs.getItems();
        }
        for (KeyValueEntity valueEntity : valueEntities) {
            String statusField = valueEntity.getValue("statusField").toString().toUpperCase();
            Long countField = valueEntity.getValue("countField");
            valueCount += statusField + " " + String.valueOf(countField) + " чел.; ";
        }
        labelCount.setValue(valueCount);
    }

    public Component generateImage_photoCell(Employee entity) {
        Label photoEmployee = componentsFactory.createComponent(Label.class);
        if (entity.getImage_photo() == null) {
            photoEmployee.setValue("Нет");
        } else {
            photoEmployee.setValue("Есть");
        }
		return photoEmployee;
    }

    public Component generateImage_signCell(Employee entity) {
        Label photoEmployee = componentsFactory.createComponent(Label.class);
        if (entity.getImage_sign() == null) {
            photoEmployee.setValue("Нет");
        } else {
            photoEmployee.setValue("Есть");
        }
        return photoEmployee;
    }

    public void onCreateBtnClick() {

    }
}