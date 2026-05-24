package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.dto.EvaluateDTO;
import com.komima.entity.Evaluate;
import com.komima.service.EvaluateService;
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

    @PostMapping("/create")
    public ApiResponse<Evaluate> create(@Valid @RequestBody EvaluateDTO evaluateDTO) {
        Evaluate evaluate = evaluateService.create(evaluateDTO);
        return ApiResponse.success("评价成功", evaluate);
    }

    @GetMapping("/task/{taskId}")
    public ApiResponse<List<Evaluate>> getByTask(@PathVariable Integer taskId) {
        return ApiResponse.success(evaluateService.getByTaskId(taskId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Evaluate>> getByUser(@PathVariable Integer userId) {
        return ApiResponse.success(evaluateService.getByUserId(userId));
    }
}
