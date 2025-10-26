package com.zjgsu.todo.controller;

import com.zjgsu.todo.common.ApiResponse;
import com.zjgsu.todo.model.Todo;
import com.zjgsu.todo.service.TodoService;
import com.zjgsu.todo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户Todo嵌套资源Controller
 * 演示RESTful API的资源嵌套关系
 * 路径：/api/users/{userId}/todos
 */
@RestController
@RequestMapping("/api/users/{userId}/todos")
public class UserTodoController {

    private final UserService userService;
    private final TodoService todoService;

    public UserTodoController(UserService userService, TodoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }

    /**
     * 获取指定用户的所有Todo
     * GET /api/users/{userId}/todos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Todo>>> getUserTodos(@PathVariable Long userId) {
        // 验证用户是否存在
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("User not found with id: " + userId));
        }

        List<Todo> todos = todoService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(todos));
    }

    /**
     * 为指定用户创建Todo
     * POST /api/users/{userId}/todos
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Todo>> createUserTodo(
            @PathVariable Long userId,
            @RequestBody Todo todo) {
        // 验证用户是否存在
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("User not found with id: " + userId));
        }

        todo.setUserId(userId);
        Todo created = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }
}
