package model;

public class Department {
    private int id;
    private String departmentCode;
    private String departmentName;
    private String description;
    private String headOfDepartment;

    public Department() {}

    public Department(int id, String departmentCode, String departmentName, String description, String headOfDepartment) {
        this.id = id;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.description = description;
        this.headOfDepartment = headOfDepartment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHeadOfDepartment() { return headOfDepartment; }
    public void setHeadOfDepartment(String headOfDepartment) { this.headOfDepartment = headOfDepartment; }

    @Override
    public String toString() {
        return departmentCode + " - " + departmentName;
    }
}