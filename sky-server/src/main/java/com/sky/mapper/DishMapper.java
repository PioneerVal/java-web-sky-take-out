package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**\
     * 根据分类id查询所关联菜品的数量
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id = #{id}")
    public Long countDishByCategory(Long id);
}
