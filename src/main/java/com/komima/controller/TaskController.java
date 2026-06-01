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
    public ApiResponse<Task> publish(@Valid @RequestBody TaskDTO dto) {
        return ApiResponse.success("发布成功", taskService.publish(dto));
    }

    @GetMapping("/list")
    public ApiResponse<List<Task>> list(@RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) Integer category,
                                        @RequestParam(required = false) Integer hasReward,
                                        @RequestParam(required = false) Integer excludeUserId) {
        if (status != null || category != null || hasReward != null || excludeUserId != null) {
            return ApiResponse.success(taskService.listFiltered(status, category, hasReward, excludeUserId));
        }
        return ApiResponse.success(taskService.listAll());
    }

    @GetMapping("/list/user/{userId}")
    public ApiResponse<List<Task>> listByUser(@PathVariable Integer userId) {
        return ApiResponse.success(taskService.listByUserId(userId));
    }

    @GetMapping("/list/applicant/{applicantId}")
    public ApiResponse<List<Task>> listByApplicant(@PathVariable Integer applicantId) {
        return ApiResponse.success(taskService.listByApplicantId(applicantId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> getById(@PathVariable Integer id) {
        return ApiResponse.success(taskService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMyTask(@PathVariable Integer id, @RequestParam Integer userId) {
        taskService.deleteMyPendingTask(id, userId);
        return ApiResponse.success("已删除", null);
    }
}
