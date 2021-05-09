package org.example.app.service;

import org.example.app.repository.BookRepository;
import org.example.web.dto.Book;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BookRepositoryTest {

    @Ignore
    @Test
    public void filterByAuthor() {
        BookRepository repository = new BookRepository();
        Book book1 = new Book();
        book1.setAuthor("Anton");
        Book book2 = new Book();
        book2.setAuthor("Anna");
        repository.store(book1);
        repository.store(book2);
        List<Book> rsl = repository.filterByAuthor("anton");
        assertThat(rsl.size(), is(1));
    }
}