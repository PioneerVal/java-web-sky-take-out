package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 获取当前分类下的菜品数据
     * @return
     */
    @GetMapping("/list")
    public Result list(Long categoryId){

        log.info("获取分类下的菜品数据:{}",categoryId);
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        //只查询起售菜品
        dish.setStatus(StatusConstant.ENABLE);

        List<DishVO> dishByCategoryId = dishService.getDishWithFlavorByCategoryId(dish);
        return Result.success(dishByCategoryId);
    }
}
