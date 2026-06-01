package com.komima.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class EvaluateDTO {

    @NotNull(message = "委托ID不能为空")
    private Integer taskId;

    @NotNull(message = "评分人ID不能为空")
    private Integer fromUserId;

    @NotNull(message = "被评分人ID不能为空")
    private Integer toUserId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能低于1")
    @Max(value = 5, message = "评分不能高于5")
    private Integer score;

    private String content;
}
