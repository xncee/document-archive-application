package services;

import java.time.LocalDate;

public class FilterService {
    private static FilterService instance;
    private String status;
    private String department;
    private String classification;
    private LocalDate startDate;
    private LocalDate endDate;

    private FilterService() {

    }
    public static FilterService getInstance() {
        if (instance == null) {
            instance = new FilterService();
        }

        return instance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDepartment() {
        return department;
    }

    public String getClassification() {
        return classification;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void reset() {
        status = null;
        department = null;
        classification = null;
        startDate = LocalDate.now().minusMonths(1);
        endDate = LocalDate.now();
    }
}