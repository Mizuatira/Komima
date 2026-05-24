
package com.komima.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Evaluate {

    private Integer id;
    
    private Integer taskId;
    
    private Integer userId;
    
    private Integer score;
    
    private String content;
    
    private LocalDateTime createTime;
}
