package com.phone.service;

import com.phone.model.User;
import com.phone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getUsers(Pageable pageable, String filterField, String filterValue) {
        if (filterField != null && filterValue != null && !filterValue.isEmpty()) {
            switch (filterField) {
                case "firstName":
                    return userRepository.findByFirstNameContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "lastName":
                    return userRepository.findByLastNameContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "email":
                    return userRepository.findByEmailContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "department":
                    return userRepository.findByDepartmentContainingIgnoreCaseAndDeletedFalse(filterValue, pageable);
                case "status":
                    try {
                        User.UserStatus status = User.UserStatus.valueOf(filterValue.toUpperCase());
                        return userRepository.findByStatusAndDeletedFalse(status, pageable);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid status value
                        return Page.empty(pageable);
                    }
                // Add more filter cases as needed
                default:
                    // No valid filter field provided, return all non-deleted users with pagination/sorting
                    return userRepository.findByDeletedFalse(pageable);
            }
        } else {
            // No filter provided, return all non-deleted users with pagination/sorting
            return userRepository.findByDeletedFalse(pageable);
        }
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }

    public List<User> getUsersByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByFirstNameContainingOrLastNameContaining(searchTerm, searchTerm);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userDetails.getFirstName());
                    existingUser.setLastName(userDetails.getLastName());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setDepartment(userDetails.getDepartment());
                    existingUser.setStatus(userDetails.getStatus());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setDeleted(true);
            userRepository.save(user);
        });
    }

    public User markUserAsLeft(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setStatus(User.UserStatus.LEFT_COMPANY);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
} 