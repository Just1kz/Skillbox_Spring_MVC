package org.example.web.dto.dataBookToAction;

import javax.validation.constraints.NotEmpty;

public class BookAuthorToAction {

    @NotEmpty
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
