package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {

    //添加分类信息
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "value(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insertCategory(Category category);

    //分页查询分类信息
    Page<Category> selectCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO);
}
