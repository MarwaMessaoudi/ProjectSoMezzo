package isetb.mobileelite.login.Model;

import java.io.Serializable;

public class Appel implements Serializable {

    private Long id;
    private String date;  // Use String to handle the date
    private int duration;
    private String description;
    private User user;


    public Appel(long id, String date, int duration, String description) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.description = description;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
