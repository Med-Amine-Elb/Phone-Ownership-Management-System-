package com.phone.repository;

import com.phone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByDepartment(String department);
    List<User> findByStatus(User.UserStatus status);
    List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
} 