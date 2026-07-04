package com.example.keshe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    // 根据用户名查用户（用于登录认证）
    @Select("SELECT id, username, password, active FROM t_user WHERE username = #{username}")
    com.example.keshe.entity.User findByUsername(@Param("username") String username);

    // 查用户的所有角色
    @Select("""
        SELECT r.role FROM t_role r
        INNER JOIN t_user_role ur ON r.id = ur.role_id
        WHERE ur.user_id = #{userId}
    """)
    List<String> findRolesByUserId(@Param("userId") Integer userId);
}
