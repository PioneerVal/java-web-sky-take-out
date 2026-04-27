package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CategoryMapper {

    //添加分类信息
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "value(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insertCategory(Category category);

    //分页查询分类信息
    Page<Category> selectCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    //修改分类状态
    @Update("update category set status = #{status} where id =#{id}")
    void updateCategoryStatusById(Long id, Integer status);

    //修改分类信息
    @Update("update category set name = #{name},sort = #{sort},update_time = #{updateTime},update_user = #{updateUser} where id = #{id}")
    void updateCategoryById(Category category);

    //根据id查询分类信息
    @Delete("delete from category where id = #{id}")
    void deleteCategoryById(long id);
}
