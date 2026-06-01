package com.komima.dto;

import com.komima.entity.Evaluate;
import com.komima.entity.Task;
import com.komima.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileVO {
    private User user;
    private List<Task> publishedTasks;
    private List<Task> acceptedTasks;
    private List<Evaluate> evaluations;
}
