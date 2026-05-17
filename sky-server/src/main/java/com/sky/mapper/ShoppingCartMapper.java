package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    //删除数据,删除一个商品
    void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO, Long userId);


    //插入数据
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            " VALUES (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime}) ")
    void insert(ShoppingCart shoppingCart);

    //根据用户id查询购物车
    @Select("select * from shopping_cart where user_id =#{userId}")
    List<ShoppingCart> selectByUserId(Long userId);

    //根据用户id和菜品id查询购物车
    @Select("select * from shopping_cart where user_id = #{userId} and dish_id = #{dishId}")
    ShoppingCart selectByUserIdAndDishId(Long userId, Long dishId);

    //修改数据
    void update(ShoppingCart sc);

    //根据用户id和套餐id查询购物车
    @Select("select * from shopping_cart where user_id = #{userId} and setmeal_id = #{setmealId}")
    ShoppingCart selectByUserIdAndSetmealId(Long userId, Long setmealId);

    //清空购物车
    @Select("delete from shopping_cart where user_id = #{userId}")
    void cleanByUserId(Long userId);
}
