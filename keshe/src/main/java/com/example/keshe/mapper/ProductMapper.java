package com.example.keshe.mapper;

import com.example.keshe.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("""
        SELECT p.id, p.name, p.photo_url, p.price, p.descp,
               p.release_date, p.cat_id,
               c.name AS category_name
        FROM product p
        LEFT JOIN category c ON p.cat_id = c.id
        ORDER BY p.id
    """)
    List<Product> findAllWithCategory();

    @Select("""
        SELECT p.id, p.name, p.photo_url, p.price, p.descp,
               p.release_date, p.cat_id,
               c.name AS category_name
        FROM product p
        LEFT JOIN category c ON p.cat_id = c.id
        WHERE p.id = #{id}
    """)
    Product findByIdWithCategory(Integer id);

    // 动态组合查询（支持按分类、名称模糊搜索、价格区间）
    @Select("""
        <script>
        SELECT p.id, p.name, p.photo_url, p.price, p.descp,
               p.release_date, p.cat_id,
               c.name AS category_name
        FROM product p
        LEFT JOIN category c ON p.cat_id = c.id
        <where>
            <if test='name != null and name != ""'>
                AND p.name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test='catId != null'>
                AND p.cat_id = #{catId}
            </if>
            <if test='minPrice != null'>
                AND p.price &gt;= #{minPrice}
            </if>
            <if test='maxPrice != null'>
                AND p.price &lt;= #{maxPrice}
            </if>
        </where>
        ORDER BY p.id
        </script>
    """)
    List<Product> search(@Param("name") String name,
                         @Param("catId") Integer catId,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice);

    @Insert("INSERT INTO product (name, photo_url, price, descp, release_date, cat_id) " +
            "VALUES (#{name}, #{photoUrl}, #{price}, #{descp}, #{releaseDate}, #{catId})")
    int insert(Product product);

    @Update("UPDATE product SET name=#{name}, photo_url=#{photoUrl}, price=#{price}, " +
            "descp=#{descp}, release_date=#{releaseDate}, cat_id=#{catId} WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM product WHERE id=#{id}")
    int delete(Integer id);
}
