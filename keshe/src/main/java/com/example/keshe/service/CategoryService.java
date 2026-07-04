package com.example.keshe.service;

import com.example.keshe.entity.Category;
import com.example.keshe.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper cm) {
        this.categoryMapper = cm;
    }

    /** 全量列表，带缓存 */
    @Cacheable(value = "categories", unless = "#result == null or #result.isEmpty()")
    public List<Category> findAll() {
        log.info("⏳ [Cache 未命中] categories 全量从 MySQL 查询");
        return categoryMapper.findAll();
    }

    /** 单实体查询，带缓存 */
    @Cacheable(value = "category", key = "#id", unless = "#result == null")
    public Category findById(Integer id) {
        log.info("⏳ [Cache 未命中] category::{} 从 MySQL 查询", id);
        return categoryMapper.findById(id);
    }

    /** 新增，写入缓存 + 清除列表缓存 */
    @CachePut(value = "category", key = "#result.id")
    @CacheEvict(value = "categories", allEntries = true)
    public Category save(Category category) {
        log.info("💾 [新增分类] name={}, 写入 MySQL", category.getName());
        categoryMapper.insert(category);
        return category;
    }

    /** 更新，写入缓存 + 清除列表缓存 */
    @CachePut(value = "category", key = "#result.id")
    @CacheEvict(value = "categories", allEntries = true)
    public Category update(Category category) {
        log.info("✏️ [更新分类] id={}, 写入 MySQL", category.getId());
        categoryMapper.update(category);
        return category;
    }

    /** 删除，清除缓存 */
    @CacheEvict(value = {"category", "categories"}, allEntries = true, beforeInvocation = true)
    public void delete(Integer id) {
        log.info("🗑️ [删除分类] id={}, 删除 MySQL 记录", id);
        categoryMapper.delete(id);
    }
}
