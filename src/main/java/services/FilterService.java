package services;

import models.FilterState;

import java.util.List;
import java.util.ArrayList;

public class FilterService {
    public List<String> getDepartments() {
        List<String> departments = new ArrayList<>();
        departments.add("Engineering");
        departments.add("Marketing");
        departments.add("Sales");
        departments.add("Human Resources");
        departments.add("Finance");
        departments.add("Operations");
        return departments;
    }

    public List<String> getClassifications() {
        List<String> classifications = new ArrayList<>();
        classifications.add("High Priority");
        classifications.add("Medium Priority");
        classifications.add("Low Priority");
        classifications.add("Urgent");
        classifications.add("Routine");
        return classifications;
    }

    public void applyFilters(FilterState filterState) {
        // Implementation would connect to backend service
        // to apply filters and update the UI
    }
}