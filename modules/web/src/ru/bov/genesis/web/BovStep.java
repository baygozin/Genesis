package ru.bov.genesis.web;

import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.SubStep;

/**
 * Created by baygozin on 10.07.17.
 */
public class BovStep extends Step {

    private String directEmployee;

    public BovStep(String directEmployee) {
        this.directEmployee = directEmployee;
    }

    public BovStep(String caption, String directEmployee) {
        super(caption);
        this.directEmployee = directEmployee;
    }

    public BovStep() {
        super();
    }

    public BovStep(String caption, CaptionMode captionMode, String directEmployee) {
        super(caption, captionMode);
        this.directEmployee = directEmployee;
    }

    public BovStep(String caption, CaptionMode captionMode, String directEmployee, SubStep... subSteps) {
        super(caption, captionMode, subSteps);
        this.directEmployee = directEmployee;
    }

    public String getDirectEmployee() {
        return directEmployee;
    }

    public void setDirectEmployee(String directEmployee) {
        this.directEmployee = directEmployee;
    }

}
