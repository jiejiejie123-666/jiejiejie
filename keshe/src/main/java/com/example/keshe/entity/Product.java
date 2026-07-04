package com.example.keshe.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Product {
    private Integer id;
    private String name;
    private String photoUrl;
    private Double price;
    private String descp;
    private LocalDate releaseDate;
    private Integer catId;

    // 多表联查时携带的分类名称（非数据库字段）
    private String categoryName;
}
