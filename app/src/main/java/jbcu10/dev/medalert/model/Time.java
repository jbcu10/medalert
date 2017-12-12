package jbcu10.dev.medalert.model;

/**
 * Created by dev on 12/12/17.
 */

public class Time {
    private String uuid;
    private String time;
    private int intentId;

    public Time(String uuid, String time, int intentId) {
        this.uuid = uuid;
        this.time = time;
        this.intentId = intentId;
    }

    public Time() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIntentId() {
        return intentId;
    }

    public void setIntentId(int intentId) {
        this.intentId = intentId;
    }

    @Override
    public String toString() {
        return "Time{" +
                "uuid='" + uuid + '\'' +
                ", time='" + time + '\'' +
                ", intentId=" + intentId +
                '}';
    }
}
