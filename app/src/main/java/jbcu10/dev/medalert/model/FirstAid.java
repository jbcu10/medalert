package jbcu10.dev.medalert.model;

import java.util.List;

/**
 * Created by dev on 10/14/17.
 */

public class FirstAid {
    private int id;
    private String uuid;
    private String name;
    private String description;
    private String link;
    private List<Instructions> instructionsList;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Instructions> getInstructionsList() {
        return instructionsList;
    }

    public void setInstructionsList(List<Instructions> instructionsList) {
        this.instructionsList = instructionsList;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
