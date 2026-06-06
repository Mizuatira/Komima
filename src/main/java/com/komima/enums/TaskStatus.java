package com.komima.enums;

/**
 * 任务状态枚举
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
public enum TaskStatus {

    PENDING(0, "待接单"),
    IN_PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成"),
    EXPIRED(3, "已过期");

    private final int code;
    private final String desc;

    TaskStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TaskStatus fromCode(int code) {
        for (TaskStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的任务状态: " + code);
    }

    public static boolean canComplete(TaskStatus current) {
        return current == IN_PROGRESS;
    }
}
