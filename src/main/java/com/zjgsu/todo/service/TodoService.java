package com.zjgsu.todo.service;

import com.zjgsu.todo.exception.ResourceNotFoundException;
import com.zjgsu.todo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Todo服务层
 * 使用内存存储，演示RESTful API
 */
@Service
public class TodoService {
    // 使用内存存储
    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final UserService userService;

    public TodoService(UserService userService) {
        this.userService = userService;
        // 初始化一些测试数据
        createTodo(new Todo(null, "学习Spring Boot", "完成Spring Boot基础教程", 1L));
        createTodo(new Todo(null, "实现RESTful API", "创建用户和Todo的CRUD接口", 1L));
        createTodo(new Todo(null, "编写文档", "完善API文档", 2L));
    }

    /**
     * 获取所有Todo
     */
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    /**
     * 根据用户ID获取Todo列表
     */
    public List<Todo> findByUserId(Long userId) {
        return todos.values().stream()
                .filter(todo -> todo.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查找Todo
     */
    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todos.get(id));
    }

    /**
     * 创建Todo
     */
    public Todo createTodo(Todo todo) {
        // 验证用户是否存在
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }

        Long id = idCounter.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    /**
     * 更新Todo
     */
    public Todo updateTodo(Long id, Todo todo) {
        if (!todos.containsKey(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }

        // 验证用户是否存在
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }

        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    /**
     * 删除Todo
     */
    public boolean deleteTodo(Long id) {
        if (!todos.containsKey(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        todos.remove(id);
        return true;
    }

    /**
     * 切换Todo完成状态
     */
    public Todo toggleComplete(Long id) {
        Todo todo = todos.get(id);
        if (todo == null) {
            throw new ResourceNotFoundException("Todo", id);
        }
        todo.setCompleted(!todo.getCompleted());
        return todo;
    }
}
