package com.example.snapmind;

public class firebasemodel {
    public String title;
    public String content;

    public firebasemodel() {
    }

    public firebasemodel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // You might also want to add setter methods if needed
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}