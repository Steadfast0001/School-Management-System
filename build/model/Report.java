package model;

import java.time.LocalDate;

public class Report {
    private int id;
    private String reportType;
    private String title;
    private String description;
    private LocalDate generatedDate;
    private String generatedBy;
    private String data; // JSON or serialized data

    public Report() {}

    public Report(int id, String reportType, String title, String description, LocalDate generatedDate, String generatedBy) {
        this.id = id;
        this.reportType = reportType;
        this.title = title;
        this.description = description;
        this.generatedDate = generatedDate;
        this.generatedBy = generatedBy;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDate generatedDate) { this.generatedDate = generatedDate; }

    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    @Override
    public String toString() {
        return title + " (" + reportType + ") - " + generatedDate;
    }
}