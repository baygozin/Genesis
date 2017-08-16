package ru.bov.genesis.web;

import org.tltv.gantt.client.shared.Step;

/**
 * Created by baygozin on 03.07.17.
 */
public interface GanttListener {
    void stepModified(Step step);

    void stepDeleted(Step step);

    void stepMoved(Step step, int newStepIndex, int oldStepIndex);
}
