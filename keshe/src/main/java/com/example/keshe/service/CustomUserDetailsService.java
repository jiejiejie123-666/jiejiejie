package com.example.keshe.service;

import com.example.keshe.entity.User;
import com.example.keshe.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        if (user.getActive() != null && user.getActive() == 0) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 查询用户的角色
        List<String> roles = userMapper.findRolesByUserId(user.getId());
        // 转换为 Spring Security 需要的格式（ROLE_ 前缀已在数据库中）
        String[] roleArray = roles.toArray(new String[0]);

        // 使用明文密码（数据库存储的也是明文）
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roles.stream()
                        .map(r -> r.replace("ROLE_", ""))
                        .toArray(String[]::new))
                .disabled(user.getActive() == null || user.getActive() != 1)
                .build();
    }
}
