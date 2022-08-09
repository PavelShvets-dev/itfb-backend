package com.pnshvets.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pnshvets.interview.entities.AppBook;
import com.pnshvets.interview.repository.customrepository.CustomBookRepository;

@Repository
public interface BookRepository extends JpaRepository<AppBook, Long>, CustomBookRepository {
}
