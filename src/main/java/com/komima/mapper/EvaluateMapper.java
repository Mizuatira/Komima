package com.komima.mapper;

import com.komima.entity.Evaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EvaluateMapper {

    int insert(Evaluate evaluate);

    List<Evaluate> selectByTaskId(@Param("taskId") Integer taskId);

    List<Evaluate> selectByUserId(@Param("userId") Integer userId);

    List<Evaluate> selectAll();

    Evaluate selectByTaskAndFromUser(@Param("taskId") Integer taskId, @Param("fromUserId") Integer fromUserId);

    int deleteById(@Param("id") Integer id);

    int deleteByTaskId(@Param("taskId") Integer taskId);
}
