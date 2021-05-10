package org.example.web.dto.dataBookToAction;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class BookSizeToAction {

    @Digits(integer = 4, fraction = 0)
    @NotNull
    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
