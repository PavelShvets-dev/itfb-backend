package com.pnshvets.interview.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AppRole implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }

}
