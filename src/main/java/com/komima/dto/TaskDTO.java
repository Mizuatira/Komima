package com.komima.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TaskDTO {

    @NotBlank(message = "委托标题不能为空")
    private String title;

    @NotBlank(message = "委托内容不能为空")
    private String content;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "请选择分类")
    private Integer category;

    private Integer hasReward;

    @NotNull(message = "请设置招募人数")
    private Integer recruitCount;
}
