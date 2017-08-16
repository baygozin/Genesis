package ru.bov.genesis.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;
import ru.bov.genesis.entity.mainentity.Employee;
import com.haulmont.cuba.core.listener.BeforeDetachEntityListener;
import com.haulmont.cuba.core.listener.BeforeAttachEntityListener;

import static ru.bov.genesis.utils.GlobalTools.statusStr;

/**
 * Created by Administrator on 03.05.2017.
 */

@Component("genesis_EmployeeItemListener")
public class EmployeeListener implements
        BeforeInsertEntityListener<Employee>,
        BeforeUpdateEntityListener<Employee>,
        BeforeDetachEntityListener<Employee>,
        BeforeAttachEntityListener<Employee> {

    @Override
    public void onBeforeInsert(Employee entity, EntityManager entityManager) {
        entity.setFieldStatus(statusStr(entity));
        entityManager.persist(entity);
    }

    @Override
    public void onBeforeUpdate(Employee entity, EntityManager entityManager) {
        entity.setFieldStatus(statusStr(entity));
        entityManager.persist(entity);
    }


    @Override
    public void onBeforeDetach(Employee entity, EntityManager entityManager) {
        entityManager.persist(entity);
    }


    @Override
    public void onBeforeAttach(Employee entity) {
    }


}

