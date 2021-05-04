package org.example.app.service;

import java.util.List;

public interface ProjectRepository<Book> {
    List<Book> retreiveAll();

    void store(Book book);

    boolean removeItemById(Integer bookIdToRemove);
}
