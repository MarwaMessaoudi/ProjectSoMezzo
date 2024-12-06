package pi.pperformance.elite.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

@Table(name = "`appel`")
@Entity
public class Appel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long call_id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String date;  // Changed to String

    private int duration;  // in seconds or minutes
    private String description;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    // Getters and Setters
    public Long getId() {
        return call_id;
    }

    public void setId(Long id) {
        this.call_id = call_id;
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
