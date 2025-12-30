package model;

import java.time.LocalDate;

public class Student {

    private int id;
    private String name;
    private String matricule;
    private String className;
    private LocalDate dateOfBirth;

    public Student() {}

    public Student(int id, String name, String matricule,
                   String className, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.matricule = matricule;
        this.className = className;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
