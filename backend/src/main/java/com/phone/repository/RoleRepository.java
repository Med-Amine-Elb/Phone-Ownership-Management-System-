package com.phone.repository;

import com.phone.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByDeletedFalse();
    Optional<Role> findByName(Role.RoleType name);
} 