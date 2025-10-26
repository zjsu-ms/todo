#!/bin/bash

# Todo RESTful API 测试脚本
# 运行此脚本前，请确保应用已启动：./mvnw spring-boot:run

BASE_URL="http://localhost:8080"

echo "================================"
echo "Todo RESTful API 测试"
echo "================================"
echo ""

# 测试用户API
echo "1. 获取所有用户 (GET /api/users)"
curl -s -X GET $BASE_URL/api/users | jq '.'
echo ""
echo ""

echo "2. 获取单个用户 (GET /api/users/1)"
curl -s -X GET $BASE_URL/api/users/1 | jq '.'
echo ""
echo ""

echo "3. 创建新用户 (POST /api/users)"
curl -s -X POST $BASE_URL/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"王五","email":"wangwu@example.com"}' | jq '.'
echo ""
echo ""

echo "4. 更新用户 (PUT /api/users/1)"
curl -s -X PUT $BASE_URL/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"username":"张三(已更新)","email":"zhangsan.new@example.com"}' | jq '.'
echo ""
echo ""

# 测试Todo API
echo "5. 获取所有Todo (GET /api/todos)"
curl -s -X GET $BASE_URL/api/todos | jq '.'
echo ""
echo ""

echo "6. 获取单个Todo (GET /api/todos/1)"
curl -s -X GET $BASE_URL/api/todos/1 | jq '.'
echo ""
echo ""

echo "7. 创建新Todo (POST /api/todos)"
curl -s -X POST $BASE_URL/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"测试任务","description":"这是一个测试任务","userId":1}' | jq '.'
echo ""
echo ""

echo "8. 切换Todo完成状态 (PATCH /api/todos/1/toggle)"
curl -s -X PATCH $BASE_URL/api/todos/1/toggle | jq '.'
echo ""
echo ""

echo "9. 获取用户的所有Todo (GET /api/users/1/todos)"
curl -s -X GET $BASE_URL/api/users/1/todos | jq '.'
echo ""
echo ""

echo "10. 为用户创建Todo (POST /api/users/1/todos)"
curl -s -X POST $BASE_URL/api/users/1/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"用户专属任务","description":"通过嵌套路由创建的任务"}' | jq '.'
echo ""
echo ""

echo "11. 按用户ID筛选Todo (GET /api/todos?userId=1)"
curl -s -X GET "$BASE_URL/api/todos?userId=1" | jq '.'
echo ""
echo ""

echo "12. 删除Todo (DELETE /api/todos/1)"
curl -s -X DELETE $BASE_URL/api/todos/1 | jq '.'
echo ""
echo ""

echo "13. 尝试获取不存在的资源 (GET /api/users/999) - 演示404错误"
curl -s -X GET $BASE_URL/api/users/999 | jq '.'
echo ""
echo ""

echo "================================"
echo "测试完成!"
echo "================================"
