package com.pnshvets.interview.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pnshvets.interview.entities.AppUser;

public interface UserService extends UserDetailsService {

    /*
     * Add new user
     */
    AppUser save(AppUser user);

    /*
     * Update existing user
     */
    Optional<AppUser> update(AppUser user);

    /*
     * Get all users
     */
    List<AppUser> getAll();

    /*
     * Get all users
     */
    Optional<AppUser> getOne(Long id);

    /*
     * Delete existing user
     */
    void delete(Long id);
}
