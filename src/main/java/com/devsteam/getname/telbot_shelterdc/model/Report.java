package com.devsteam.getname.telbot_shelterdc.model;

import java.io.File;

public class Report {
    private final File photo;
    private final String meals;
    private final String wellBeingAndAdaptation;
    private final String behaviorChanges;

    public Report(File photo, String meals, String wellBeingAndAdaptation, String behaviorChanges) {
        this.photo = photo;
        this.meals = meals;
        this.wellBeingAndAdaptation = wellBeingAndAdaptation;
        this.behaviorChanges = behaviorChanges;
    }
}
