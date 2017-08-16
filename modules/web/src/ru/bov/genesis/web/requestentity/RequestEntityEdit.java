package ru.bov.genesis.web.requestentity;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import ru.bov.genesis.entity.RequestEntity;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Map;

public class RequestEntityEdit extends AbstractEditor<RequestEntity> {

    @Inject
    private DataManager dm;
    @Inject
    private DataSource requestEntityDs;

    @Override
    public void init(Map<String, Object> params) {
        RequestEntity entity = dm.reload(((RequestEntity) params.get("ITEM")), "requestEntity-view");
        if (entity.getSpecialist() != null) {
            dm.reload(entity.getSpecialist(), "directing-view");
        }
    }
}

