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
