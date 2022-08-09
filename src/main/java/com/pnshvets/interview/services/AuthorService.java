package com.pnshvets.interview.services;

import java.util.List;
import java.util.Optional;

import com.pnshvets.interview.entities.AppAuthor;

public interface AuthorService {

    /*
     * Add new author
     */
    AppAuthor save(AppAuthor author);

    /*
     * Update existing author
     */
    Optional<AppAuthor> update(AppAuthor author);

    /*
     * Get all authors
     */
    List<AppAuthor> getAll();

    /*
     * Get all authors
     */
    Optional<AppAuthor> getOne(Long id);

    /*
     * Delete existing author
     */
    void delete(Long id);
}
