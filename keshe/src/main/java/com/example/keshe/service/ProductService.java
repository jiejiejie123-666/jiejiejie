package com.example.keshe.service;

import com.example.keshe.entity.Product;
import com.example.keshe.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductMapper productMapper;

    public ProductService(ProductMapper pm) {
        this.productMapper = pm;
    }

    // ==================== Phase 5: 声明式缓存 ====================

    /** 全量列表，带缓存 */
    @Cacheable(value = "products", unless = "#result == null or #result.isEmpty()")
    public List<Product> findAllWithCategory() {
        log.info("⏳ [Cache 未命中] 商品全量列表从 MySQL 查询");
        return productMapper.findAllWithCategory();
    }

    /** 单实体查询，带缓存 */
    @Cacheable(value = "product", key = "#id", unless = "#result == null")
    public Product findByIdWithCategory(Integer id) {
        log.info("⏳ [Cache 未命中] product::{} 从 MySQL 查询", id);
        return productMapper.findByIdWithCategory(id);
    }

    /** 新增，写入缓存 + 清除列表缓存 */
    @CachePut(value = "product", key = "#result.id")
    @CacheEvict(value = "products", allEntries = true)
    public Product save(Product product) {
        log.info("💾 [新增商品] name={}, 写入 MySQL", product.getName());
        productMapper.insert(product);
        return product;
    }

    /** 更新，写入缓存 + 清除列表缓存 */
    @CachePut(value = "product", key = "#result.id")
    @CacheEvict(value = "products", allEntries = true)
    public Product update(Product product) {
        log.info("✏️ [更新商品] id={}, 写入 MySQL", product.getId());
        productMapper.update(product);
        return product;
    }

    /** 删除，清除缓存 */
    @CacheEvict(value = {"product", "products"}, allEntries = true, beforeInvocation = true)
    public void delete(Integer id) {
        log.info("🗑️ [删除商品] id={}, 删除 MySQL 记录", id);
        productMapper.delete(id);
    }

    // ==================== 分页搜索（不带缓存，搜索条件多变） ====================

    public PageInfo<Product> searchProducts(int pageNum, int pageSize,
                                            String name, Integer catId,
                                            Double minPrice, Double maxPrice) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(productMapper.search(name, catId, minPrice, maxPrice));
    }
}
