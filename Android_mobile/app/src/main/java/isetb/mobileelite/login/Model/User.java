package isetb.mobileelite.login.Model;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Long id;
    private String first_name;
    private String last_name;
    private String email;

    private Date birthDate;

    private Role role;
    private String password;
    private boolean isActive;

    // to avoid adding this attribute to the database
    private String confirm_password;

    public User() {
    }

    // Entity constructor
    public User(String FN, String LN, String mail, Role role, String pwd, Boolean Active) {
        this.first_name = FN;
        this.last_name = LN;
        this.email = mail;
        this.role = role;
        this.password = pwd;

        this.birthDate = new Date(); // Obtient la date actuelle
        this.isActive = false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public Long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String getUsername() {
        return email; // Treat email as the username for authentication
    }
}