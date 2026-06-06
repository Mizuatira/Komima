package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.dto.TaskDTO;
import com.komima.entity.Task;
import com.komima.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 任务控制器
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 发布任务
     * @param dto 任务信息
     * @return 发布的任务
     */
    @PostMapping("/publish")
    public ApiResponse<Task> publish(@Valid @RequestBody TaskDTO dto) {
        return ApiResponse.success("发布成功", taskService.publish(dto));
    }

    /**
     * 获取任务列表
     * @param status 状态筛选
     * @param category 分类筛选
     * @param hasReward 是否有报酬筛选
     * @param excludeUserId 排除的用户ID
     * @return 任务列表
     */
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

    /**
     * 获取用户发布的任务列表
     * @param userId 用户ID
     * @return 任务列表
     */
    @GetMapping("/list/user/{userId}")
    public ApiResponse<List<Task>> listByUser(@PathVariable Integer userId) {
        return ApiResponse.success(taskService.listByUserId(userId));
    }

    /**
     * 获取用户申请的任务列表
     * @param applicantId 申请人ID
     * @return 任务列表
     */
    @GetMapping("/list/applicant/{applicantId}")
    public ApiResponse<List<Task>> listByApplicant(@PathVariable Integer applicantId) {
        return ApiResponse.success(taskService.listByApplicantId(applicantId));
    }

    /**
     * 获取任务详情
     * @param id 任务ID
     * @return 任务详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Task> getById(@PathVariable Integer id) {
        return ApiResponse.success(taskService.getById(id));
    }

    /**
     * 删除用户自己的待接单任务
     * @param id 任务ID
     * @param userId 用户ID
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMyTask(@PathVariable Integer id, @RequestParam Integer userId) {
        taskService.deleteMyPendingTask(id, userId);
        return ApiResponse.success("已删除", null);
    }
}
