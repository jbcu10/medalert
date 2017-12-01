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
    private List<String> time;
    private Patient patient;

    public Reminder() {

    }

    public Reminder(String uuid, String description, List<Medicine> medicineList, List<String> time, Patient patient) {
        this.uuid = uuid;
        this.description = description;
        this.medicineList = medicineList;
        this.time = time;
        this.patient = patient;
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

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
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

    @Override
    public String toString() {
        return "Reminder{" +
                "uuid='" + uuid + '\'' +
                ", description='" + description + '\'' +
                ", medicineList=" + medicineList +
                ", time=" + time +
                ", patient=" + patient +
                '}';
    }
}
