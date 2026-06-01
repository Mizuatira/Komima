package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.entity.User;
import com.komima.entity.Task;
import com.komima.service.UserService;
import com.komima.service.TaskService;
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

    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        return ApiResponse.success(userService.getAll());
    }

    @PutMapping("/user/{id}/role")
    public ApiResponse<Void> updateRole(@PathVariable Integer id, @RequestParam Integer role) {
        userService.updateRole(id, role);
        return ApiResponse.success("角色已更新", null);
    }

    @DeleteMapping("/user/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ApiResponse.success("已删除", null);
    }

    @GetMapping("/tasks")
    public ApiResponse<List<Task>> tasks() {
        return ApiResponse.success(taskService.listAll());
    }

    @DeleteMapping("/task/{id}")
    public ApiResponse<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ApiResponse.success("已删除", null);
    }
}
