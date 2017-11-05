package jbcu10.dev.medalert.model;

/**
 * Created by dev on 10/14/17.
 */

public class Instructions {
    private int id;
    private String uuid;
    private String instruction;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
