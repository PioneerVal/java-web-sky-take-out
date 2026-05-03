package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    //注入菜品Mapper
    @Autowired
    private DishMapper dishMapper;
    //注入菜品口味Mapper
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    //注入套餐菜品Mapper
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        //添加菜品，构建菜品对象
        Dish dish = new Dish();

        //拷贝属性，需要两个对象的属性相同，才可以拷贝
        BeanUtils.copyProperties(dishDTO,dish);

        //设置菜品状态
        dish.setStatus(StatusConstant.DISABLE);

        //操作数据库
        dishMapper.insertDish(dish);

        //构建菜品口味对象
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        //遍历菜品口味对象，设置菜品id
        dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        if(dishFlavors != null && !dishFlavors.isEmpty()){
            dishFlavorMapper.insert(dishFlavors);
        }

    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        //设置分页参数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.selectPage(dishPageQueryDTO);

        return new PageResult(dishPage.getTotal(),dishPage.getResult());
    }

    @Override
    public void removeDish(String ids) {

        //将ids字符串转为数组
        String[] idsArr = ids.split(",");

        //判断菜品是否起售状态,如果菜品起售状态，则不能删除
        for (String s : idsArr) {
            //获取菜品状态
            Dish dish = dishMapper.selectDishById(Long.parseLong(s));
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(dish.getName() + "菜品正在起售中，不能删除");
            }
        }

        //判断菜品是否关联了套餐，如果菜品关联了套餐，则不能删除
        for (String s : idsArr) {
            Long count = setmealDishMapper.countByDishId(Long.parseLong(s));
            if(count>0){
                throw new DeletionNotAllowedException("当前菜品关联了套餐，不能删除");
            }
        }
        //执行菜品的批量删除
        dishMapper.deleteDishByIds(idsArr);


    }

    @Override
    public void setStatus(Long id, Integer status) {
        //判断菜品是否要改为停售状态，如果需要将菜品停售，则将关联的套餐也停售
        /*if(status.equals(StatusConstant.DISABLE)){
            //查询该菜品关联所有的套餐
        }*/
        //TODO 待套餐接口完善后，将此功能完善
        dishMapper.updateStatus(id,status);
    }

    @Override
    public DishVO getDishById(Long id) {
        //构造要返回的DishVO对象
        DishVO dishVO = new DishVO();
        //查询菜品信息
        Dish dish = dishMapper.selectDishById(id);
        BeanUtils.copyProperties(dish,dishVO);

        //查询菜品分类名称
        Category category = categoryMapper.selectCategoryById(dish.getCategoryId());
        if(category != null) {
            dishVO.setCategoryName(category.getName());
        }

        //查询菜品口味信息
        List<DishFlavor> dishFlavor = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(dishFlavor);

        return dishVO;
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        //更新菜品表中的数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
        //更新菜品口味表中的数据,先删除后插入
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors != null && !dishFlavors.isEmpty()){
            dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insert(dishFlavors);
        }

    }

    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }
}
