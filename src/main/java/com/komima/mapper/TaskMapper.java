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

    List<Task> selectFiltered(@Param("status") Integer status, @Param("category") Integer category, @Param("hasReward") Integer hasReward, @Param("excludeUserId") Integer excludeUserId);

    List<Task> selectByUserId(@Param("userId") Integer userId);

    /** 我承接的：通过已通过的申请查到任务 */
    List<Task> selectByApplicantId(@Param("applicantId") Integer applicantId);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int deleteById(@Param("id") Integer id);

    int expirePendingTasks(@Param("days") int days, @Param("expiredStatus") int expiredStatus);
}
