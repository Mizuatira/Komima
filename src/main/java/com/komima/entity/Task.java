package com.komima.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 任务实体类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
@Accessors(chain = true)
public class Task {

    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private Integer status;
    private Integer category;
    private Integer hasReward;
    /** 需要招募人数，默认1 */
    private Integer recruitCount;
    /** 已通过申请数，子查询注入 */
    private transient Integer approvedCount;
    /** 待审核申请数，子查询注入 */
    private transient Integer applyCount;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
