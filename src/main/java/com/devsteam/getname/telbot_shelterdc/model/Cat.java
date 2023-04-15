package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Cat {

    @Id
    @GeneratedValue
    private Long id;

    private String birthYear;

    private String name;

    private String breed;

    private String description;

    public Cat() {
    }

    public Cat(long id, String birthYear, String name, String breed, String description) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

}