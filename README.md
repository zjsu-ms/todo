# Todo RESTful API Demo - 从零开始教程

一个简洁的Spring Boot项目，用于演示RESTful API的设计和实现。本教程将手把手教你从零开始构建这个项目。

## 📋 目录

- [项目说明](#项目说明)
- [技术栈](#技术栈)
- [环境准备](#环境准备)
- [从零开始构建项目](#从零开始构建项目)
  - [第一步：创建Spring Boot项目](#第一步创建spring-boot项目)
  - [第二步：在IntelliJ IDEA中打开项目](#第二步在intellij-idea中打开项目)
  - [第三步：创建实体类](#第三步创建实体类)
  - [第四步：创建统一响应格式](#第四步创建统一响应格式)
  - [第五步：创建Service层](#第五步创建service层)
  - [第六步：创建Controller层](#第六步创建controller层)
  - [第七步：创建全局异常处理](#第七步创建全局异常处理)
  - [第八步：配置应用](#第八步配置应用)
  - [第九步：运行项目](#第九步运行项目)
  - [第十步：测试API](#第十步测试api)
- [完整API文档](#完整api文档)
- [学习要点](#学习要点)

---

## 项目说明

本项目实现了用户(User)和待办事项(Todo)的完整CRUD操作，展示了：

- ✅ RESTful API设计原则
- ✅ 统一的响应格式
- ✅ 正确使用HTTP状态码
- ✅ 资源嵌套关系
- ✅ 全局异常处理

## 技术栈

- **Spring Boot** 3.5.6
- **Java** 25（或Java 17+）
- **Maven** 3.8+
- **IntelliJ IDEA** 2024+（推荐使用Community或Ultimate版本）

## 环境准备

在开始之前，请确保你的电脑上已安装以下软件：

### 1. 安装Java JDK

```bash
# 检查Java版本
java -version
```

如果未安装，请从以下地址下载安装：
- Oracle JDK: https://www.oracle.com/java/technologies/downloads/
- OpenJDK: https://adoptium.net/

### 2. 安装Maven（可选）

Spring Boot项目自带Maven Wrapper（`mvnw`），无需单独安装Maven。

### 3. 安装IntelliJ IDEA

下载地址：https://www.jetbrains.com/idea/download/

推荐安装Ultimate版本（学生可免费使用），Community版本也完全够用。

---

## 从零开始构建项目

### 第一步：创建Spring Boot项目

#### 方式一：使用Spring Initializr网站（推荐新手）

1. 打开浏览器，访问 https://start.spring.io/

2. 配置项目参数：
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.5.6
   - **Project Metadata**:
     - Group: `com.zjgsu`
     - Artifact: `todo`
     - Name: `todo`
     - Package name: `com.zjgsu.todo`
     - Packaging: `Jar`
     - Java: `25` (或17)

3. 添加依赖（Dependencies）：
   - 点击 "ADD DEPENDENCIES" 按钮
   - 搜索并添加：`Spring Web`

4. 点击 "GENERATE" 按钮下载项目压缩包

5. 解压下载的 `todo.zip` 文件到你的工作目录

#### 方式二：使用IntelliJ IDEA创建（推荐）

1. 打开IntelliJ IDEA

2. 点击 `File` -> `New` -> `Project...`

3. 在左侧选择 `Spring Initializr`

4. 配置项目：
   - **Name**: `todo`
   - **Location**: 选择你的项目保存位置
   - **Language**: `Java`
   - **Type**: `Maven`
   - **Group**: `com.zjgsu`
   - **Artifact**: `todo`
   - **Package name**: `com.zjgsu.todo`
   - **JDK**: 选择已安装的JDK（推荐17或更高版本）
   - **Java**: `25` (或17)
   - **Packaging**: `Jar`

5. 点击 `Next`，在Dependencies页面选择：
   - **Web** -> `Spring Web`

6. 点击 `Create`

---

### 第二步：在IntelliJ IDEA中打开项目

如果使用Spring Initializr网站创建的项目：

1. 打开IntelliJ IDEA

2. 点击 `File` -> `Open...`

3. 选择解压后的项目文件夹（包含 `pom.xml` 的目录）

4. 点击 `OK`，IDEA会自动识别为Maven项目

5. 等待IDEA下载依赖（右下角会显示进度）

初始项目结构：
```
todo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/zjgsu/todo/
│   │   │       └── TodoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/zjgsu/todo/
│               └── TodoApplicationTests.java
├── pom.xml
└── mvnw (Maven Wrapper)
```

---

### 第三步：创建实体类

实体类用于表示业务数据模型。

#### 3.1 创建 `model` 包

1. 在项目视图中，右键点击 `src/main/java/com/zjgsu/todo`
2. 选择 `New` -> `Package`
3. 输入包名：`model`
4. 点击 `OK`

#### 3.2 创建User实体类

1. 右键点击 `model` 包
2. 选择 `New` -> `Java Class`
3. 输入类名：`User`
4. 添加以下代码：

```java
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
```

**💡 小提示**：可以使用IDEA快捷键生成Getter/Setter
- 在类中按 `Alt + Insert` (Windows/Linux) 或 `Cmd + N` (Mac)
- 选择 `Getter and Setter`
- 选择所有字段，点击 `OK`

#### 3.3 创建Todo实体类

同样的方式，在 `model` 包中创建 `Todo.java`：

```java
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
```

---

### 第四步：创建统一响应格式

RESTful API应该返回统一格式的响应，方便客户端处理。

#### 4.1 创建 `common` 包

1. 右键点击 `com/zjgsu/todo`
2. 选择 `New` -> `Package`
3. 输入：`common`

#### 4.2 创建ApiResponse类

在 `common` 包中创建 `ApiResponse.java`：

```java
package com.zjgsu.todo.common;

import java.time.LocalDateTime;

/**
 * 统一API响应格式
 */
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 成功响应 - 带数据
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // 成功响应 - 无数据
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    // 创建成功响应
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Created", data);
    }

    // 错误响应
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // 客户端错误
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null);
    }

    // 未找到
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null);
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
```

#### 4.3 创建 `exception` 包和自定义异常

1. 创建 `exception` 包

2. 在 `exception` 包中创建 `ResourceNotFoundException.java`：

```java
package com.zjgsu.todo.exception;

/**
 * 资源未找到异常
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}
```

---

### 第五步：创建Service层

Service层包含业务逻辑。为了简化演示，我们使用内存存储数据。

#### 5.1 创建 `service` 包

右键点击 `com/zjgsu/todo` -> `New` -> `Package` -> 输入 `service`

#### 5.2 创建UserService

在 `service` 包中创建 `UserService.java`：

```java
package com.zjgsu.todo.service;

import com.zjgsu.todo.exception.ResourceNotFoundException;
import com.zjgsu.todo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务层
 * 使用内存存储，演示RESTful API
 */
@Service
public class UserService {
    // 使用内存存储
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public UserService() {
        // 初始化一些测试数据
        createUser(new User(null, "张三", "zhangsan@example.com"));
        createUser(new User(null, "李四", "lisi@example.com"));
    }

    /**
     * 获取所有用户
     */
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * 根据ID查找用户
     */
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * 创建用户
     */
    public User createUser(User user) {
        Long id = idCounter.getAndIncrement();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    /**
     * 更新用户
     */
    public User updateUser(Long id, User user) {
        if (!users.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        user.setId(id);
        users.put(id, user);
        return user;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        users.remove(id);
        return true;
    }

    /**
     * 检查用户是否存在
     */
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}
```

**📝 代码说明**：
- `@Service` 注解标记这是一个服务类，Spring会自动管理
- `ConcurrentHashMap` 用于线程安全的内存存储
- `AtomicLong` 用于生成唯一ID
- 构造函数中初始化了两个测试用户

#### 5.3 创建TodoService

在 `service` 包中创建 `TodoService.java`：

```java
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
```

---

### 第六步：创建Controller层

Controller层处理HTTP请求并调用Service层。

#### 6.1 创建 `controller` 包

右键点击 `com/zjgsu/todo` -> `New` -> `Package` -> 输入 `controller`

#### 6.2 创建UserController

在 `controller` 包中创建 `UserController.java`：

```java
package com.zjgsu.todo.controller;

import com.zjgsu.todo.common.ApiResponse;
import com.zjgsu.todo.model.User;
import com.zjgsu.todo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理Controller
 * 演示RESTful API的CRUD操作
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取所有用户
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * 根据ID获取用户
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("User not found with id: " + id)));
    }

    /**
     * 创建用户
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    /**
     * 更新用户
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    /**
     * 删除用户
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
}
```

**📝 代码说明**：
- `@RestController` = `@Controller` + `@ResponseBody`，表示返回JSON
- `@RequestMapping("/api/users")` 定义基础URL路径
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` 分别对应HTTP方法
- `@PathVariable` 获取URL路径中的参数
- `@RequestBody` 将请求体JSON转换为Java对象

#### 6.3 创建TodoController

在 `controller` 包中创建 `TodoController.java`：

```java
package com.zjgsu.todo.controller;

import com.zjgsu.todo.common.ApiResponse;
import com.zjgsu.todo.model.Todo;
import com.zjgsu.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo管理Controller
 * 演示RESTful API的CRUD操作和资源嵌套
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * 获取所有Todo
     * GET /api/todos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Todo>>> getAllTodos(
            @RequestParam(required = false) Long userId) {
        List<Todo> todos;
        if (userId != null) {
            todos = todoService.findByUserId(userId);
        } else {
            todos = todoService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.success(todos));
    }

    /**
     * 根据ID获取Todo
     * GET /api/todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> getTodoById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(todo -> ResponseEntity.ok(ApiResponse.success(todo)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Todo not found with id: " + id)));
    }

    /**
     * 创建Todo
     * POST /api/todos
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Todo>> createTodo(@RequestBody Todo todo) {
        Todo created = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    /**
     * 更新Todo
     * PUT /api/todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> updateTodo(
            @PathVariable Long id,
            @RequestBody Todo todo) {
        Todo updated = todoService.updateTodo(id, todo);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    /**
     * 删除Todo
     * DELETE /api/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok(ApiResponse.success("Todo deleted successfully"));
    }

    /**
     * 切换Todo完成状态
     * PATCH /api/todos/{id}/toggle
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Todo>> toggleComplete(@PathVariable Long id) {
        Todo updated = todoService.toggleComplete(id);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }
}
```

**📝 新知识点**：
- `@RequestParam` 用于获取查询参数，如 `/api/todos?userId=1`
- `@PatchMapping` 用于部分更新操作

#### 6.4 创建UserTodoController（演示资源嵌套）

在 `controller` 包中创建 `UserTodoController.java`：

```java
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
```

**📝 资源嵌套说明**：
- URL路径 `/api/users/{userId}/todos` 表示"用户的Todo"
- 这种设计展示了资源之间的从属关系
- RESTful设计中，资源嵌套不应超过2层

---

### 第七步：创建全局异常处理

统一处理所有Controller抛出的异常。

在 `exception` 包中创建 `GlobalExceptionHandler.java`：

```java
package com.zjgsu.todo.exception;

import com.zjgsu.todo.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 * 统一处理API异常，返回标准格式的错误响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(ex.getMessage()));
    }

    /**
     * 处理Spring MVC的资源未找到异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Resource not found: " + ex.getResourcePath()));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(ex.getMessage()));
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Internal server error: " + ex.getMessage()));
    }
}
```

**📝 代码说明**：
- `@RestControllerAdvice` 全局异常处理注解
- `@ExceptionHandler` 指定要处理的异常类型
- 所有Controller抛出的异常都会被这里捕获并统一处理

---

### 第八步：配置应用

#### 8.1 配置application.yml

1. 删除默认的 `application.properties` 文件：
   - 右键点击 `src/main/resources/application.properties`
   - 选择 `Delete`

2. 创建 `application.yml`：
   - 右键点击 `src/main/resources`
   - 选择 `New` -> `File`
   - 输入文件名：`application.yml`
   - 添加以下内容：

```yaml
server:
  port: 8080

spring:
  application:
    name: todo-api

# 日志配置
logging:
  level:
    com.zjgsu.todo: INFO
    org.springframework.web: INFO
```

**📝 配置说明**：
- `server.port` 设置应用端口为8080
- `spring.application.name` 设置应用名称
- `logging.level` 配置日志级别

---

### 第九步：运行项目

#### 方式一：使用IntelliJ IDEA运行（推荐）

1. 找到 `TodoApplication.java` 主类
2. 点击类名左侧的绿色三角形 ▶️
3. 选择 `Run 'TodoApplication'`

或者：
- 点击顶部工具栏的运行按钮 ▶️
- 或按快捷键 `Shift + F10` (Windows/Linux) 或 `Ctrl + R` (Mac)

#### 方式二：使用Maven命令运行

1. 打开IDEA底部的 `Terminal` 面板
2. 运行命令：

```bash
./mvnw spring-boot:run
```

Windows用户使用：
```cmd
mvnw.cmd spring-boot:run
```

#### 验证启动成功

看到以下日志说明启动成功：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

...
Started TodoApplication in 2.345 seconds
```

应用现在运行在 `http://localhost:8080`

---

### 第十步：测试API

#### 10.1 使用浏览器测试GET请求

打开浏览器，访问：
- http://localhost:8080/api/users
- http://localhost:8080/api/todos

你应该能看到JSON格式的响应数据。

#### 10.2 使用IntelliJ IDEA的HTTP Client测试

1. 在项目根目录创建 `api-test.http` 文件：
   - 右键点击项目根目录
   - 选择 `New` -> `File`
   - 输入文件名：`api-test.http`

2. 添加以下测试代码：

```http
### 获取所有用户
GET http://localhost:8080/api/users

### 获取单个用户
GET http://localhost:8080/api/users/1

### 创建新用户
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "王五",
  "email": "wangwu@example.com"
}

### 更新用户
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
  "username": "张三(已更新)",
  "email": "zhangsan.new@example.com"
}

### 获取所有Todo
GET http://localhost:8080/api/todos

### 获取用户1的所有Todo
GET http://localhost:8080/api/users/1/todos

### 创建Todo
POST http://localhost:8080/api/todos
Content-Type: application/json

{
  "title": "新任务",
  "description": "任务描述",
  "userId": 1
}

### 切换Todo完成状态（演示PATCH）
PATCH http://localhost:8080/api/todos/1/toggle

### 删除Todo
DELETE http://localhost:8080/api/todos/1

### 测试404错误
GET http://localhost:8080/api/users/999
```

3. 点击每个请求左侧的绿色三角形 ▶️ 执行请求
4. 查看右侧的响应结果

#### 10.3 使用Postman测试

##### 方式一：导入OpenAPI文件（推荐）

本项目提供了完整的OpenAPI 3.0规范文件 `openapi.yaml`，可以一键导入所有API接口。

1. 下载安装Postman：https://www.postman.com/downloads/

2. 导入OpenAPI文件：
   - 打开Postman
   - 点击左上角 `Import` 按钮
   - 选择 `File` 标签
   - 点击 `Upload Files`
   - 选择项目根目录下的 `openapi.yaml` 文件
   - 点击 `Import`

3. Postman会自动创建：
   - 完整的API接口集合（Collection）
   - 所有请求的URL、方法、请求体示例
   - 自动分组（User、Todo、User-Todo）
   - 完整的接口说明文档

4. 使用导入的接口：
   - 在左侧集合中选择任意接口
   - 点击 `Send` 发送请求
   - 查看响应结果

**💡 提示**：导入后，Postman会自动设置好所有请求参数和示例数据，无需手动配置！

##### 方式二：手动创建请求

如果你想手动学习，也可以逐个创建请求：

1. 创建新请求：
   - 点击 `+` 创建新标签
   - 选择HTTP方法（GET, POST, PUT, DELETE, PATCH）
   - 输入URL，如：`http://localhost:8080/api/users`
   - 对于POST/PUT请求，在 `Body` 选项卡选择 `raw` 和 `JSON`，输入JSON数据
   - 点击 `Send` 发送请求

2. 建议测试顺序：
   - GET `/api/users` - 查看所有用户
   - POST `/api/users` - 创建新用户
   - GET `/api/users/3` - 获取新创建的用户
   - PUT `/api/users/3` - 更新用户
   - GET `/api/todos` - 查看所有Todo
   - POST `/api/todos` - 创建新Todo
   - PATCH `/api/todos/1/toggle` - 切换完成状态
   - DELETE `/api/todos/1` - 删除Todo

#### 10.4 使用curl命令测试

如果你熟悉命令行，也可以使用curl：

```bash
# 获取所有用户
curl http://localhost:8080/api/users

# 创建新用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"王五","email":"wangwu@example.com"}'
```

---

## 完整API文档

### 用户管理 API

| 方法 | URL | 说明 | 请求体 | 状态码 |
|------|-----|------|--------|--------|
| GET | `/api/users` | 获取所有用户 | - | 200 |
| GET | `/api/users/{id}` | 获取单个用户 | - | 200/404 |
| POST | `/api/users` | 创建用户 | User JSON | 201 |
| PUT | `/api/users/{id}` | 更新用户 | User JSON | 200/404 |
| DELETE | `/api/users/{id}` | 删除用户 | - | 200/404 |

### Todo管理 API

| 方法 | URL | 说明 | 请求体 | 状态码 |
|------|-----|------|--------|--------|
| GET | `/api/todos` | 获取所有Todo | - | 200 |
| GET | `/api/todos?userId={id}` | 按用户筛选Todo | - | 200 |
| GET | `/api/todos/{id}` | 获取单个Todo | - | 200/404 |
| POST | `/api/todos` | 创建Todo | Todo JSON | 201 |
| PUT | `/api/todos/{id}` | 更新Todo | Todo JSON | 200/404 |
| PATCH | `/api/todos/{id}/toggle` | 切换完成状态 | - | 200/404 |
| DELETE | `/api/todos/{id}` | 删除Todo | - | 200/404 |

### 资源嵌套 API

| 方法 | URL | 说明 | 请求体 | 状态码 |
|------|-----|------|--------|--------|
| GET | `/api/users/{userId}/todos` | 获取用户的Todo | - | 200/404 |
| POST | `/api/users/{userId}/todos` | 为用户创建Todo | Todo JSON | 201/404 |

### 请求体示例

**创建用户**：
```json
{
  "username": "张三",
  "email": "zhangsan@example.com"
}
```

**创建Todo**：
```json
{
  "title": "学习Spring Boot",
  "description": "完成RESTful API教程",
  "userId": 1
}
```

### 响应格式

**成功响应**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "username": "张三",
    "email": "zhangsan@example.com",
    "createdAt": "2025-10-13T10:00:00"
  },
  "timestamp": "2025-10-13T10:00:00"
}
```

**错误响应**：
```json
{
  "code": 404,
  "message": "User not found with id: 999",
  "data": null,
  "timestamp": "2025-10-13T10:00:00"
}
```

---

## 学习要点

### RESTful API设计原则

本项目演示了以下RESTful最佳实践：

#### 1. 资源导向设计
- ✅ 使用名词而非动词：`/api/users` 而非 `/api/getUsers`
- ✅ 使用复数形式：`/api/users` 而非 `/api/user`
- ✅ 资源嵌套：`/api/users/{id}/todos` 表示资源关系

#### 2. 标准HTTP方法
- **GET** - 查询资源（幂等、安全）
- **POST** - 创建资源（非幂等）
- **PUT** - 更新/替换资源（幂等）
- **PATCH** - 部分更新资源
- **DELETE** - 删除资源（幂等）

#### 3. 正确使用HTTP状态码
- **200 OK** - 成功（GET、PUT）
- **201 Created** - 创建成功（POST）
- **204 No Content** - 成功无内容（DELETE）
- **400 Bad Request** - 客户端请求错误
- **404 Not Found** - 资源不存在
- **500 Internal Server Error** - 服务器错误

#### 4. 统一响应格式
所有API返回统一的JSON格式，包含：
- `code` - 状态码
- `message` - 消息
- `data` - 数据
- `timestamp` - 时间戳

#### 5. 幂等性
- **幂等操作**：GET、PUT、DELETE - 多次执行结果相同
- **非幂等操作**：POST - 每次执行都创建新资源

#### 6. 全局异常处理
使用 `@RestControllerAdvice` 统一处理异常，返回一致的错误格式。

---

## 常见问题

### 1. 端口被占用

**错误信息**：
```
Web server failed to start. Port 8080 was already in use.
```

**解决方法**：
- 方式1：修改 `application.yml` 中的端口号
- 方式2：停止占用8080端口的程序

### 2. Maven依赖下载慢

**解决方法**：配置国内Maven镜像

编辑 `pom.xml`，在 `<project>` 标签内添加：

```xml
<repositories>
    <repository>
        <id>aliyun</id>
        <url>https://maven.aliyun.com/repository/public</url>
    </repository>
</repositories>
```

### 3. IDEA无法识别Spring注解

**解决方法**：
1. 确保IDEA已安装Spring插件
2. 右键点击项目 -> `Maven` -> `Reload Project`
3. `File` -> `Invalidate Caches / Restart`

### 4. 404 Not Found

**检查项**：
- 应用是否启动成功？
- URL是否正确？（注意路径和端口）
- Controller的 `@RequestMapping` 路径是否正确？

### 5. JSON解析错误

**常见原因**：
- 请求头缺少 `Content-Type: application/json`
- JSON格式错误（缺少引号、逗号等）
- 字段名不匹配

---

## 项目扩展建议

完成基础项目后，可以尝试以下扩展：

1. **添加数据验证** - 使用 `@Valid` 和验证注解
2. **添加Swagger文档** - 自动生成API文档
3. **集成数据库** - 使用Spring Data JPA
4. **添加分页** - 实现分页查询
5. **添加日志** - 使用SLF4J记录日志
6. **单元测试** - 编写Controller和Service的测试
7. **添加认证** - 使用Spring Security

---

## 总结

恭喜！你已经完成了一个完整的RESTful API项目。通过这个项目，你应该掌握了：

- ✅ Spring Boot项目的创建和结构
- ✅ RESTful API的设计原则
- ✅ Controller、Service、Model的分层架构
- ✅ HTTP方法和状态码的正确使用
- ✅ 统一响应格式和异常处理
- ✅ 使用IntelliJ IDEA开发Spring Boot应用

这是一个很好的起点，继续学习并实践，你会成为一名优秀的后端开发工程师！

---

## 参考资源

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [RESTful API设计指南](https://restfulapi.net/)
- [HTTP状态码](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status)
- [IntelliJ IDEA官方文档](https://www.jetbrains.com/help/idea/)
