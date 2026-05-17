package com.sky.controller.admin;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrdersController")
@RequestMapping("/admin/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("分页搜索订单：{}", ordersPageQueryDTO);
        PageResult pageResult = ordersService.pageOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getById(@PathVariable Long id){

        log.info("查询订单详情，订单id为：{}",id);
        OrderVO orderVO = ordersService.getOrderDetailById(id);
        return Result.success(orderVO);
    }

    /**
     * 接单
     * @param ordersDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result confirm( @RequestBody OrdersDTO ordersDTO){

        log.info("订单确认：{}", ordersDTO.getId());
        ordersService.confirm(ordersDTO);
        return Result.success();
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){

        log.info("订单拒单：{}", ordersRejectionDTO);
        ordersService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 派单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result delivery( @PathVariable Long id){
        log.info("订单派送：{}", id);
        ordersService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result complete( @PathVariable Long id){
        log.info("订单完成：{}", id);
        ordersService.complete(id);
        return Result.success();
    }
}
