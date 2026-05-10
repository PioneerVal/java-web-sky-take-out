package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    void add(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
     PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 根据id修改套餐状态
     * @param id
     * @param status
     */
    void setStatus(Long id, Integer status);

    /**
     * 批量删除套餐
     * @param ids
     */
    void remove(String ids);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> listByCategoryIdAndStatus(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品信息
     * @param id
     * @return
     */
    List<DishItemVO> getDishBySetmealId(Long id);
}
