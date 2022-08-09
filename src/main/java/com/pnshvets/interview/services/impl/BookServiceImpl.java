package com.pnshvets.interview.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pnshvets.interview.entities.AppBook;
import com.pnshvets.interview.repository.BookRepository;
import com.pnshvets.interview.services.BookService;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public AppBook save(AppBook book) {
        log.debug("Save new book {}", book);

        return bookRepository.save(book);
    }

    @Override
    public Optional<AppBook> update(AppBook book) {
        log.debug("Update book {}", book);

        return bookRepository
                .findById(book.getId())
                .map(
                        existingbook -> {
                            if (book.getName() != null) {
                                existingbook.setName(book.getName());
                            }

                            return existingbook;
                        })
                .map(bookRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppBook> getAll() {
        log.debug("Request to get list of books");
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppBook> getOne(Long id) {
        log.debug("Request to get book with id = {}", id);
        return bookRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete book by id {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public List<AppBook> findByAuthor(String author) {
        log.debug("Request to get list of books by author");
        return bookRepository.findByAuthor(author);
    }

}
