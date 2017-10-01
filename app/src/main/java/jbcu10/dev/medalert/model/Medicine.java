package jbcu10.dev.medalert.model;

import java.util.Date;

/**
 * Created by dev on 10/1/17.
 */

public class Medicine {

    private int id;
    private String uuid;
    private String name;
    private String genericName;
    private String diagnosis;
    private String description;
    private long expiration;
    private int total ;
    private Doctor doctor;

    public Medicine( String uuid, String name, String genericName, String diagnosis, String description, long expiration, int total, Doctor doctor) {
        this.uuid = uuid;
        this.name = name;
        this.genericName = genericName;
        this.diagnosis = diagnosis;
        this.description = description;
        this.expiration = expiration;
        this.total = total;
        this.doctor = doctor;
    }

    public Medicine() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
