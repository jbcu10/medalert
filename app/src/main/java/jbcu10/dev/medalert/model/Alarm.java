package jbcu10.dev.medalert.model;

/**
 * Created by dev on 10/9/17.
 */

public class Alarm {

    private int id;
    private String time;
    private int medicineId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
}
