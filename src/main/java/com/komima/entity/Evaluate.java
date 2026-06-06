package com.komima.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 评价实体类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
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
