package com.pnshvets.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pnshvets.interview.entities.AppRole;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, String> {

}
