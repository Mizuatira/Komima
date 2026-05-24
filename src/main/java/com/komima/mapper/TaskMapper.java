
package com.komima.mapper;

import com.komima.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    int insert(Task task);
    
    Task selectById(@Param("id") Integer id);
    
    List<Task> selectAll();
    
    List<Task> selectByUserId(@Param("userId") Integer userId);
    
    List<Task> selectByReceiverId(@Param("receiverId") Integer receiverId);
    
    int update(Task task);
    
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    int updateReceiver(@Param("id") Integer id, @Param("receiverId") Integer receiverId);
    
    int deleteById(@Param("id") Integer id);
}
