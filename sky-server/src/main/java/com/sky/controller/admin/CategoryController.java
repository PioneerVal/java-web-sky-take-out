package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/category")


public class CategoryController {
    //注入service层对象
    @Autowired
    CategoryService categoryService;

    /**
     * 添加分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){

        log.info("添加分类{}",categoryDTO);

        categoryService.addCategory(categoryDTO);

        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> getCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询分类{}",categoryPageQueryDTO);

        //调用service层

        PageResult pageResultCategory = categoryService.getCategoryPage(categoryPageQueryDTO);

        return Result.success(pageResultCategory);
    }

    /**
     * 修改分类状态
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateCategoryStatus(@PathVariable Integer status, Long id){
        log.info("修改分类状态:分类ID：{}，状态修改为：{}",id,status);
        categoryService.updateCategoryStatus(id,status);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
   public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
    log.info("修改分类{}",categoryDTO);
    categoryService.updateCategory(categoryDTO);
    return Result.success();
   }

   @DeleteMapping
   public Result deleteCategory(long id){
        log.info("删除分类,id={}",id);
        categoryService.deleteCategory(id);
        return Result.success();
   }
}
