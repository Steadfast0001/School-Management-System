package model;

import java.time.LocalDate;

public class Fee {
    private int id;
    private int studentId;
    private String studentName;
    private String feeType;
    private double amount;
    private double paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String status; // PENDING, PAID, OVERDUE

    public Fee() {}

    public Fee(int id, int studentId, String feeType, double amount, LocalDate dueDate) {
        this.id = id;
        this.studentId = studentId;
        this.feeType = feeType;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getFeeType() { return feeType; }
    public void setFeeType(String feeType) { this.feeType = feeType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getBalance() {
        return amount - paidAmount;
    }

    @Override
    public String toString() {
        return studentName + " - " + feeType + " ($" + amount + ") - " + status;
    }
}