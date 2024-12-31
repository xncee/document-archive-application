package model.entities;

public class Department {
    private final String name; // Name of the department (PK)
    private String description; // Optional description
    private String email; // Optional email address

    // Constructor with all fields
    public Department(String name, String description, String email) {
        this.name = name;
        this.description = description;
        this.email = email;
    }

    // Constructor without optional fields
    public Department(String name) {
        this.name = name;
        this.description = null;
        this.email = null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                (description != null ? ", description='" + description + '\'' : "") +
                (email != null ? ", email='" + email + '\'' : "") +
                '}';
    }
}
