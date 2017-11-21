package jbcu10.dev.medalert.model;

/**
 * Created by dev on 10/1/17.
 */

public class Doctor  extends Person {
    private String specialization;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
