package com.khanhnq.libraryapp.component;

public class BookTitleList {
    private String id, bookName, author, publisher, category;

    public BookTitleList(String id, String bookName, String author, String category, String publisher){
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
    }

    public String getId() { return id;}
    public String getBookName() { return bookName; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getCategory() { return category; }
}
