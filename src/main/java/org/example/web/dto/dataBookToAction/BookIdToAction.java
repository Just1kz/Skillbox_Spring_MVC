package org.example.web.dto.dataBookToAction;

import javax.validation.constraints.NotEmpty;

public class BookIdToAction {

    @NotEmpty
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
