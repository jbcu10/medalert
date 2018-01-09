package jbcu10.dev.medalert.model;

/**
 * Created by dev on 10/10/17.
 */

public class Relative extends Person {
    private String relationship;
    private boolean notify;

    public Relative(String uuid, String firstName, String middleName, String lastName, String contactNumber, String email, String relationship,boolean notify,String imageUri) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.relationship = relationship;
        this.notify = notify;
        this.imageUri = imageUri;

    }

    public Relative(int id, String uuid, String firstName, String middleName, String lastName, String contactNumber, String email, String relationship,boolean notify, String imageUri) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.relationship = relationship;
        this.notify = notify;
        this.imageUri = imageUri;

    }

    public Relative() {

    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
