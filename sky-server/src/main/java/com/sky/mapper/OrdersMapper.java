package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersMapper {

    // 插入数据
    public void insert(Orders orders);

    // 分页查询历史订单
    List<Orders> pageOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    // 根据id查询订单详情
    @Select("select * from orders where id = #{id}")
    Orders selectOrderById(Long id);

    // 更新订单
    void updateById(Orders orders);
}
