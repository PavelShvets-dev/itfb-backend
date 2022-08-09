package com.pnshvets.interview.repository.customrepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pnshvets.interview.entities.AppBook;

@SuppressWarnings("unchecked")
public class CustomBookRepositoryImpl implements CustomBookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AppBook> findByAuthor(String author) {
        return (List<AppBook>) entityManager
                .createQuery("FROM AppBook b JOIN b.authors a WHERE a.name LIKE CONCAT('%',:author,'%')")
                .setParameter("author", author)
                .getResultList();
    }

}
