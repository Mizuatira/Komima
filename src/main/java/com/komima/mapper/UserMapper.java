package com.komima.mapper;

import com.komima.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    User selectById(@Param("id") Integer id);

    User selectByUsername(@Param("username") String username);

    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    List<User> selectAll();

    int updateProfile(User user);

    int updateRole(@Param("id") Integer id, @Param("role") Integer role);

    int deleteById(@Param("id") Integer id);
}
