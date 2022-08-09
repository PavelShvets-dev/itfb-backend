package com.pnshvets.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pnshvets.interview.entities.AppAuthor;

@Repository
public interface AuthorRepository extends JpaRepository<AppAuthor, Long> {

}
