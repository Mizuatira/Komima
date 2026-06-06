package com.komima.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 申请实体类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
@Accessors(chain = true)
public class Application {

    private Integer id;
    private Integer taskId;
    private Integer applicantId;
    /** 0=待审核 1=已通过 2=已拒绝 */
    private Integer status;
    /** 发布方确认 */
    private Integer confirmPublisher;
    /** 接单方确认 */
    private Integer confirmApplicant;
    private LocalDateTime createTime;
}
