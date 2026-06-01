package com.komima.mapper;

import com.komima.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    int insert(Application application);

    Application selectById(@Param("id") Integer id);

    List<Application> selectByTaskId(@Param("taskId") Integer taskId);

    Application selectByTaskAndApplicant(@Param("taskId") Integer taskId, @Param("applicantId") Integer applicantId);

    int countApprovedByTaskId(@Param("taskId") Integer taskId);

    int countConfirmedByTaskId(@Param("taskId") Integer taskId);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int confirmPublisher(@Param("id") Integer id);

    int confirmApplicant(@Param("id") Integer id);

    int deleteById(@Param("id") Integer id);

    int deleteByTaskId(@Param("taskId") Integer taskId);
}
