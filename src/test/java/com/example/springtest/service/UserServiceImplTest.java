package com.example.springtest.service;

import com.example.springtest.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 3); // We seed 3 users on startup
    }

    @Test
    void testCreateUser() {
        User newUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .build();
        
        User created = userService.createUser(newUser);
        assertNotNull(created.getId());
        assertEquals("testuser", created.getUsername());
    }

    @Test
    void testGetUserById() {
        List<User> users = userService.getAllUsers();
        User firstUser = users.get(0);
        
        User found = userService.getUserById(firstUser.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(firstUser.getUsername(), found.getUsername());
    }
}
