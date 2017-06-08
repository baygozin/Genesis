package ru.bov.genesis.web.employee;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CustomValueGroupDatasource;
import com.haulmont.cuba.gui.data.impl.ValueCollectionDatasourceImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.bov.genesis.entity.mainentity.Employee;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.bov.genesis.utils.GlobalTools.returnStatusColor;

public class EmployeeBrowse extends AbstractLookup {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    private GroupTable<Employee> employeesTable;

    @Inject
    private Datasource<Employee> employeesDs;

    @Inject
    private Table.Column dateWorkStart;

    @Inject
    private Label labelCount;


    @Inject
    private CollectionDatasource<KeyValueEntity, Object> countDs;

    @Inject
    private Map<String, Object> status;

    @Override
    public void init(Map<String, Object> params) {

        employeesTable.setSortable(true);
        employeesTable.setColumnSortable("fullName", true);

        employeesTable.setStyleProvider(((entity, property) -> {
                return returnStatusColor(property, entity);
        }));

        employeesDs.addItemChangeListener(new Datasource.ItemChangeListener<Employee>() {
            @Override
            public void itemChanged(Datasource.ItemChangeEvent<Employee> e) {
                refreshStatus();
                getExpireDate(e);
                employeesTable.setFocusable(true);
            }
        });
        refreshStatus();
    }

    public void getExpireDate(Datasource.ItemChangeEvent<Employee> e) {
        Employee employee = e.getItem();
        
    }

    public void refreshStatus() {
        String valueCount = "";
        countDs.refresh();
        Collection<KeyValueEntity> valueEntities = countDs.getItems();
        for (KeyValueEntity valueEntity : valueEntities) {
            String statusField = valueEntity.getValue("statusField");
            Long countField = valueEntity.getValue("countField");
            valueCount += statusField + " " + String.valueOf(countField) + " чел.; ";
        }
        labelCount.setValue(valueCount);
    }

}