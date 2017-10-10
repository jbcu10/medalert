package jbcu10.dev.medalert.model;

/**
 * Created by dev on 10/10/17.
 */

public class Relative extends Person {
    private String relationship;
    public Relative( String uuid, String firstName, String middleName, String lastName, String contactNumber, String email,String relationship) {

        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.relationship = relationship;
    }

    public Relative() {

    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
