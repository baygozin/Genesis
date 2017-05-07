package ru.bov.genesis.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;
import ru.bov.genesis.entity.mainentity.Employee;

import java.util.Date;

/**
 * Created by Administrator on 03.05.2017.
 */

@Component("EmployeeItemListener")
public class EmployeeListener implements
        BeforeInsertEntityListener<Employee>,
        BeforeUpdateEntityListener<Employee> {

    @Override
    public void onBeforeInsert(Employee entity, EntityManager entityManager) {
        entity.setFieldStatus(statusStr(entity, entity.getDateWorkStart(), entity.getDateWorkEnd()));
        entityManager.persist(entity);
    }

    @Override
    public void onBeforeUpdate(Employee entity, EntityManager entityManager) {
        entity.setFieldStatus(statusStr(entity, entity.getDateWorkStart(), entity.getDateWorkEnd()));
        entityManager.persist(entity);
    }

    private String statusStr(Employee entity, Date start, Date end) {
        if (entity.getBuilding() != null) {
            if (isWorkMan(start, end)) {
                return "вахта";
            } else {
                return "межвахта";
            }
        } else {
            System.out.println("==== Свободен ====");
            return "свободен";
        }
    }

    public boolean isWorkMan(Date start, Date end) {
        Date current = new Date();
        if (start != null && end != null) {
            if (current.after(start) && current.before(end)) {
                return true;
            } else if (current.after(end) && current.before(start)) {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }
}

