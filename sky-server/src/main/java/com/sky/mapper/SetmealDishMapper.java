package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //根据菜品查询关联的套餐
    @Select("select count(*) from setmeal_dish where dish_id = #{dishId}")
    Long countByDishId(Long dishId);

    //批量插入
    void insertBatch(List<SetmealDish> setmealDishes);

    //根据套餐id查询关联的菜品
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> selectBySetmealId(Long setmealId);

    //根据套餐id删除关联关系
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    //根据套餐id和菜品id查询关联关系,返回的是套餐所关联的菜品信息
    @Select("select d.* from setmeal_dish s left join dish d on s.dish_id = d.id where s.setmeal_id = #{id} ")
    List<Dish> selectBySetmealIdAndDishId(Long id);

    //批量删除
    void delete(String[] idsArray);


    //根据菜品id查询关联的套餐id
    @Select("select setmeal_id from setmeal_dish where dish_id = #{id}")
    List<Long> selectSetmealIdByDishId(Long id);
}
