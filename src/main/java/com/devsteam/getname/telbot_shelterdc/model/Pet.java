package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "name")
    private String name;

    @Column(name = "breed")
    private String breed;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCo")
    private PetOwner petOwner;

    @Column(name = "kind")
    @Enumerated(EnumType.STRING)
    private Kind kind;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Pet() {
    }

    public Pet(long id, int birthYear, String name, String breed, String description, Color color,
               Status status, PetOwner petOwner, Kind kind, Gender gender) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.color = color;
        this.status = status;
        this.petOwner = petOwner;
        this.kind = kind;
        this.gender = gender;
    }
    public Pet(long id, int birthYear, String name, String breed, String description, Color color, Status status, Kind kind, Gender gender) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.color = color;
        this.status = status;
        this.kind = kind;
        this.gender = gender;
    }

    public Pet(int birthYear, String name, String breed, String description, Color color, Status status, Kind kind, Gender gender) {
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.color = color;
        this.status = status;
        this.kind = kind;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public int getBirthYear() {
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

    public void setBirthYear(int birthYear) {
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

    public Color getColor() {
        return color;
    }

    public Status getStatus() {
        return status;
    }


    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
