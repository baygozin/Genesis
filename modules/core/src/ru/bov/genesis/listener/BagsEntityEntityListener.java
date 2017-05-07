package ru.bov.genesis.listener;

import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.EntityManager;
import ru.bov.genesis.entity.services.BagsEntity;

import java.time.LocalDateTime;
import java.util.Calendar;

@Component("genesis_BagsEntityEntityListener")
public class BagsEntityEntityListener implements BeforeInsertEntityListener<BagsEntity> {


    @Override
    public void onBeforeInsert(BagsEntity entity, EntityManager entityManager) {
        Calendar cal = Calendar.getInstance();
        entity.setBugDateTime(cal.getTime());
    }


}