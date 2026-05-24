
package com.komima.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {

    private Integer id;
    
    private String title;
    
    private String content;
    
    private Integer userId;
    
    private Integer status;
    
    private Integer receiverId;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
