package com.komima.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Evaluate {

    private Integer id;
    private Integer taskId;
    private Integer fromUserId;
    private Integer toUserId;
    private Integer score;
    private String content;
    private LocalDateTime createTime;
}
