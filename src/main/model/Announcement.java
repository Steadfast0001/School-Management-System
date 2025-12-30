package model;

import java.time.LocalDate;

public class Announcement {
    private int id;
    private String title;
    private String content;
    private String author;
    private LocalDate publishDate;
    private LocalDate expiryDate;
    private String targetAudience; // ALL, STUDENTS, TEACHERS, ADMIN
    private boolean isActive;

    public Announcement() {}

    public Announcement(int id, String title, String content, String author, LocalDate publishDate, LocalDate expiryDate, String targetAudience) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.publishDate = publishDate;
        this.expiryDate = expiryDate;
        this.targetAudience = targetAudience;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return title + " - " + author + " (" + publishDate + ")";
    }
}