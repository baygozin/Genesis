package ru.bov.genesis.web;

import com.haulmont.cuba.gui.components.VBoxLayout;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.tltv.gantt.Gantt;
import org.tltv.gantt.client.shared.Step;

import java.util.Collection;
import java.util.Date;

/**
 * Created by baygozin on 03.07.17.
 */
public class TableGanttLayout extends HorizontalSplitPanel {//implements GanttListener{

    private Gantt gantt;
    private Table ganttTable;

    private BeanItemContainer<BovStep> container;

    public void setContainer(Collection<BovStep> container) {
        this.container.removeAllItems();
        this.container.addAll(container);
    }

    public TableGanttLayout(Gantt gantt) {
        this.gantt = gantt;
        setSizeFull();
        container = new BeanItemContainer<BovStep>(BovStep.class);
        container.addAll((Collection) gantt.getSteps());
        setSizeFull();
        ganttTable = createTableForGantt();
        addComponent(ganttTable);
        addComponent(gantt);
        setSplitPosition(30, Unit.PERCENTAGE);
    }


    private Table createTableForGantt() {
        Table table = new Table("Сотрудники", container);
        table.setSortEnabled(false);
        table.setBuffered(false);
        table.setSelectable(true);
        table.setSizeFull();
        table.setStyleName("my_styled_table");
        table.setVisibleColumns( new Object[] {"description", "directEmployee"} );
        table.setColumnHeader("description", "");
        table.setColumnHeader("directEmployee", "");
        gantt.setVerticalScrollDelegateTarget(table);
        table.setColumnExpandRatio("description", 1);
        return table;
    }

//    @Override
//    public void stepModified(Step step) {
//        if (!ganttTable.containsId(step)) {
//            ((BeanItemContainer<Step>) ganttTable.getContainerDataSource()).addBean(step);
//        } else {
//            ganttTable.refreshRowCache();
//        }
//    }
//
//    @Override
//    public void stepDeleted(Step step) {
//        ganttTable.removeItem(step);
//    }
//
//    @Override
//    public void stepMoved(Step step, int newStepIndex, int oldStepIndex) {
//        if (oldStepIndex < newStepIndex) {
//            newStepIndex--;
//        }
//        ganttTable.removeItem(step);
//        ((BeanItemContainer<Step>) ganttTable.getContainerDataSource()).addItemAt(newStepIndex, step);
//    }
}
