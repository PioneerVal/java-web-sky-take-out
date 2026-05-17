package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrdersController")
@RequestMapping("/user/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = ordersService.userSubmit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 历史订单查询
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO ){
        Long userId = BaseContext.getCurrentId();
        //传递用户id
        ordersPageQueryDTO.setUserId(userId);
        log.info("分页查询订单:{}", ordersPageQueryDTO);
        PageResult pageResult = ordersService.pageOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param id
     */

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail( @PathVariable Long id){

        log.info("查询订单明细，订单号：{}",id);
        OrderVO orderVO = ordersService.getOrderDetailById(id);

        return Result.success(orderVO);
    }

}
