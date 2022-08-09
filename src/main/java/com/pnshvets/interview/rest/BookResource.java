package com.pnshvets.interview.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnshvets.interview.entities.AppBook;
import com.pnshvets.interview.repository.BookRepository;
import com.pnshvets.interview.rest.errors.AppEntityAlreadyExistsException;
import com.pnshvets.interview.rest.errors.AppEntityNotFoundException;
import com.pnshvets.interview.services.BookService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
public class BookResource {
    private final String ENTITY_NAME = "AppBook";

    private final BookService bookService;
    private final BookRepository bookRepository;

    public BookResource(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    /*
     * Create new book
     */
    @PostMapping("/book")
    public ResponseEntity<AppBook> saveBook(@RequestBody AppBook book) throws URISyntaxException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, book);
        if (book.getId() != null) {
            throw new AppEntityAlreadyExistsException("New entity should not have predefined ID");
        }
        AppBook result = bookService.save(book);
        return ResponseEntity
                .created(new URI("/api/book/" + result.getId()))
                .body(result);
    }

    /*
     * Update existing book
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<AppBook> updateBook(@PathVariable(value = "id", required = false) final Long id,
            @RequestBody AppBook book)
            throws URISyntaxException {
        log.debug("REST request to update {} : {}", ENTITY_NAME, book);
        if (book.getId() == null) {
            throw new AppEntityNotFoundException("Invalid id - NULL");
        }
        if (!Objects.equals(id, book.getId())) {
            throw new RuntimeException("Invalid Id - wrong value");
        }

        if (!bookRepository.existsById(id)) {
            throw new AppEntityNotFoundException("Entity not found");
        }

        AppBook result = bookRepository.save(book);
        return ResponseEntity
                .ok()
                .body(result);
    }

    /*
     * Get list of all books
     */
    @GetMapping("/book/list")
    public ResponseEntity<List<AppBook>> getBooks() {
        log.debug("REST request to get list of books");
        List<AppBook> all = bookService.getAll();
        return ResponseEntity.ok().body(all);
    }

    /*
     * Get list of books by criteria
     */
    @GetMapping("/book/findBooks/{searchBy}")
    public ResponseEntity<List<AppBook>> findBooks(@PathVariable String searchBy) {
        log.debug("REST request to get list of books by criteria");
        List<AppBook> byAuthor = bookService.findByAuthor(searchBy);
        return ResponseEntity.ok().body(byAuthor);
    }

    /*
     * Get book by id
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<AppBook> getBookById(@PathVariable Long id) {
        log.debug("REST request to get book : {}", id);
        Optional<AppBook> book = bookService.getOne(id);

        if (book.isPresent()) {
            return ResponseEntity.ok().body(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * Delete book by id
     */
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete book by id : {}", id);
        bookService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
