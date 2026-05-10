package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 添加分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);


    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult getCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类状态
     * @param id
     * @param status
     */
    void updateCategoryStatus(Long id, Integer status);

    /**、
     * 修改分类信息
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     *根据id删除分类
     * @param id
     */
    void deleteCategory(long id);

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    List<Category> getCategoryByType(Integer id);
}
