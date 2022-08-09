package com.pnshvets.interview.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnshvets.interview.auth.AuthModel;
import com.pnshvets.interview.entities.AppRole;
import com.pnshvets.interview.entities.AppUser;
import com.pnshvets.interview.repository.RoleRepository;
import com.pnshvets.interview.repository.UserRepository;
import com.pnshvets.interview.rest.errors.AppEntityAlreadyExistsException;
import com.pnshvets.interview.rest.errors.AppEntityNotFoundException;
import com.pnshvets.interview.services.UserService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthResource {

    private final String ENTITY_NAME = "AppUser";

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthResource(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /*
     * Create new user
     */
    @PostMapping("/user")
    public ResponseEntity<AppUser> saveUser(@RequestBody AuthModel authModel) throws URISyntaxException {
        log.info("REST request to save {} : {}", ENTITY_NAME, authModel);
        if (authModel.getId() != null) {
            throw new AppEntityAlreadyExistsException("New entity should not have predefined ID");
        }

        AppUser user = new AppUser();
        user.setLogin(authModel.getUsername());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(authModel.getPassword()));

        if (authModel.getAuthorities() != null) {
            user.setRoles(authModel.getAuthorities().stream().map(a -> {
                AppRole appRole = new AppRole();
                appRole.setName(a);

                return appRole;
            }).collect(Collectors.toSet()));
        }

        AppUser result = userService.save(user);
        return ResponseEntity
                .created(new URI("/api/user/" + result.getId()))
                .body(result);
    }

    /*
     * Update existing user
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable(value = "id", required = false) final Long id,
            @RequestBody AuthModel authModel)
            throws URISyntaxException {
        log.info("REST request to update {} : {}", ENTITY_NAME, authModel);
        if (authModel.getId() == null) {
            throw new AppEntityNotFoundException("Invalid id - NULL");
        }
        if (!Objects.equals(id, authModel.getId())) {
            throw new RuntimeException("Invalid Id - wrong value");
        }

        if (!userRepository.existsById(id)) {
            throw new AppEntityNotFoundException("Entity not found");
        }

        AppUser user = userRepository.getById(id);

        user.setLogin(authModel.getUsername());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(authModel.getPassword()));

        if (authModel.getAuthorities() != null) {
            user.setRoles(authModel.getAuthorities().stream().map(a -> {
                AppRole appRole = new AppRole();
                appRole.setName(a);

                return appRole;
            }).collect(Collectors.toSet()));
        }

        AppUser result = userRepository.save(user);
        return ResponseEntity
                .ok()
                .body(result);
    }

    /*
     * Get list of all users
     */
    @GetMapping("/user/list")
    public ResponseEntity<List<AuthModel>> getUsers() {
        log.info("REST request to get list of users");
        List<AuthModel> result = userService.getAll().stream().map(u -> {
            AuthModel authModel = new AuthModel();
            authModel.setId(u.getId());
            authModel.setUsername(u.getLogin());
            authModel.setAuthorities(u.getRoles().stream().map(r -> r.getAuthority()).collect(Collectors.toSet()));

            return authModel;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    /*
     * Get list of all roles
     */
    @GetMapping("/role/list")
    public ResponseEntity<List<AppRole>> getRoles() {
        log.info("REST request to get list of roles");
        return ResponseEntity.ok().body(roleRepository.findAll());
    }

    /*
     * Get user by id
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<AuthModel> getUserById(@PathVariable Long id) {
        log.info("REST request to get user : {}", id);
        Optional<AppUser> u = userService.getOne(id);

        if (u.isPresent()) {
            AppUser user = u.get();
            AuthModel authModel = new AuthModel();
            authModel.setId(user.getId());
            authModel.setUsername(user.getLogin());
            authModel.setAuthorities(user.getRoles().stream().map(r -> r.getAuthority()).collect(Collectors.toSet()));
            return ResponseEntity.ok().body(authModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * Delete user by id
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST request to delete user by id : {}", id);
        userService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
