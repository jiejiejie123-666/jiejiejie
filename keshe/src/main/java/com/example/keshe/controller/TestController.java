package com.example.keshe.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestController {
    private final JdbcTemplate jdbc;
    public TestController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/test-user")
    public String test() {
        var list = jdbc.queryForList("SELECT id, username, password, active FROM t_user WHERE username='admin'");
        if (list.isEmpty()) return "NOT FOUND";
        Map<String, Object> u = list.get(0);
        return String.format("id=%s, username=[%s], password=[%s], active=%s",
                u.get("id"), u.get("username"), u.get("password"), u.get("active"));
    }
}
