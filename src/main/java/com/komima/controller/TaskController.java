package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.dto.TaskDTO;
import com.komima.entity.Task;
import com.komima.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/publish")
    public ApiResponse<Task> publish(@Valid @RequestBody TaskDTO taskDTO) {
        Task task = taskService.publish(taskDTO);
        return ApiResponse.success("发布成功", task);
    }

    @GetMapping("/list")
    public ApiResponse<List<Task>> list() {
        return ApiResponse.success(taskService.listAll());
    }

    @GetMapping("/list/user/{userId}")
    public ApiResponse<List<Task>> listByUser(@PathVariable Integer userId) {
        return ApiResponse.success(taskService.listByUserId(userId));
    }

    @GetMapping("/list/receiver/{receiverId}")
    public ApiResponse<List<Task>> listByReceiver(@PathVariable Integer receiverId) {
        return ApiResponse.success(taskService.listByReceiverId(receiverId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> getById(@PathVariable Integer id) {
        return ApiResponse.success(taskService.getById(id));
    }

    @PostMapping("/{id}/accept")
    public ApiResponse<Void> accept(@PathVariable Integer id, @RequestParam Integer userId) {
        taskService.acceptTask(id, userId);
        return ApiResponse.success("接单成功", null);
    }

    @PostMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Integer id,
                                           @RequestParam Integer status,
                                           @RequestParam Integer userId) {
        taskService.updateStatus(id, status, userId);
        return ApiResponse.success("状态更新成功", null);
    }
}
