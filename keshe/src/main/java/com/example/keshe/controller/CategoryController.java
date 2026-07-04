package com.example.keshe.controller;

import com.example.keshe.entity.Category;
import com.example.keshe.service.CategoryService;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService cs) { this.categoryService = cs; }

    // 解决空字符串 → Integer 转换失败的问题
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    // ==================== 列表 ====================

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "CategoryList";
    }

    // ==================== 新增表单 ====================

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("formAction", "/category/add");
        return "CategoryForm";
    }

    // ==================== 新增提交 ====================

    @PostMapping("/add")
    public String addSubmit(Category category, RedirectAttributes ra) {
        categoryService.save(category);
        ra.addFlashAttribute("msg", "分类 " + category.getName() + " 添加成功");
        return "redirect:/category/list";
    }

    // ==================== 编辑表单 ====================

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return "redirect:/category/list";
        }
        model.addAttribute("category", category);
        model.addAttribute("formAction", "/category/edit/" + id);
        return "CategoryForm";
    }

    // ==================== 编辑提交 ====================

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Integer id, Category category, RedirectAttributes ra) {
        category.setId(id);
        categoryService.update(category);
        ra.addFlashAttribute("msg", "分类 " + category.getName() + " 更新成功");
        return "redirect:/category/list";
    }

    // ==================== 删除 ====================

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        Category c = categoryService.findById(id);
        String name = c != null ? c.getName() : "未知";
        categoryService.delete(id);
        ra.addFlashAttribute("msg", "🗑️ 分类「" + name + "」已删除");
        return "redirect:/category/list";
    }
}
