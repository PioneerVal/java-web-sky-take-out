package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        //新增套餐，构建Setmeal对象
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        //新增套餐和菜品的关联关系
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //遍历setmealDishes,添加setmealId
        setmealDishes.forEach(setmealDish->setmealDish.setSetmealId(setmealId));
        //批量插入
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        //设置分页参数
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        //查询并获取结果
        Page<SetmealVO> pageResult = setmealMapper.selectByPage(setmealPageQueryDTO);
        return new PageResult(pageResult.getTotal(), pageResult.getResult());
    }

    @Override
    public SetmealVO getById(Long id) {
        //根据id查询套餐信息
        Setmeal setmeal = setmealMapper.selectById(id);
        //根据id查询套餐关联的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        //构建套餐VO对象并返回
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //更新菜品表中的数据，并获取套餐id
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //更新套餐和菜品的关联关系，删除当前套餐关联的菜，插入新的关联关系
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.deleteBySetmealId(setmealId);
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void setStatus(Long id, Integer status) {
        //判断套餐关联的菜品是否为停售状态，如果为停售状态，则不能起售该套餐
        //判断是否要将菜品改为起售状态
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            // List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
            //select d.* from setmeal_dish s left join dish d on s.dish_id = d.id where s.setmeal_id = 32;
            List<Dish> dishes = setmealDishMapper.selectBySetmealIdAndDishId(id);
            for (Dish dish : dishes) {
                if (Objects.equals(dish.getStatus(), StatusConstant.DISABLE)) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        //批量修改套餐状态
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        setmealMapper.updateStatus(ids, status);
    }

    @Override
    @Transactional
    public void remove(String ids) {
        //若该套餐为起售状态，则不能删除该套餐
        String[] idsArray = ids.split(",");
        List<Setmeal> setmealList = setmealMapper.selectByIds(idsArray);
        for (Setmeal setmeal : setmealList) {
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除套餐
        setmealMapper.delete(idsArray);
        //删除套餐和菜品的关联关系
        setmealDishMapper.delete(idsArray);
    }

    @Override
    public List<Setmeal> listByCategoryIdAndStatus(Setmeal setmeal) {

        return setmealMapper.selectByCondition(setmeal);
    }

    @Override
    public List<DishItemVO> getDishBySetmealId(Long id) {
        //根据套餐id查询菜品id
        List<DishItemVO> dishItemVOS = setmealDishMapper.selectDishIdBySetmealId(id);
        return dishItemVOS;
    }
}
