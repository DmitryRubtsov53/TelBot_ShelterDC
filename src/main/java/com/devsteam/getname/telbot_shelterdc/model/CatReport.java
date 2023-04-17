package com.devsteam.getname.telbot_shelterdc.model;


import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")

public class CatReport {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @Column(nullable = false)
    private CatOwner CatOwner;
    @Column(nullable = false)
    private File photo;
    @Column(nullable = false)
    private String meals;
    @Column(nullable = false)
    private String wellBeingAndAdaptation;
    @Column(nullable = false)
    private String behaviorChanges;
    @Column(nullable = false)
    private LocalDateTime reportDateTime;

    @Column(nullable = false)
    private boolean reportIsComplete;
    @Column(nullable = false)
    private boolean ownerIsOnTrialPeriod;

    public long getId() {
        return id;
    }

    public File getPhoto() {
        return photo;
    }

    public String getMeals() {
        return meals;
    }

    public String getWellBeingAndAdaptation() {
        return wellBeingAndAdaptation;
    }

    public String getBehaviorChanges() {
        return behaviorChanges;
    }

    public LocalDateTime getReportDateTime() {
        return reportDateTime;
    }

    public CatOwner getCatOwner() {
        return CatOwner;
    }

    public boolean isReportIsComplete() {
        return reportIsComplete;
    }

    public boolean isOwnerIsOnTrialPeriod() {
        return ownerIsOnTrialPeriod;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCatOwner(CatOwner catOwner) {
        CatOwner = catOwner;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public void setMeals(String meals) {
        this.meals = meals;
    }

    public void setWellBeingAndAdaptation(String wellBeingAndAdaptation) {
        this.wellBeingAndAdaptation = wellBeingAndAdaptation;
    }

    public void setBehaviorChanges(String behaviorChanges) {
        this.behaviorChanges = behaviorChanges;
    }

    public void setReportDateTime(LocalDateTime reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public void setReportIsComplete(boolean reportIsComplete) {
        this.reportIsComplete = reportIsComplete;
    }

    public void setOwnerIsOnTrialPeriod(boolean ownerIsOnTrialPeriod) {
        this.ownerIsOnTrialPeriod = ownerIsOnTrialPeriod;
    }
}
