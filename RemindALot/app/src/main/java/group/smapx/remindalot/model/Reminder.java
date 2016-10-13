package group.smapx.remindalot.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Reminder implements Serializable {
    private long id;
    private String title;

    public boolean isSmsSent() {
        return smsSent;
    }

    public void setSmsSent(boolean smsSent) {
        this.smsSent = smsSent;
    }

    private boolean smsSent;
    private String description;
    private long date;
    private ArrayList<Contact> contacts;
    private LocationData locationData;

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    private boolean notified;

    public String getMeansOfTransportation() {
        return meansOfTransportation;
    }

    public void setMeansOfTransportation(String meansOfTransportation) {
        this.meansOfTransportation = meansOfTransportation;
    }

    private String meansOfTransportation;
    private int requestCode;

    public Reminder() {
    }

    public Reminder(String title, String description, long date, ArrayList<Contact> contacts) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.contacts = contacts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
    }
}
