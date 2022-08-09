package com.pnshvets.interview.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pnshvets.interview.entities.AppUser;
import com.pnshvets.interview.repository.UserRepository;
import com.pnshvets.interview.services.UserService;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser save(AppUser user) {
        log.debug("Save new user {}", user);

        return userRepository.save(user);
    }

    @Override
    public Optional<AppUser> update(AppUser user) {
        log.debug("Update user {}", user);

        return userRepository
                .findById(user.getId())
                .map(
                        existinguser -> {
                            if (user.getLogin() != null) {
                                existinguser.setLogin(user.getLogin());
                            }
                            if (user.getPassword() != null) {
                                existinguser.setPassword(user.getPassword());
                            }
                            if (user.getRoles() != null) {
                                existinguser.setRoles(user.getRoles());
                            }

                            return existinguser;
                        })
                .map(userRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUser> getAll() {
        log.debug("Request to get list of users");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUser> getOne(Long id) {
        log.debug("Request to get user with id = {}", id);
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete user by id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findOneWithAuthoritiesByLogin(username.toLowerCase())
                .map(user -> createSpringSecurityUser(username.toLowerCase(), user))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User " + username.toLowerCase() + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin,
            AppUser user) {
        List<GrantedAuthority> grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                grantedAuthorities);
    }

}
