package jbcu10.dev.medalert.model;

/**
 * Created by dev on 12/16/17.
 */

public class Token {
    private int id;
    private String token;

    public Token() {}

    public Token(  String token) {
         this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
