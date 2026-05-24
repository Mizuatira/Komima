# Komima - 校园委托对接系统

> Spring Boot 课程设计项目 · 让校园生活更便捷

## 功能概览

- **用户系统** — 注册 / 登录 / 个人中心（昵称、性别、学院、专业、联系方式）
- **委托发布** — 发布委托任务，填写标题和详情
- **任务接单** — 浏览委托列表，接受他人发布的委托
- **状态流转** — 未接单 → 已接单 → 已完成
- **评价系统** — 对已完成的委托进行评分和评价
- **管理员后台** — 用户管理、委托管理、角色切换
- **统一异常处理** — 全局异常捕获，统一 JSON 响应格式
- **AOP 请求日志** — 接口请求 / 响应 / 异常自动记录
- **数据库事务** — `@Transactional` 保证数据一致性
- **参数校验** — `@Valid` + Jakarta Validation

## 技术栈

| 层面 | 技术 |
|------|------|
| 框架 | Spring Boot 2.7 |
| 持久层 | MyBatis + MySQL |
| 前端 | Bootstrap 5 + Bootstrap Icons |
| 工具 | Lombok, Spring AOP, Maven Wrapper |
| 运行环境 | Java 17, MySQL |

## 项目结构

```
src/main/java/com/komima/
├── Application.java              # 启动类
├── config/WebLogAspect.java      # AOP 请求日志
├── controller/
│   ├── UserController.java       # 注册 / 登录 / 个人中心
│   ├── TaskController.java       # 委托 CRUD / 接单 / 状态变更
│   ├── EvaluateController.java   # 评价创建与查询
│   └── AdminController.java      # 管理员后台 API
├── dto/                          # 请求 DTO（含校验注解）
├── entity/                       # 实体类
├── enums/TaskStatus.java         # 委托状态枚举
├── exception/
│   ├── BusinessException.java    # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理
├── mapper/                       # MyBatis Mapper 接口
├── service/                      # 服务接口与实现
src/main/resources/
├── mapper/                       # MyBatis XML
├── static/                       # 前端页面
│   ├── index.html               # 首页（委托列表 + 后台面板 + 个人中心）
│   ├── login.html               # 登录 / 注册
│   ├── publish.html             # 发布委托
│   └── profile.html             # 个人中心（独立页）
└── application.example.yml      # 配置模板
```

## 快速开始

### 1. 准备数据库

```sql
CREATE DATABASE komima_task DEFAULT CHARSET utf8mb4;
```

### 2. 配置 application.yml

```bash
cp src/main/resources/application.example.yml src/main/resources/application.yml
```

修改数据库连接信息为你的 MySQL 账号密码。

### 3. 启动

```bash
./mvnw spring-boot:run        # macOS / Linux
mvnw.cmd spring-boot:run       # Windows
```

### 4. 访问

打开浏览器访问 `http://localhost:8080/login.html`

## 默认账号

| 用户名 | 密码 | 昵称 | 角色 |
|--------|------|------|------|
| `admin` | `admin` | 系统管理员 | 管理员 |
| `xiaoming` | `123456` | 小明 | 普通用户 |
| `xiaohong` | `123456` | 小红 | 普通用户 |
| `xiaogang` | `123456` | 小刚 | 普通用户 |

## API 概览

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/user/register` | 注册 |
| POST | `/api/user/login` | 登录 |
| PUT  | `/api/user/{id}/profile` | 更新个人资料 |
| POST | `/api/task/publish` | 发布委托 |
| GET  | `/api/task/list` | 委托列表 |
| POST | `/api/task/{id}/accept` | 接单 |
| POST | `/api/task/{id}/status` | 更新状态 |
| POST | `/api/evaluate/create` | 创建评价 |
| GET  | `/api/admin/users` | 管理用户列表 |
| GET  | `/api/admin/tasks` | 管理委托列表 |

所有 API 返回统一格式 `{ code: 200, message: "...", data: ... }`。
