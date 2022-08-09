package com.pnshvets.interview.repository.customrepository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.pnshvets.interview.entities.AppBook;

public interface CustomBookRepository {
    List<AppBook> findByAuthor(@Param("author") String author);
}
