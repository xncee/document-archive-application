package data;

public enum Tables {
    USERS("users"),
    DOCUMENTS("documents"),
    DEPARTMENTS("departments");

    private final String tableName;

    Tables(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    // Override toString if you want to use the enum directly in a query
    @Override
    public String toString() {
        return tableName;
    }
}