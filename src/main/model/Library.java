package model;

public class Library {
    private int id;
    private String bookTitle;
    private String author;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private String location;

    public Library() {}

    public Library(int id, String bookTitle, String author, String isbn, String category, int totalCopies, int availableCopies, String location) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return bookTitle + " by " + author + " (" + availableCopies + "/" + totalCopies + " available)";
    }
}