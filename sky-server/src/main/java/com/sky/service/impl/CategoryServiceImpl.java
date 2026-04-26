package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {
    //注入mapper层对象
    @Autowired
    CategoryMapper categoryMapper;
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
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        //获取结果
        Page<Category> page = categoryMapper.selectCategoryPage(categoryPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }


}
