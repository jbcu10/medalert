package jbcu10.dev.medalert.model;

import java.util.List;

/**
 * Created by dev on 10/9/17.
 */

public class Reminder {

    private int id;
    private String uuid;
    private String description;
    private List<Medicine> medicineList;
    private List<Time> time;
    private Patient patient;
    private boolean turnOn;

    public Reminder() {

    }

    public Reminder(String uuid, String description, List<Medicine> medicineList, List<Time> time, Patient patient,boolean turnOn) {
        this.uuid = uuid;
        this.description = description;
        this.medicineList = medicineList;
        this.time = time;
        this.patient = patient;
        this.turnOn = turnOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Medicine> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    public List<Time> getTime() {
        return time;
    }

    public void setTime(List<Time> time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean isTurnOn() {
        return turnOn;
    }

    public void setTurnOn(boolean turnOn) {
        this.turnOn = turnOn;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "uuid='" + uuid + '\'' +
                ", description='" + description + '\'' +
                ", medicineList=" + medicineList +
                ", patient=" + patient +
                '}';
    }
}
