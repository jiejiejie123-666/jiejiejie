package com.example.keshe.controller;

import com.example.keshe.entity.Product;
import com.example.keshe.service.CategoryService;
import com.example.keshe.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService ps, CategoryService cs) {
        this.productService = ps; this.categoryService = cs;
    }

    // 解决空字符串 → Integer/Double/LocalDate 转换失败的问题
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(LocalDate.parse(text.trim()));
                }
            }
        });
    }

    // ==================== 列表（搜索+分页） ====================

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer catId,
                       @RequestParam(required = false) Double minPrice,
                       @RequestParam(required = false) Double maxPrice,
                       Model model) {
        PageInfo<Product> pageInfo = productService.searchProducts(pageNum, pageSize, name, catId, minPrice, maxPrice);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("name", name);
        model.addAttribute("catId", catId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "ProductList";
    }

    // ==================== 新增表单 ====================

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("formAction", "/product/add");
        return "ProductForm";
    }

    // ==================== 新增提交 ====================

    @PostMapping("/add")
    public String addSubmit(Product product, RedirectAttributes ra) {
        productService.save(product);
        ra.addFlashAttribute("msg", "商品 " + product.getName() + " 添加成功");
        return "redirect:/product/list";
    }

    // ==================== 编辑表单 ====================

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Product product = productService.findByIdWithCategory(id);
        if (product == null) {
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("formAction", "/product/edit/" + id);
        return "ProductForm";
    }

    // ==================== 编辑提交 ====================

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Integer id, Product product, RedirectAttributes ra) {
        product.setId(id);
        productService.update(product);
        ra.addFlashAttribute("msg", "商品 " + product.getName() + " 更新成功");
        return "redirect:/product/list";
    }

    // ==================== 删除 ====================

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        Product p = productService.findByIdWithCategory(id);
        String name = p != null ? p.getName() : "未知";
        productService.delete(id);
        ra.addFlashAttribute("msg", "🗑️ 商品「" + name + "」已删除");
        return "redirect:/product/list";
    }
}
