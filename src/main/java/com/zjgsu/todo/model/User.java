package com.zjgsu.todo.model;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
public class User {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
