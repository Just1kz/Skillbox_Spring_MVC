package org.example.app.repository;

import java.util.List;

public interface ProjectRepository<Book> extends FilterBooksInRepository {

    List<Book> retreiveAll();

    void store(Book book);

    boolean removeItemById(Integer bookIdToRemove);

    void removeAllBooksToAuthor(String author);

    void removeAllBooksToTitle(String title);

    boolean removeAllBooksToSize(Integer size);
}
