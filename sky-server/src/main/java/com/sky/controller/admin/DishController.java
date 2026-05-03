package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("上传菜品：{}",dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品的分页查询：{}",dishPageQueryDTO);
        PageResult pageDish = dishService.page(dishPageQueryDTO);

        return Result.success(pageDish);
    }

    /**
     * 根据id批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result removeDish(String ids){

        log.info("删除菜品：{}",ids);
        dishService.removeDish(ids);
        return Result.success();
    }

    @PostMapping("status/{status}")
    public Result setStatus(Long id, @PathVariable Integer status){

        log.info("更改菜品状态：菜品id：{}，状态修改为：{}",id,status);
        dishService.setStatus(id,status);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> getDishById( @PathVariable Long id){
        log.info("根据id查询菜品信息,id:{}",id);
        DishVO dishVO = dishService.getDishById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    public Result updateDish( @RequestBody DishDTO dishDTO){
        log.info("修改菜品信息：{}",dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Dish>> getByCategoryId(Long categoryId){
        log.info("根据分类id查询菜品信息：{}",categoryId);
        List<Dish> list = dishService.getDishByCategoryId(categoryId);
        return Result.success(list);
    }

}
