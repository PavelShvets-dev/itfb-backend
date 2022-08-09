package com.pnshvets.interview.auth;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthModel {
    private Long id;
    private String username;
    private String password;
    private Set<String> authorities;
}
