package isetb.mobileelite.login.Model;

public enum Role {
    EMPLOYEE,
    CONTROLLER,
    MANAGER;

    @Override
    public String toString() {
        // Capitalize the first letter of the role
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
