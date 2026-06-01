package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.dto.EvaluateDTO;
import com.komima.dto.UserProfileVO;
import com.komima.entity.Evaluate;
import com.komima.entity.Task;
import com.komima.entity.User;
import com.komima.service.EvaluateService;
import com.komima.service.TaskService;
import com.komima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/evaluate")
@CrossOrigin(origins = "*")
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ApiResponse<Evaluate> create(@Valid @RequestBody EvaluateDTO dto) {
        return ApiResponse.success("评价成功", evaluateService.create(dto));
    }

    @GetMapping("/task/{taskId}")
    public ApiResponse<List<Evaluate>> getByTask(@PathVariable Integer taskId) {
        return ApiResponse.success(evaluateService.getByTaskId(taskId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Evaluate>> getByUser(@PathVariable Integer userId) {
        return ApiResponse.success(evaluateService.getByUserId(userId));
    }

    @GetMapping("/user/{userId}/profile")
    public ApiResponse<UserProfileVO> getUserProfile(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        UserProfileVO vo = new UserProfileVO();
        vo.setUser(user);
        vo.setPublishedTasks(taskService.listByUserId(userId));
        vo.setAcceptedTasks(taskService.listByApplicantId(userId));
        vo.setEvaluations(evaluateService.getByUserId(userId));
        return ApiResponse.success(vo);
    }
}
