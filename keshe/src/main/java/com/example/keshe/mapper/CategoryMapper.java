package com.example.keshe.mapper;

import com.example.keshe.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("SELECT id, name, descp FROM category")
    List<Category> findAll();

    @Select("SELECT id, name, descp FROM category WHERE id = #{id}")
    Category findById(Integer id);

    @Insert("INSERT INTO category (name, descp) VALUES (#{name}, #{descp})")
    int insert(Category category);

    @Update("UPDATE category SET name=#{name}, descp=#{descp} WHERE id=#{id}")
    int update(Category category);

    @Delete("DELETE FROM category WHERE id=#{id}")
    int delete(Integer id);
}
