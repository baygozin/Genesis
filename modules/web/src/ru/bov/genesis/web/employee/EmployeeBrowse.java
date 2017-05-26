package ru.bov.genesis.web.employee;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.bov.genesis.entity.mainentity.Employee;

import javax.inject.Inject;
import java.util.Map;

import static ru.bov.genesis.utils.GlobalTools.returnStatusColor;

public class EmployeeBrowse extends AbstractLookup {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    private GroupTable<Employee> employeesTable;

    @Inject
    private Table.Column dateWorkStart;

    @Override
    public void init(Map<String, Object> params) {
        employeesTable.setSortable(true);
        employeesTable.setColumnSortable("fullName", true);

        employeesTable.setStyleProvider(((entity, property) -> {
                return returnStatusColor(property, entity);
        }));
    }

}