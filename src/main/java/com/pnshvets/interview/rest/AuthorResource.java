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

import com.pnshvets.interview.entities.AppAuthor;
import com.pnshvets.interview.repository.AuthorRepository;
import com.pnshvets.interview.rest.errors.AppEntityAlreadyExistsException;
import com.pnshvets.interview.rest.errors.AppEntityNotFoundException;
import com.pnshvets.interview.services.AuthorService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
public class AuthorResource {
    private final String ENTITY_NAME = "AppAuthor";

    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    public AuthorResource(AuthorService authorService, AuthorRepository authorRepository) {
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    /*
     * Create new author
     */
    @PostMapping("/author")
    public ResponseEntity<AppAuthor> saveAuthor(@RequestBody AppAuthor author) throws URISyntaxException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, author);
        if (author.getId() != null) {
            throw new AppEntityAlreadyExistsException("New entity should not have predefined ID");
        }
        AppAuthor result = authorService.save(author);
        return ResponseEntity
                .created(new URI("/api/author/" + result.getId()))
                .body(result);
    }

    /*
     * Update existing author
     */
    @PutMapping("/author/{id}")
    public ResponseEntity<AppAuthor> updateAuthor(@PathVariable(value = "id", required = false) final Long id,
            @RequestBody AppAuthor author)
            throws URISyntaxException {
        log.debug("REST request to update {} : {}", ENTITY_NAME, author);
        if (author.getId() == null) {
            throw new AppEntityNotFoundException("Invalid id - NULL");
        }
        if (!Objects.equals(id, author.getId())) {
            throw new RuntimeException("Invalid Id - wrong value");
        }

        if (!authorRepository.existsById(id)) {
            throw new AppEntityNotFoundException("Entity not found");
        }

        AppAuthor result = authorRepository.save(author);
        return ResponseEntity
                .ok()
                .body(result);
    }

    /*
     * Get list of all authors
     */
    @GetMapping("/author/list")
    public ResponseEntity<List<AppAuthor>> getAuthors() {
        log.debug("REST request to get list of authors");
        List<AppAuthor> all = authorService.getAll();
        return ResponseEntity.ok().body(all);
    }

    /*
     * Get author by id
     */
    @GetMapping("/author/{id}")
    public ResponseEntity<AppAuthor> getAuthorById(@PathVariable Long id) {
        log.debug("REST request to get author : {}", id);
        Optional<AppAuthor> author = authorService.getOne(id);

        if (author.isPresent()) {
            return ResponseEntity.ok().body(author.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * Delete author by id
     */
    @DeleteMapping("/author/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.debug("REST request to delete author by id : {}", id);
        authorService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
