package com.pnshvets.interview.rest;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnshvets.interview.auth.AuthModel;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
public class AccountPublicResource {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AccountPublicResource(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @GetMapping("/authentificated")
    public ResponseEntity<AuthModel> authentificated() {
        AuthModel auth = new AuthModel();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            auth.setUsername(authentication.getName());
            auth.setAuthorities(
                    authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));
        }

        return ResponseEntity.ok().body(auth);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthModel> signin(@RequestBody AuthModel auth) {
        log.info("Request to signin with {}", auth.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                auth.getUsername(),
                auth.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        auth.setAuthorities(
                authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));

        return ResponseEntity.ok().body(auth);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request to logout");
        new SecurityContextLogoutHandler().logout(request, response, null);
    }
}
