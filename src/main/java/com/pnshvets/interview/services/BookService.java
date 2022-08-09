package com.pnshvets.interview.services;

import java.util.List;
import java.util.Optional;

import com.pnshvets.interview.entities.AppBook;

public interface BookService {

    /*
     * Add new book
     */
    AppBook save(AppBook book);

    /*
     * Update existing book
     */
    Optional<AppBook> update(AppBook book);

    /*
     * Get all books
     */
    List<AppBook> getAll();

    /*
     * Get all books by author
     */
    List<AppBook> findByAuthor(String author);

    /*
     * Get all books
     */
    Optional<AppBook> getOne(Long id);

    /*
     * Delete existing book
     */
    void delete(Long id);
}
