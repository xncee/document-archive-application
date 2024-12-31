package model.entities;

public class Classification {
    private final String name; // name of the classification (PK)
    private String description; // Optional field

    // Constructor with required field only
    public Classification(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    // Constructor with both fields
    public Classification(String name, String description) {
        this(name); // Call the single-parameter constructor to validate 'name'
        this.description = description;
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

    @Override
    public String toString() {
        return "Classification{" +
                "name='" + name + '\'' +
                ", description='" + (description != null ? description : "N/A") + '\'' +
                '}';
    }
}
