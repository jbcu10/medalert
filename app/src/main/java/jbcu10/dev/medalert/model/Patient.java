package jbcu10.dev.medalert.model;

/**
 * Created by dev on 11/26/17.
 */

public class Patient extends Person {

    public Patient(String uuid, String firstName, String middleName, String lastName, String contactNumber, String email, String gender) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.gender = gender;
    }

    public Patient(int id, String uuid, String firstName, String middleName, String lastName, String contactNumber, String email, String gender) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.gender = gender;
    }

    public Patient() {

    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
