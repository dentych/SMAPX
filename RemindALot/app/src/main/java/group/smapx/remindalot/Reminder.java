package group.smapx.remindalot;

import android.location.Address;

import java.io.Serializable;
import java.util.ArrayList;

public class Reminder implements Serializable {
    private String title;
    private String description;
    private long date;
    private ArrayList<Contact> contacts;
    private Address address;

    public Reminder(String title, String description, long date, ArrayList<Contact> contacts) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.contacts = contacts;
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
}
