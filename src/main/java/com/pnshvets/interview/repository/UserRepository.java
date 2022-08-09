package com.pnshvets.interview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pnshvets.interview.entities.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<AppUser> findOneWithAuthoritiesByLogin(String login);
}
