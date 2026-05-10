package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    /**
     * 菜品的分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void removeDish(String ids);

    /**
     * 修改菜品状态
     * @param id
     * @param status
     */
    void setStatus(Long id, Integer status);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    DishVO getDishById(Long id);

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品信息
     * @param categoryId
     * @return
     */
    List<Dish> getDishByCategoryId(Long categoryId);

    /**
     * 根据分类id查询菜品信息，包含口味信息
     * @param dish
     * @return
     */
    List<DishVO> getDishWithFlavorByCategoryId(Dish dish);
}
