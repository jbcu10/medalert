package jbcu10.dev.medalert.model;

/**
 * Created by dev on 12/16/17.
 */

public class User extends Person {
    public User() {
    }

    public User(String email, String firstName, String lastName) {
    super(email, firstName, lastName);
    }public User( int id,String email, String firstName, String lastName) {
    super(id,email, firstName, lastName);
    }
}
