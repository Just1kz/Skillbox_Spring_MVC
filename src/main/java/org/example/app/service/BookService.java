package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.app.repository.BookRepository;
import org.example.app.repository.ProjectRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final ProjectRepository bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
            bookRepo.store(book);
    }

    public boolean removeBookById(String bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public void removeBookByAuthor(String author) {
        bookRepo.removeAllBooksToAuthor(author);
    }

    public void removeBookByTitle(String title) {
        bookRepo.removeAllBooksToTitle(title);
    }

    public boolean removeBookBySize(Integer size) {
        bookRepo.removeAllBooksToSize(size);
        return true;
    }

    public List<Book> filterByAuthor(String author) {
        return bookRepo.filterByAuthor(author);
    }

    public List<Book> filterByTitle(String title) {
        return bookRepo.filterByTitle(title);
    }

    public List<Book> filterBySize(Integer size) {
        return bookRepo.filterBySize(size);
    }

    private void defaultInit() {
        logger.info("default INIT in book service bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in book service bean");
    }
}
