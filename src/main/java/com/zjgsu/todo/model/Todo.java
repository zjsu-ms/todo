package com.zjgsu.todo.model;

import java.time.LocalDateTime;

/**
 * Todo实体类
 */
public class Todo {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private Long userId; // 关联的用户ID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Todo() {
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Todo(Long id, String title, String description, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
