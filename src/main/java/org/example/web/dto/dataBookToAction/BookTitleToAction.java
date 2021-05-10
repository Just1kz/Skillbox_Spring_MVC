package org.example.web.dto.dataBookToAction;

import javax.validation.constraints.NotEmpty;

public class BookTitleToAction {

    @NotEmpty
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
