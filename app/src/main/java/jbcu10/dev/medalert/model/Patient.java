package jbcu10.dev.medalert.model;

/**
 * Created by dev on 11/26/17.
 */

public class Patient extends Person {

    private boolean enabled;
    public Patient(String uuid, String firstName, String middleName, String lastName, String contactNumber, String email, String gender,boolean enabled,String imageUri) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.gender = gender;
        this.enabled = enabled;
        this.imageUri = imageUri;

    }

    public Patient(int id, String uuid, String firstName, String middleName, String lastName, String contactNumber, String email, String gender,String imageUri) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.gender = gender;
        this.imageUri = imageUri;

    }

    public Patient() {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
