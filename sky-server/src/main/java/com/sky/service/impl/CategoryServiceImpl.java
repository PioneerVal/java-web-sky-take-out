package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {
    //注入mapper层对象
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        //补全基础信息
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .type(categoryDTO.getType())
                .build();

        //0为停用，1为启用，默认为0
        category.setStatus(0);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        //调用mapper层操作数据库
        categoryMapper.insertCategory(category);
    }

    @Override
    public PageResult getCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {

        //使用pagehelper插件执行分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        //获取结果
        Page<Category> page = categoryMapper.selectCategoryPage(categoryPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateCategoryStatus(Long id, Integer status) {
        categoryMapper.updateCategoryStatusById(id, status);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        //补全基础信息
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .type(categoryDTO.getType())
                .id(categoryDTO.getId())
                .build();

        //添加修改时间和修改人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.updateCategoryById(category);

    }

    @Override
    public void deleteCategory(long id) {
        //先判断该分类是否与菜品和套餐关联
        //如果和菜品有关联
        long dishCount = dishMapper.countDishByCategory(id);
        if (dishCount > 0) {
           throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //如果和套餐有关联
        long setmealCount = setmealMapper.countSetmealByCategory(id);
        if (setmealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //没有关联，删除该分类
        categoryMapper.deleteCategoryById(id);

    }


}
