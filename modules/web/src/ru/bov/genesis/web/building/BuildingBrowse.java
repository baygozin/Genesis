package ru.bov.genesis.web.building;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import ru.bov.genesis.entity.mainentity.Building;

import javax.inject.Inject;

public class BuildingBrowse extends AbstractLookup {

    @Inject
    private ComponentsFactory componentsFactory;

    public Component generateNumberEmployeeCell(Building entity) {
        Label numberEmployee = componentsFactory.createComponent(Label.class);
        numberEmployee.setValue(String.valueOf(entity.getEmployeeCk().size()));
        return numberEmployee;
    }
}