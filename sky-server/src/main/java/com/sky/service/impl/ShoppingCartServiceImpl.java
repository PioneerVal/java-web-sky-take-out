package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //获取用户id
        Long userId = BaseContext.getCurrentId();

        //如果添加的是个菜品
        if (shoppingCartDTO.getDishId() != null) {
            log.info("添加的是个菜品");
            //查询购物车中是否已存在该菜品
            ShoppingCart sc = shoppingCartMapper.selectByUserIdAndDishId(userId, shoppingCartDTO.getDishId());
            //查询菜品，获得价格
            Dish d = dishMapper.selectDishById(shoppingCartDTO.getDishId());
            if (sc != null) {
                log.info("购物车中已存在该菜品{}", shoppingCartDTO.getDishId());
                //设置购物车中当前菜品的数量：当前数量加1
                sc.setNumber(sc.getNumber() + 1);
                //设置购物车中当前菜品的金额：当前价格加菜价
                //sc.setAmount(sc.getAmount().add(d.getPrice()));
                shoppingCartMapper.update(sc);
                return;
            } else {
                log.info("购物车中不存在该菜品{}", shoppingCartDTO.getDishId());
                Dish dish = dishMapper.selectDishById(shoppingCartDTO.getDishId());
                //创建一个购物车对象
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .dishId(shoppingCartDTO.getDishId())
                        .setmealId(shoppingCartDTO.getSetmealId())
                        .dishFlavor(shoppingCartDTO.getDishFlavor())
                        .number(1)
                        .amount(dish.getPrice())
                        .image(dish.getImage())
                        .name(dish.getName())
                        .createTime(LocalDateTime.now())
                        .build();
                shoppingCartMapper.insert(shoppingCart);
                return;
            }
        }

        //如果菜品添加的是个套餐
        if (shoppingCartDTO.getSetmealId() != null) {
            Setmeal setmeal = setmealMapper.selectById(shoppingCartDTO.getSetmealId());
            log.info("添加的是个套餐");
            //查询购物车中是否已存在该套餐
            ShoppingCart sc = shoppingCartMapper.selectByUserIdAndSetmealId(userId, shoppingCartDTO.getSetmealId());
            if (sc != null) {
                //如果购物车中存在该套餐
                log.info("购物车中已存在该套餐{}", shoppingCartDTO.getSetmealId());
                //设置购物车中当前套餐的数量：当前数量加1
                sc.setNumber(sc.getNumber() + 1);
                //设置购物车中当前套餐的金额：当前价格加套餐价
                //sc.setAmount(sc.getAmount().add(setmeal.getPrice()));
                shoppingCartMapper.update(sc);
            }else{
                //如果购物车中不存在该套餐
                //创建一个购物车对象
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .dishId(shoppingCartDTO.getDishId())
                        .setmealId(shoppingCartDTO.getSetmealId())
                        .dishFlavor(shoppingCartDTO.getDishFlavor())
                        .number(1)
                        .createTime(LocalDateTime.now())
                        .name(setmeal.getName())
                        .image(setmeal.getImage())
                        .amount(setmeal.getPrice())
                        .build();

                shoppingCartMapper.insert(shoppingCart);

            }

        }



    }

    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
       return shoppingCartMapper.selectByUserId(userId);
    }

    @Override
    public void subShoppingCart( ShoppingCartDTO shoppingCartDTO) {
        //获取用户id
        Long userId = BaseContext.getCurrentId();

        //如果删除的是菜品
        if (shoppingCartDTO.getDishId() != null){
            log.info("删除的是菜品{}", shoppingCartDTO.getDishId());
            ShoppingCart sc = shoppingCartMapper.selectByUserIdAndDishId(userId, shoppingCartDTO.getDishId());
            if (sc.getNumber() == 1) {
                shoppingCartMapper.deleteShoppingCart(shoppingCartDTO, userId);
                return;
            }else{
                //购物车中该套餐的数量减1
                sc.setNumber(sc.getNumber() - 1);
                shoppingCartMapper.update(sc);
                return;
            }
        }
        //如果删除的是套餐
        if (shoppingCartDTO.getSetmealId() != null){
            log.info("删除的是套餐,{}", shoppingCartDTO.getSetmealId());
            ShoppingCart sc = shoppingCartMapper.selectByUserIdAndSetmealId(userId, shoppingCartDTO.getSetmealId());
            if (sc.getNumber() == 1) {
                shoppingCartMapper.deleteShoppingCart(shoppingCartDTO, userId);

            } else{
                //购物车中该套餐的数量减1
                sc.setNumber(sc.getNumber() - 1);
                shoppingCartMapper.update(sc);
            }
        }

    }
}
