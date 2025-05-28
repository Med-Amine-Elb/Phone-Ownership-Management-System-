package com.phone.controller;

import com.phone.model.User;
import com.phone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<com.phone.dto.UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String filterField,
            @RequestParam(required = false) String filterValue) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<User> usersPage = userService.getUsers(
                pageable, filterField, filterValue);

        return ResponseEntity.ok(usersPage.map(User::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.phone.dto.UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(u -> ResponseEntity.ok(User.toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<com.phone.dto.UserDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(u -> ResponseEntity.ok(User.toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    public List<com.phone.dto.UserDto> getUsersByDepartment(@PathVariable String department) {
        return User.toDtoList(userService.getUsersByDepartment(department));
    }

    @GetMapping("/status/{status}")
    public List<com.phone.dto.UserDto> getUsersByStatus(@PathVariable com.phone.model.User.UserStatus status) {
        return User.toDtoList(userService.getUsersByStatus(status));
    }

    @GetMapping("/search")
    public List<com.phone.dto.UserDto> searchUsers(@RequestParam String query) {
        return User.toDtoList(userService.searchUsers(query));
    }

    @PostMapping
    public com.phone.dto.UserDto createUser(@RequestBody com.phone.model.User user) {
        return User.toDto(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.phone.dto.UserDto> updateUser(@PathVariable Long id, @RequestBody com.phone.model.User user) {
        try {
            com.phone.model.User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(User.toDto(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/mark-left")
    public ResponseEntity<com.phone.dto.UserDto> markUserAsLeft(@PathVariable Long id) {
        try {
            com.phone.model.User updatedUser = userService.markUserAsLeft(id);
            return ResponseEntity.ok(User.toDto(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 