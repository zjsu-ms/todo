package com.zjgsu.todo.service;

import com.zjgsu.todo.exception.ResourceNotFoundException;
import com.zjgsu.todo.model.Todo;
import com.zjgsu.todo.repository.TodoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Todo服务层
 * 使用数据库存储
 */
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserService userService;

    public TodoService(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    /**
     * 初始化测试数据
     */
    @PostConstruct
    public void init() {
        // 只在数据库为空时初始化测试数据
        if (todoRepository.count() == 0) {
            createTodo(new Todo(null, "学习Spring Boot", "完成Spring Boot基础教程", 1L));
            createTodo(new Todo(null, "实现RESTful API", "创建用户和Todo的CRUD接口", 1L));
            createTodo(new Todo(null, "编写文档", "完善API文档", 2L));
        }
    }

    /**
     * 获取所有Todo
     */
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    /**
     * 根据用户ID获取Todo列表
     */
    public List<Todo> findByUserId(Long userId) {
        return todoRepository.findByUserId(userId);
    }

    /**
     * 根据ID查找Todo
     */
    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    /**
     * 创建Todo
     */
    @Transactional
    public Todo createTodo(Todo todo) {
        // 验证用户是否存在
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }
        return todoRepository.save(todo);
    }

    /**
     * 更新Todo
     */
    @Transactional
    public Todo updateTodo(Long id, Todo todo) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", id));

        // 验证用户是否存在
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }

        // 更新字段
        existingTodo.setTitle(todo.getTitle());
        existingTodo.setDescription(todo.getDescription());
        existingTodo.setCompleted(todo.getCompleted());
        existingTodo.setUserId(todo.getUserId());

        return todoRepository.save(existingTodo);
    }

    /**
     * 删除Todo
     */
    @Transactional
    public boolean deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        todoRepository.deleteById(id);
        return true;
    }

    /**
     * 切换Todo完成状态
     */
    @Transactional
    public Todo toggleComplete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", id));
        todo.setCompleted(!todo.getCompleted());
        return todoRepository.save(todo);
    }
}
