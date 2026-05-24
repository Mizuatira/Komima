package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.entity.Evaluate;
import com.komima.entity.Task;
import com.komima.entity.User;
import com.komima.mapper.EvaluateMapper;
import com.komima.mapper.TaskMapper;
import com.komima.mapper.UserMapper;
import com.komima.service.EvaluateService;
import com.komima.service.TaskService;
import com.komima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EvaluateMapper evaluateMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        return ApiResponse.success(userService.getAll());
    }

    @PutMapping("/user/{id}/role")
    public ApiResponse<Void> updateRole(@PathVariable Integer id, @RequestParam Integer role) {
        userService.updateRole(id, role);
        return ApiResponse.success("角色更新成功", null);
    }

    @DeleteMapping("/user/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ApiResponse.success("用户已删除", null);
    }

    @GetMapping("/tasks")
    public ApiResponse<List<Task>> tasks() {
        return ApiResponse.success(taskService.listAll());
    }

    @DeleteMapping("/task/{id}")
    public ApiResponse<Void> deleteTask(@PathVariable Integer id) {
        taskMapper.deleteById(id);
        return ApiResponse.success("委托已删除", null);
    }

    @GetMapping("/evaluates")
    public ApiResponse<List<Evaluate>> evaluates() {
        return ApiResponse.success(evaluateMapper.selectAll());
    }

    @DeleteMapping("/evaluate/{id}")
    public ApiResponse<Void> deleteEvaluate(@PathVariable Integer id) {
        evaluateMapper.deleteById(id);
        return ApiResponse.success("评价已删除", null);
    }
}
