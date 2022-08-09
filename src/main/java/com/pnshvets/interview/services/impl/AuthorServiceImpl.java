package com.pnshvets.interview.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pnshvets.interview.entities.AppAuthor;
import com.pnshvets.interview.repository.AuthorRepository;
import com.pnshvets.interview.services.AuthorService;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AppAuthor save(AppAuthor author) {
        log.debug("Save new author {}", author);

        return authorRepository.save(author);
    }

    @Override
    public Optional<AppAuthor> update(AppAuthor author) {
        log.debug("Update author {}", author);

        return authorRepository
                .findById(author.getId())
                .map(
                        existingAuthor -> {
                            if (author.getName() != null) {
                                existingAuthor.setName(author.getName());
                            }

                            return existingAuthor;
                        })
                .map(authorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppAuthor> getAll() {
        log.debug("Request to get list of authors");
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppAuthor> getOne(Long id) {
        log.debug("Request to get author with id = {}", id);
        return authorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete author by id {}", id);
        authorRepository.deleteById(id);
    }

}
