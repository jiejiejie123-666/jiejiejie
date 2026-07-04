package com.example.keshe.entity;

import lombok.Data;
import java.util.List;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer active;

    // 非数据库字段：用户拥有的角色列表
    private List<String> roles;
}
