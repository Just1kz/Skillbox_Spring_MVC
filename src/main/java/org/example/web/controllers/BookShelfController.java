package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.service.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.dataBookToAction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;

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

    public <T> Model initModel(Model model, T rsl) {

        if (rsl instanceof  Book) {
            model.addAttribute("book", rsl);
        } else {
            model.addAttribute("book", new Book());
        }

        if (rsl instanceof BookIdToAction) {
            model.addAttribute("bookIdToAction", rsl);
        } else {
            model.addAttribute("bookIdToAction", new BookIdToAction());
        }

        if (rsl instanceof BookAuthorToAction) {
            model.addAttribute("bookAuthorToAction", rsl);
        } else {
            model.addAttribute("bookAuthorToAction", new BookAuthorToAction());
        }

        if (rsl instanceof BookTitleToAction) {
            model.addAttribute("bookTitleToAction", rsl);
        } else {
            model.addAttribute("bookTitleToAction", new BookTitleToAction());
        }

        if (rsl instanceof BookSizeToAction) {
            model.addAttribute("bookSizeToAction", rsl);
        } else {
            model.addAttribute("bookSizeToAction", new BookSizeToAction());
        }

        if (rsl instanceof List) {
            model.addAttribute("bookList", rsl);
        } else {
            model.addAttribute("bookList", bookService.getAllBooks());
        }

        if (rsl instanceof BookFilter) {
            model.addAttribute("bookFilter", rsl);
        } else {
            model.addAttribute("bookFilter", new BookFilter());
        }

        return model;
    }

    @GetMapping(value = "/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model = initModel(model, bookService.getAllBooks());
        //model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @GetMapping(value = "/shelfFiltered")
    public String booksWithFilter(Model model, @RequestParam(value = "filter") String filter) {
        logger.info("got book shelft");
        //model = initModel(model);
        logger.info(filter);
        String[] rsl = filter.split("=");
        logger.info(rsl[1]);
        if (rsl[0].equals("author")) {
            model = initModel(model, bookService.filterByAuthor(rsl[1]));
            logger.info(bookService.filterByAuthor(rsl[1]));
        }
        if (rsl[0].equals("title")) {
            model = initModel(model, bookService.filterByTitle(rsl[1]));
        }
        if (rsl[0].equals("size")) {
            model = initModel(model, bookService.filterBySize(Integer.parseInt(rsl[1])));
        }
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model = initModel(model, book);
            logger.info("we have errors in book parameter");
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/id")
    public String removeBookById(
            @Valid BookIdToAction bookIdToAction,
            BindingResult bindingResult,
            Model model) {
        if(bindingResult.hasErrors()) {
            model = initModel(model, bookIdToAction);
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToAction.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/author")
    public String removeBookByAuthor(
            @Valid BookAuthorToAction bookAuthorToAction,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model = initModel(model, bookAuthorToAction);
            return "book_shelf";
        } else {
            bookService.removeBookByAuthor(bookAuthorToAction.getAuthor());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/title")
    public String removeBookByTitle(
            @Valid BookTitleToAction bookTitleToAction,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model = initModel(model, bookTitleToAction);
            return "book_shelf";
        } else {
            bookService.removeBookByTitle(bookTitleToAction.getTitle());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove/size")
    public String removeBookBySize(
            @Valid BookSizeToAction bookSizeToAction,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            logger.info(bindingResult.getAllErrors());
            logger.info(bindingResult.getFieldError());
            model = initModel(model, bookSizeToAction);
            return "book_shelf";
        } else {
            bookService.removeBookBySize(bookSizeToAction.getSize());
            return "redirect:/books/shelf";
        }
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

    @PostMapping("/filter/ultra")
    public String filterBookToClass(
            @Valid BookFilter bookFilter,
            BindingResult bindingResult,
            Model model) {

        List<ObjectError> rsl = bindingResult.getAllErrors();
        for (ObjectError x : rsl) {
            logger.info(x.toString());
            logger.info(x.getObjectName());
            logger.info(x.getCode());
            logger.info(x.getDefaultMessage());
            if (!x.toString().contains("on field 'author'") && !bookFilter.getAuthor().isEmpty()) {
                model.addAttribute("filter", "author=" + bookFilter.getAuthor());
                return "redirect:/books/shelfFiltered";
            }
            if (!x.toString().contains("on field 'title'") && !bookFilter.getTitle().isEmpty()) {
                model.addAttribute("filter", "title=" + bookFilter.getTitle());
                return "redirect:/books/shelfFiltered";
            }
            if (!x.toString().contains("on field 'size'") && bookFilter.getSize() != null) {
                model.addAttribute("filter", "size=" + bookFilter.getSize());
                return "redirect:/books/shelfFiltered";
            }
        }
        model = initModel(model, bookFilter);
        return "book_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_upload");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("new file saved at: " + serverFile.getAbsolutePath());
        model = initModel(model, new Object());
        return "redirect: /books_market/books/shelf";
    }

    @PostMapping("/downloadFile")
    public void downloadFile(@RequestParam("file") MultipartFile file,
                               Model model,
                               HttpServletResponse response) throws IOException {
        response.setContentType("image/jpg");
        response.addHeader("Content-Disposition", "attachment; filename="+file.getOriginalFilename());
        response.getOutputStream().write(file.getBytes());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public String handlerError(Model model, FileNotFoundException exception) {
        model = initModel(model, exception);
        model.addAttribute("errorMessage", exception.getMessage());
        return "book_shelf";
    }
}
