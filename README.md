# Todo RESTful API Demo - 从零开始教程

![Version](https://img.shields.io/badge/version-1.2.1-blue.svg)
![Java](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)
![Docker](https://img.shields.io/badge/docker-ghcr.io-2496ED?logo=docker&logoColor=white)

一个简洁的Spring Boot项目，用于演示RESTful API的设计和实现。本教程将手把手教你从零开始构建这个项目。

> **注意**: 如果 Docker badge 显示 "invalid"，请按照下面的说明将 GitHub Package 设置为公开。

## 📋 目录

- [项目说明](#项目说明)
- [技术栈](#技术栈)
- [使用 Docker Compose 运行（推荐）](#使用-docker-compose-运行推荐)
- [Docker 使用与国内镜像加速（中国大陆）](#docker-使用与国内镜像加速中国大陆)
- [环境准备](#环境准备)
- [数据库配置](#数据库配置)
  - [开发环境（H2内存数据库）](#开发环境h2内存数据库)
  - [生产环境（MySQL数据库）](#生产环境mysql数据库)
  - [数据库配置参数说明](#数据库配置参数说明)
  - [常见数据库问题](#常见数据库问题)
- [从零开始构建项目](#从零开始构建项目)
  - [第一步：创建Spring Boot项目](#第一步创建spring-boot项目)
  - [第二步：在IntelliJ IDEA中打开项目](#第二步在intellij-idea中打开项目)
  - [第三步：创建实体类](#第三步创建实体类)
  - [第四步：创建统一响应格式](#第四步创建统一响应格式)
  - [第五步：创建Service层](#第五步创建service层)
  - [第六步：创建Controller层](#第六步创建controller层)
  - [第七步：创建全局异常处理](#第七步创建全局异常处理)
  - [第八步：配置数据库](#第八步配置数据库)
  - [第九步：运行项目](#第九步运行项目)
  - [第十步：测试API](#第十步测试api)
- [完整API文档](#完整api文档)
- [学习要点](#学习要点)
- [项目扩展建议](#项目扩展建议)
- [CI/CD - 自动发布 Docker 镜像](#cicd---自动发布-docker-镜像)

---

## 项目说明

本项目实现了用户(User)和待办事项(Todo)的完整CRUD操作，展示了：

- ✅ RESTful API设计原则
- ✅ 统一的响应格式
- ✅ 正确使用HTTP状态码
- ✅ 资源嵌套关系
- ✅ 全局异常处理
- ✅ JPA/Hibernate数据持久化
- ✅ MySQL数据库集成
- ✅ 多环境配置（开发/生产）

## 技术栈

本项目使用的技术栈版本信息请参考：[课程技术栈版本说明](../../README.md#课程技术栈版本)

核心技术：

- **Spring Boot** - Web应用框架
- **Java** - 主要编程语言
- **Maven** - 项目构建工具
- **IntelliJ IDEA** - 推荐的集成开发环境
- **MySQL** - 生产环境数据库
- **H2 Database** - 开发环境内存数据库

## 使用 Docker Compose 运行（推荐）

无需本地安装 MySQL，使用 Docker 运行完整环境：

1) 确保已安装以下软件：
   - JDK 17+ 和 Maven（或使用项目自带的 `./mvnw`）
   - Docker 与 Docker Compose（Docker Desktop 或 docker cli）

2) **先在本地构建 JAR 包**：

```bash
# 使用 Maven Wrapper（推荐）
./mvnw clean package -DskipTests

# 或使用本地 Maven
mvn clean package -DskipTests
```

3) 在项目根目录执行 Docker Compose：

```bash
# 构建镜像并启动服务
docker compose up -d --build

# 查看服务状态
docker compose ps

# 查看应用日志
docker compose logs -f app
```

3) 访问接口

- 应用地址：http://localhost:8080
- 示例：`GET http://localhost:8080/api/users`

4) 数据持久化与初始化

- MySQL 使用命名卷 `mysql_data` 持久化数据
- 首次启动会自动执行 `src/main/resources/db/init.sql` 初始化库表与示例数据

5) 关闭与清理

```bash
# 停止
docker compose down

# 停止并清理数据卷（会删除数据库数据）
docker compose down -v
```

6) 常见问题

- **JAR 文件未找到**：确保先执行 `./mvnw clean package -DskipTests` 构建 JAR
- 端口被占用：修改 `docker-compose.yml` 中映射端口（8080、3306）
- 首次启动 app 失败：可能是 MySQL 尚未就绪，Compose 已配置健康检查与依赖，稍等片刻或继续查看日志
- 使用自定义数据库账号/密码：在 `docker-compose.yml` 中修改 `MYSQL_USER`/`MYSQL_PASSWORD`，同时更新 `app` 服务中的 `DB_USERNAME`/`DB_PASSWORD`

环境变量说明（app 服务）：

- `SPRING_PROFILES_ACTIVE=prod`：使用 `application-prod.yml`
- `DB_URL=jdbc:mysql://db:3306/todo_db?...`：连接 Compose 网络内的 `db` 服务
- `DB_USERNAME`、`DB_PASSWORD`：数据库凭据

### 仅构建 Docker 镜像（不使用 Compose）

如需单独构建镜像：

```bash
# 1. 先构建 JAR
./mvnw clean package -DskipTests

# 2. 构建 Docker 镜像
docker build -t zjgsu/todo:latest .

# 3. 运行容器（需要自行配置数据库连接）
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/todo_db \
  -e DB_USERNAME=todo_user \
  -e DB_PASSWORD=todo_password \
  zjgsu/todo:latest
```

镜像基于 Java 25（Temurin）JRE；如你的环境仅支持 Java 17，可将 `pom.xml` 的 `<java.version>` 与 `Dockerfile` 的基础镜像同步调整。

---

## Docker 使用与国内镜像加速（中国大陆）

在中国大陆网络环境下，直接从 Docker Hub 拉取镜像常遇到超时/限速。下面提供三种可靠方案，任选其一或组合使用。

### 1) 配置 Docker 镜像加速器（推荐）

使用云厂商提供的官方“镜像加速器”（与账号绑定，稳定性最好）：

- 阿里云 ACR：控制台 -> 容器镜像服务 -> 镜像工具 -> 镜像加速器（复制“专属加速器地址”）
- 腾讯云 TCR：控制台 -> TCR -> 管理中心 -> 镜像加速域名
- 华为云 SWR：控制台 -> SWR -> 工具 -> 镜像加速

将获取到的加速器地址写入 Docker 守护进程配置 `/etc/docker/daemon.json`（如文件不存在则新建）：

```json
{
    "registry-mirrors": [
        "https://<你的阿里云加速器ID>.mirror.aliyuncs.com",
        "https://mirror.ccs.tencentyun.com",
        "https://docker.mirrors.sjtug.sjtu.edu.cn"
    ]
}
```

然后重启 Docker 服务：

```bash
sudo systemctl daemon-reload
sudo systemctl restart docker
```

验证是否生效：

```bash
docker info | grep -i "Registry Mirrors" -A 3
```

生效后，直接执行项目根目录的：

```bash
docker compose up -d --build
```

即可自动拉取和构建本项目所需镜像。

提示：近年来高校公共镜像（如 USTC/TUNA/BFSU）对 Docker Hub 的镜像服务多已停止或不稳定，建议优先使用云厂商个人加速器。

### 2) 使用 DaoCloud Hub 直连 Docker Hub（无需全局配置）

如果你只想在本项目中替换单个镜像源，可将镜像名改为 `m.daocloud.io/docker.io/<原镜像>` 的形式。例如：

- `mysql:8.4` → `m.daocloud.io/docker.io/mysql:8.4`
- `eclipse-temurin:25-jre` → `m.daocloud.io/docker.io/eclipse-temurin:25-jre`
- `hello-world` → `m.daocloud.io/docker.io/hello-world`

用法示例：

- `docker-compose.yml` 中的 `db.image` 可替换为 `m.daocloud.io/docker.io/mysql:8.4`
- `Dockerfile` 中的 `FROM` 可替换为上述 DaoCloud 前缀的镜像名

如果不想改动现有文件，可新建一个 `docker-compose.override.yml` 放在项目根目录（Compose 会自动合并）：

```yaml
services:
    db:
        image: m.daocloud.io/docker.io/mysql:8.4
```

然后照常运行：

```bash
docker compose up -d --build
```

### 3) 设置网络代理（可选）

如果你所在的网络有 HTTP/HTTPS 代理，可以为 Docker 守护进程或客户端配置代理，以提升下载成功率：

- 客户端级别（`~/.docker/config.json`）：

```json
{
    "proxies": {
        "default": {
            "httpProxy": "http://127.0.0.1:7890",
            "httpsProxy": "http://127.0.0.1:7890",
            "noProxy": "localhost,127.0.0.1"
        }
    }
}
```

- 守护进程级别（systemd）：创建 `/etc/systemd/system/docker.service.d/proxy.conf`，内容示例：

```ini
[Service]
Environment="HTTP_PROXY=http://127.0.0.1:7890" "HTTPS_PROXY=http://127.0.0.1:7890" "NO_PROXY=localhost,127.0.0.1"
```

应用并重启：

```bash
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 本项目涉及的镜像一览

- 运行阶段：`eclipse-temurin:25-jre`
- 数据库：`mysql:8.4`

**注意**：现在项目改为本地构建，不再需要 Maven 镜像。在运行 `docker compose up` 前，需先执行 `./mvnw clean package -DskipTests` 构建 JAR。

若需要手动预拉镜像（先下载再构建），可执行：

```bash
docker pull eclipse-temurin:25-jre
docker pull mysql:8.4
```

国内手动下载并重命名（DaoCloud 源）：

```bash
# 1) 从 DaoCloud 拉取镜像到本地
docker pull m.daocloud.io/docker.io/eclipse-temurin:25-jre
docker pull m.daocloud.io/docker.io/mysql:8.4

# 2) 打上官方镜像名的 tag，供 Dockerfile/Compose 按原名使用
docker tag m.daocloud.io/docker.io/eclipse-temurin:25-jre eclipse-temurin:25-jre
docker tag m.daocloud.io/docker.io/mysql:8.4 mysql:8.4

# 3) 可选：删除带前缀的镜像（节省空间）
docker rmi m.daocloud.io/docker.io/eclipse-temurin:25-jre \
           m.daocloud.io/docker.io/mysql:8.4 || true
```


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
│   │   │       ├── TodoApplication.java
│   │   │       ├── common/          # 通用类
│   │   │       ├── controller/       # 控制器层
│   │   │       ├── exception/        # 异常处理
│   │   │       ├── model/            # 实体类
│   │   │       ├── repository/       # 数据访问层
│   │   │       └── service/          # 业务逻辑层
│   │   └── resources/
│   │       ├── application.yml       # 主配置文件
│   │       ├── application-dev.yml   # 开发环境配置
│   │       ├── application-prod.yml  # 生产环境配置
│   │       └── db/
│   │           └── init.sql          # 数据库初始化脚本
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

Service层包含业务逻辑。

::: {.callout-note}
**注意**：本步骤先使用内存存储演示基本功能，后续会在[第八步](#第八步配置数据库)升级为数据库存储。
:::

#### 5.1 创建 `service` 包

右键点击 `com/zjgsu/todo` -> `New` -> `Package` -> 输入 `service`

#### 5.2 创建UserService

在 `service` 包中创建 `UserService.java`（使用内存存储的简化版本）：

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

@Service
public class UserService {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public UserService() {
        // 初始化测试数据
        createUser(new User(null, "张三", "zhangsan@example.com"));
        createUser(new User(null, "李四", "lisi@example.com"));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User createUser(User user) {
        Long id = idCounter.getAndIncrement();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public User updateUser(Long id, User user) {
        if (!users.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public boolean deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        users.remove(id);
        return true;
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}
```

#### 5.3 创建TodoService

在 `service` 包中创建 `TodoService.java`（使用内存存储的简化版本）：

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

@Service
public class TodoService {
    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final UserService userService;

    public TodoService(UserService userService) {
        this.userService = userService;
        // 初始化测试数据
        createTodo(new Todo(null, "学习Spring Boot", "完成基础教程", 1L));
        createTodo(new Todo(null, "实现RESTful API", "创建CRUD接口", 1L));
    }

    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    public List<Todo> findByUserId(Long userId) {
        return todos.values().stream()
                .filter(todo -> todo.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todos.get(id));
    }

    public Todo createTodo(Todo todo) {
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }
        Long id = idCounter.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    public Todo updateTodo(Long id, Todo todo) {
        if (!todos.containsKey(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    public boolean deleteTodo(Long id) {
        if (!todos.containsKey(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        todos.remove(id);
        return true;
    }

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

**📝 代码说明**：
- 此版本使用 `ConcurrentHashMap` 进行内存存储
- 在[第八步](#第八步配置数据库)中，我们会升级为使用JPA和数据库
- `@Service` 注解让Spring自动管理这个类

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

### 第八步：配置数据库

本步骤将添加数据库支持，实现数据持久化。

#### 8.1 添加数据库依赖

1. 打开 `pom.xml` 文件

2. 在 `<dependencies>` 标签内添加以下依赖：

```xml
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL驱动 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- H2数据库（开发环境） -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

3. 点击IDEA右上角的 `M` 图标（Maven）或右键点击 `pom.xml` -> `Maven` -> `Reload Project`

#### 8.2 为实体类添加JPA注解

修改 `User.java`，添加JPA注解：

```java
package com.zjgsu.todo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters...
}
```

修改 `Todo.java`：

```java
package com.zjgsu.todo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters...
}
```

**📝 JPA注解说明**：
- `@Entity` - 标记为JPA实体类
- `@Table(name = "users")` - 指定数据库表名
- `@Id` - 标记主键字段
- `@GeneratedValue(strategy = IDENTITY)` - 自增主键
- `@Column` - 配置列属性（长度、非空、唯一等）
- `@PrePersist` - 在保存前自动执行
- `@PreUpdate` - 在更新前自动执行

#### 8.3 创建Repository接口

1. 创建 `repository` 包：右键点击 `com/zjgsu/todo` -> `New` -> `Package` -> 输入 `repository`

2. 在 `repository` 包中创建 `UserRepository.java`：

```java
package com.zjgsu.todo.repository;

import com.zjgsu.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
}
```

3. 在 `repository` 包中创建 `TodoRepository.java`：

```java
package com.zjgsu.todo.repository;

import com.zjgsu.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    /**
     * 根据用户ID查找所有Todo
     */
    List<Todo> findByUserId(Long userId);

    /**
     * 根据用户ID和完成状态查找Todo
     */
    List<Todo> findByUserIdAndCompleted(Long userId, Boolean completed);

    /**
     * 根据完成状态查找Todo
     */
    List<Todo> findByCompleted(Boolean completed);

    /**
     * 根据标题模糊查询
     */
    List<Todo> findByTitleContaining(String keyword);
}
```

**📝 Spring Data JPA说明**：
- 继承 `JpaRepository<Entity, ID>` 即可获得基本的CRUD方法
- 按照命名规则定义方法，Spring会自动生成SQL：
  - `findBy...` - 查询
  - `existsBy...` - 判断是否存在
  - `countBy...` - 计数
  - `...And...` - AND条件
  - `...Or...` - OR条件
  - `...Containing` - 模糊查询

#### 8.4 修改Service层使用Repository

修改 `UserService.java`，从内存存储改为使用数据库：

```java
package com.zjgsu.todo.service;

import com.zjgsu.todo.exception.ResourceNotFoundException;
import com.zjgsu.todo.model.User;
import com.zjgsu.todo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        // 只在数据库为空时初始化测试数据
        if (userRepository.count() == 0) {
            createUser(new User(null, "张三", "zhangsan@example.com"));
            createUser(new User(null, "李四", "lisi@example.com"));
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        // 验证用户名和邮箱唯一性
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        user.setId(id);
        return userRepository.save(user);
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
        return true;
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
```

同样修改 `TodoService.java`：

```java
package com.zjgsu.todo.service;

import com.zjgsu.todo.exception.ResourceNotFoundException;
import com.zjgsu.todo.model.Todo;
import com.zjgsu.todo.repository.TodoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserService userService;

    public TodoService(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        // 只在数据库为空时初始化测试数据
        if (todoRepository.count() == 0) {
            createTodo(new Todo(null, "学习Spring Boot", "完成Spring Boot基础教程", 1L));
            createTodo(new Todo(null, "实现RESTful API", "创建用户和Todo的CRUD接口", 1L));
            createTodo(new Todo(null, "编写文档", "完善API文档", 2L));
        }
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public List<Todo> findByUserId(Long userId) {
        return todoRepository.findByUserId(userId);
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    @Transactional
    public Todo createTodo(Todo todo) {
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }
        return todoRepository.save(todo);
    }

    @Transactional
    public Todo updateTodo(Long id, Todo todo) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        if (todo.getUserId() != null && !userService.existsById(todo.getUserId())) {
            throw new ResourceNotFoundException("User", todo.getUserId());
        }
        todo.setId(id);
        return todoRepository.save(todo);
    }

    @Transactional
    public boolean deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        todoRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Todo toggleComplete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", id));
        todo.setCompleted(!todo.getCompleted());
        return todoRepository.save(todo);
    }
}
```

**📝 事务管理说明**：
- `@Transactional` - 标记需要事务管理的方法
- 查询操作不需要事务，增删改操作需要
- Spring会自动管理事务的提交和回滚

#### 8.5 配置数据库连接

1. 删除默认的 `application.properties` 文件

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

  # 数据源配置（MySQL）
  datasource:
    url: jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4&useUnicode=true
    username: todo_user
    password: todo_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  # JPA配置
  jpa:
    hibernate:
      ddl-auto: update  # 自动更新表结构
    show-sql: true      # 显示SQL语句
    properties:
      hibernate:
        format_sql: true  # 格式化SQL
        dialect: org.hibernate.dialect.MySQL8Dialect

# 日志配置
logging:
  level:
    com.zjgsu.todo: INFO
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

3. 创建 `application-dev.yml`（开发环境配置）：

```yaml
spring:
  # H2内存数据库配置
  datasource:
    url: jdbc:h2:mem:todo_db
    driver-class-name: org.h2.Driver
    username: sa
    password:

  # H2控制台
  h2:
    console:
      enabled: true
      path: /h2-console

  # JPA配置
  jpa:
    hibernate:
      ddl-auto: create-drop  # 每次启动重建表
    show-sql: true
    properties:
      hibernate:
        format_sql: true

# 日志配置
logging:
  level:
    com.zjgsu.todo: DEBUG
    org.springframework.web: DEBUG
```

4. 创建 `application-prod.yml`（生产环境配置）：

```yaml
spring:
  # 生产环境使用环境变量配置数据库
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4&useUnicode=true}
    username: ${DB_USERNAME:todo_user}
    password: ${DB_PASSWORD:todo_password}

  jpa:
    hibernate:
      ddl-auto: validate  # 生产环境只验证，不修改表结构
    show-sql: false       # 关闭SQL日志
    properties:
      hibernate:
        format_sql: false

# 日志配置
logging:
  level:
    com.zjgsu.todo: WARN
    org.springframework.web: WARN
```

**📝 配置说明**：

**ddl-auto 选项**：
- `create` - 每次启动创建新表（删除旧表）
- `create-drop` - 启动时创建，关闭时删除
- `update` - 更新表结构（推荐开发环境）
- `validate` - 仅验证表结构（推荐生产环境）
- `none` - 不做任何操作

**环境切换**：
- 开发环境：`./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`
- 生产环境：`./mvnw spring-boot:run -Dspring-boot.run.profiles=prod`
- 默认环境：`./mvnw spring-boot:run` (使用 application.yml)

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

## 数据库配置

项目支持两种数据库环境：

### 开发环境（H2内存数据库）

默认使用H2内存数据库，无需额外配置，启动即用：

```bash
# 使用开发环境配置启动
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

访问H2控制台：http://localhost:8080/h2-console

- **JDBC URL**: `jdbc:h2:mem:todo_db`
- **用户名**: `sa`
- **密码**: (留空)

### 生产环境（MySQL数据库）

#### 1. 安装MySQL

根据你的操作系统安装MySQL 8.0+：

- **Windows**: 下载 [MySQL Installer](https://dev.mysql.com/downloads/installer/)
- **macOS**: `brew install mysql`
- **Linux (Ubuntu/Debian)**: `sudo apt install mysql-server`

#### 2. 创建数据库和用户

使用项目提供的初始化脚本：

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source src/main/resources/db/init.sql
```

或手动执行以下SQL命令：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS todo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER IF NOT EXISTS 'todo_user'@'localhost' IDENTIFIED BY 'todo_password';

-- 授权
GRANT ALL PRIVILEGES ON todo_db.* TO 'todo_user'@'localhost';
FLUSH PRIVILEGES;

-- 使用数据库
USE todo_db;
```

初始化脚本会自动创建表结构并插入测试数据。

#### 3. 配置数据库连接

**方式一：修改配置文件**

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4&useUnicode=true
    username: todo_user
    password: todo_password
```

**方式二：使用环境变量（推荐生产环境）**

设置以下环境变量：

```bash
export DB_URL="jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4&useUnicode=true"
export DB_USERNAME="todo_user"
export DB_PASSWORD="todo_password"
```

然后使用生产配置启动：

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

#### 4. 验证数据库连接

启动应用后，检查日志：

```
Initialized JPA EntityManagerFactory for persistence unit 'default'
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

或使用测试接口：

```bash
curl http://localhost:8080/api/users
```

应该能看到预置的测试数据。

### 数据库配置参数说明

| 参数 | 开发环境 | 生产环境 | 说明 |
|------|----------|----------|------|
| `spring.jpa.hibernate.ddl-auto` | `create-drop` | `validate` | 开发环境每次启动重建表；生产环境仅验证 |
| `spring.jpa.show-sql` | `true` | `false` | 开发环境显示SQL；生产环境关闭以提高性能 |
| `spring.h2.console.enabled` | `true` | `false` | H2控制台仅开发环境启用 |
| `logging.level` | `DEBUG` | `WARN` | 开发环境详细日志；生产环境警告级别 |

### 常见数据库问题

#### 1. 连接被拒绝

```
Connection refused: localhost:3306
```

**解决方法**：
- 检查MySQL是否运行：`mysql --version` 或 `systemctl status mysql`
- 启动MySQL服务：`sudo systemctl start mysql`（Linux）或通过服务管理器启动（Windows）

#### 2. 用户认证失败

```
Access denied for user 'todo_user'@'localhost'
```

**解决方法**：
- 检查用户名和密码是否正确
- 重新创建用户和授权（参考上述SQL命令）
- MySQL 8.0需要使用 `mysql_native_password` 插件：
  ```sql
  ALTER USER 'todo_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'todo_password';
  ```

#### 3. 时区错误

```
The server time zone value 'CST' is unrecognized
```

**解决方法**：
在连接URL中添加时区参数：
```
jdbc:mysql://localhost:3306/todo_db?serverTimezone=Asia/Shanghai
```

#### 4. 字符编码问题

如果出现中文乱码，确保：
- 数据库使用 `utf8mb4` 字符集（支持完整 Unicode，包括 emoji）
- 连接URL包含 `characterEncoding=utf8mb4&useUnicode=true`
- MySQL配置文件（my.cnf）或启动参数设置：
  ```
  [mysqld]
  character-set-server=utf8mb4
  collation-server=utf8mb4_unicode_ci
  ```
- Docker Compose 中已配置：`--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci`

## 项目扩展建议

完成基础项目后，可以尝试以下扩展：

1. **添加数据验证** - 使用 `@Valid` 和验证注解
2. **添加Swagger文档** - 自动生成API文档
3. **添加分页** - 实现分页查询
4. **添加日志** - 使用SLF4J记录日志
5. **单元测试** - 编写Controller和Service的测试
6. **添加认证** - 使用Spring Security
7. **数据库迁移** - 使用Flyway或Liquibase管理数据库版本

---

## CI/CD - 自动发布 Docker 镜像

本项目配置了 GitHub Actions 工作流，可以在发布新版本时自动构建并发布 Docker 镜像到 GitHub Container Registry (ghcr.io)。

### 发布新版本的步骤

1. **更新版本号**
   
   编辑 `pom.xml`，更新版本号：
   ```xml
   <version>1.2.0</version>
   ```

2. **提交并推送代码**
   ```bash
   git add .
   git commit -m "chore: bump version to 1.2.0"
   git push origin main
   ```

3. **创建并推送 Git 标签**
   ```bash
   git tag v1.2.0
   git push origin v1.2.0
   ```

4. **在 GitHub 上创建 Release**
   - 访问仓库的 Releases 页面
   - 点击 "Create a new release"
   - 选择刚才创建的标签 `v1.2.0`
   - 填写发布说明
   - 点击 "Publish release"

5. **自动构建和发布**
   
   GitHub Actions 会自动：
   - 使用 Maven 构建 JAR 包
   - 构建 Docker 镜像
   - 推送镜像到 `ghcr.io/zjsu-ms/todo`
   - 自动生成多个标签：
     - `ghcr.io/zjsu-ms/todo:1.2.0`
     - `ghcr.io/zjsu-ms/todo:1.2`
     - `ghcr.io/zjsu-ms/todo:1`
     - `ghcr.io/zjsu-ms/todo:latest`

6. **设置包为公开（重要）**
   
   首次发布后，包默认是私有的。需要手动设置为公开：
   - 访问 https://github.com/zjsu-ms/todo/pkgs/container/todo
   - 点击右侧的 "Package settings"
   - 滚动到页面底部 "Danger Zone"
   - 点击 "Change visibility"
   - 选择 "Public"
   - 确认更改

### 使用发布的镜像

从 GitHub Container Registry 拉取镜像（需要先将包设置为公开）：

```bash
# 拉取最新版本
docker pull ghcr.io/zjsu-ms/todo:latest

# 拉取特定版本
docker pull ghcr.io/zjsu-ms/todo:1.2.0
```

在 `docker-compose.yml` 中使用发布的镜像：

```yaml
services:
  app:
    image: ghcr.io/zjsu-ms/todo:1.2.0
    # 或使用 latest 标签
    # image: ghcr.io/zjsu-ms/todo:latest
    restart: unless-stopped
    depends_on:
      db:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_URL: jdbc:mysql://db:3306/todo_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
      DB_USERNAME: todo
      DB_PASSWORD: todo_pass
      TZ: Asia/Shanghai
    ports:
      - "8080:8080"
```

### 国内用户拉取镜像加速

如果从 GitHub Container Registry 拉取镜像较慢，可以配置镜像代理或使用以下方法：

```bash
# 通过代理拉取
docker pull ghcr.io/zjsu-ms/todo:latest

# 重新打标签为本地名称
docker tag ghcr.io/zjsu-ms/todo:latest todo-app:latest
```

### 工作流配置

工作流文件位于 `.github/workflows/release.yml`，触发条件：
- 推送标签（格式：`v*.*.*`，如 `v1.2.0`）
- 发布 GitHub Release

工作流权限：
- 自动使用 `GITHUB_TOKEN`，无需额外配置
- 需要 `contents: read` 和 `packages: write` 权限

---

## 总结

恭喜！你已经完成了一个完整的RESTful API项目。通过这个项目，你应该掌握了：

- ✅ Spring Boot项目的创建和结构
- ✅ RESTful API的设计原则
- ✅ Controller、Service、Model的分层架构
- ✅ HTTP方法和状态码的正确使用
- ✅ 统一响应格式和异常处理
- ✅ 使用IntelliJ IDEA开发Spring Boot应用
- ✅ JPA/Hibernate实现数据持久化
- ✅ MySQL数据库集成与配置
- ✅ 多环境配置管理（开发/生产）

这是一个很好的起点，继续学习并实践，你会成为一名优秀的后端开发工程师！

---

## 参考资源

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [RESTful API设计指南](https://restfulapi.net/)
- [HTTP状态码](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status)
- [IntelliJ IDEA官方文档](https://www.jetbrains.com/help/idea/)
