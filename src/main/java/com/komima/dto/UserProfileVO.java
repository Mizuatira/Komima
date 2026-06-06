package com.komima.dto;

import com.komima.entity.Evaluate;
import com.komima.entity.Task;
import com.komima.entity.User;
import lombok.Data;

import java.util.List;

/**
 * 用户个人主页视图对象
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
public class UserProfileVO {
    private User user;
    private List<Task> publishedTasks;
    private List<Task> acceptedTasks;
    private List<Evaluate> evaluations;
}
