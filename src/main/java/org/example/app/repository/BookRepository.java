package org.example.app.repository;

import org.apache.log4j.Logger;
import org.example.app.service.IdProvider;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {
    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private ApplicationContext context;

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(org.example.web.dto.Book book) {
        book.setId(context.getBean(IdProvider.class).provideId(book));
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(String bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public void removeAllBooksToAuthor(String author) {
        for (Book book : retreiveAll()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                repo.remove(book);
            }
        }
    }

    @Override
    public void removeAllBooksToTitle(String title) {
        for (Book book : retreiveAll()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                repo.remove(book);
            }
        }
    }

    @Override
    public boolean removeAllBooksToSize(Integer size) {
        for (Book book : retreiveAll()) {
            if (book.getSize().equals(size)) {
                repo.remove(book);
            }
        }
        return true;
    }

    @Override
    public List<Book> filterByAuthor(String author) {
        return repo.stream()
                .filter(x -> x.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> filterByTitle(String title) {
        return repo.stream()
                .filter(x -> x.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> filterBySize(Integer size) {
        return repo.stream()
                .filter(x -> x.getSize().equals(size))
                .collect(Collectors.toList());
    }

    public int size() {
        return repo.size();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void defaultInit() {
        logger.info("default INIT in book repo bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in book repo bean");
    }
}
