package org.example.app.repository;

import org.example.web.dto.Book;

import java.util.List;

public interface FilterBooksInRepository {

    List<Book> filterByAuthor(String author);

    List<Book> filterByTitle(String title);

    List<Book> filterBySize(Integer size);
}
