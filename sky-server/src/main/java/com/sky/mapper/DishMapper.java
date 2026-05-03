package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {


    /**\
     * 根据分类id查询所关联菜品的数量
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id = #{id}")
    public Long countDishByCategory(Long id);

    //添加菜品信息
    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    //分页查询
    Page<DishVO> selectPage(DishPageQueryDTO dishPageQueryDTO);

    //根据id查询菜品信息
    @Select("select * from dish where id = #{id}")
    Dish selectDishById(Long id);

    //批量删除菜品
    void deleteDishByIds(String[] idsArr);

    //修改菜品状态
    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(Long id, Integer status);

    //修改菜品信息
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    //根据分类id查询菜品
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getDishByCategoryId(Long categoryId);
}
