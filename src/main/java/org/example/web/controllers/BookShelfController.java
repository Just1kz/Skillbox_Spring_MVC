package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.service.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "books")
@Scope("singleton")
public class BookShelfController {

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @GetMapping(value = "/shelfAfterRemoveOrSave")
    public String booksAfterRemove(
            Model model,
            @RequestParam(value = "msg") String msg) {
        logger.info("got book shelft");
        model.addAttribute("book", new Book());
        model.addAttribute("msg", msg);
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @GetMapping(value = "/shelfFiltered")
    public String booksWithFilter(Model model, @RequestParam(value = "filter") String filter) {
        logger.info("got book shelft");
        model.addAttribute("book", new Book());
        logger.info(filter);
        String[] rsl = filter.split("=");
        logger.info(rsl[1]);
        if (rsl[0].equals("author")) {
            model.addAttribute("bookList", bookService.filterByAuthor(rsl[1]));
            logger.info(bookService.filterByAuthor(rsl[1]));
        }
        if (rsl[0].equals("title")) {
            model.addAttribute("bookList", bookService.filterByTitle(rsl[1]));
        }
        if (rsl[0].equals("size")) {
            model.addAttribute("bookList", bookService.filterBySize(Integer.parseInt(rsl[1])));
        }
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book, Model model) {
        if (!book.getAuthor().isEmpty()
                || !book.getTitle().isEmpty()
                || book.getSize() != null
                || book.getId() != null) {
            bookService.saveBook(book);
            model.addAttribute("msg", new StringBuffer("Book with ID: " + book.getId() + " is save!"));
            logger.info("current repository size: " + bookService.getAllBooks().size());
        } else {
            model.addAttribute("msg", new StringBuffer("Pls set parameters for save book!"));
        }
        return "redirect:/books/shelfAfterRemoveOrSave";
    }

    @PostMapping("/remove")
    public String removeBook(
            @RequestParam(value = "bookIdToRemove") String bookIdToRemove,
            @RequestParam(value = "bookAuthorToRemove") String bookAuthorToRemove,
            @RequestParam(value = "bookTitleToRemove") String bookTitleToRemove,
            @RequestParam(value = "bookSizeToRemove") Integer bookSizeToRemove,
            Model model) {
        if (bookService.removeBookById(bookIdToRemove)) {
            model.addAttribute("msg", new StringBuffer("Remove book: " + bookIdToRemove + " is done!"));
        } else {
            model.addAttribute("msg", new StringBuffer("Book with id: " + bookIdToRemove + " is not Founded!"));
        }

        if (!"".equals(bookAuthorToRemove)) {
            bookService.removeBookByAuthor(bookAuthorToRemove);
        }

        if (!"".equals(bookTitleToRemove)) {
            bookService.removeBookByTitle(bookTitleToRemove);
        }

        if (bookService.removeBookBySize(bookSizeToRemove)) {
            logger.info("delete by size is Done!");
        }

        return "redirect:/books/shelfAfterRemoveOrSave";
    }

    @PostMapping("/filter")
    public String filterBook(
            @RequestParam(value = "bookAuthorToFilter") String bookAuthorToFilter,
            @RequestParam(value = "bookTitleToFilter") String bookTitleToFilter,
            @RequestParam(value = "bookSizeToFilter") String bookSizeToFilter,
            Model model) {
        if (!"".equals(bookAuthorToFilter)) {
            model.addAttribute("filter", "author=" + bookAuthorToFilter);
        }
        if (!"".equals(bookTitleToFilter)) {
            model.addAttribute("filter", "title=" + bookTitleToFilter);
        }
        if (!"".equals(bookSizeToFilter)) {
            int sizeToFilter = Integer.parseInt(bookSizeToFilter);
            model.addAttribute("filter", "size=" + sizeToFilter);
        }

        return "redirect:/books/shelfFiltered";
    }
}
