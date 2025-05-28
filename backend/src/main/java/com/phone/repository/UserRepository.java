package com.phone.repository;

import com.phone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByDepartment(String department);
    List<User> findByStatus(User.UserStatus status);
    List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    List<User> findByDeletedFalse();

    // Methods for pagination, sorting, and filtering
    Page<User> findByDeletedFalse(Pageable pageable);
    Page<User> findByFirstNameContainingIgnoreCaseAndDeletedFalse(String firstName, Pageable pageable);
    Page<User> findByLastNameContainingIgnoreCaseAndDeletedFalse(String lastName, Pageable pageable);
    Page<User> findByEmailContainingIgnoreCaseAndDeletedFalse(String email, Pageable pageable);
    Page<User> findByDepartmentContainingIgnoreCaseAndDeletedFalse(String department, Pageable pageable);
    Page<User> findByStatusAndDeletedFalse(User.UserStatus status, Pageable pageable);
} 