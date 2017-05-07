package ru.bov.genesis.web.employee;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.bov.genesis.Func;
import ru.bov.genesis.entity.mainentity.Employee;

import javax.inject.Inject;
import java.util.Map;

public class EmployeeBrowse extends AbstractLookup {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    private GroupTable<Employee> employeesTable;

    @Inject
    private Table.Column dateWorkStart;

    @Override
    public void init(Map<String, Object> params) {
        employeesTable.setStyleProvider((entity, property) -> {
                if (entity.getBuilding() != null) {
                    return Func.returnStatusColor(true, property, entity.getDateWorkStart(), entity.getDateWorkEnd());
                } else {
                    return Func.returnStatusColor(false, property, entity.getDateWorkStart(), entity.getDateWorkEnd());
                }
        });
        
    }

    public Component generateFullNameCell(Employee entity) {
        Label field = (Label) componentsFactory.createComponent(Label.NAME);
        employeesTable.getColumn("fullName").setCaption("ФИО");
        field.setValue(Func.fullFIO(entity.getLastName(), entity.getFirstName(), entity.getMiddleName()));
        return field;
    }
}