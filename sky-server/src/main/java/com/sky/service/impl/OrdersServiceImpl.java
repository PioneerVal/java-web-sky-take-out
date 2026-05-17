package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrdersService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderSubmitVO userSubmit(OrdersSubmitDTO ordersSubmitDTO) {
        //数据校验
        //校验地址是否存在
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.selectAddressBookById(addressBookId);
        if(addressBook == null){
            //地址不存在,抛出业务异常
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //校验购物车是否为空,根据用户id查询
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByUserId(userId);
        if(shoppingCartList == null || shoppingCartList.isEmpty()){
            //购物车为空,抛出业务异常
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //构建地址信息
        String address = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail();
        //创建订单,构建订单对象
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setAddress(address);
        orders.setOrderTime(LocalDateTime.now());
        orders.setUserId(userId);
        orders.setPayMethod(1);
        //TODO 目前没有收款权限，直接将订单置为已付款带派送状态
        //订单状态,直接写为付款成功
        orders.setPayStatus(Orders.PAID);
        //订单状态,待派送
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        //将付款时间设置为下单时间
        orders.setCheckoutTime(LocalDateTime.now());

        /*orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);*/

        orders.setUserName(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());

        //插入订单数据
        ordersMapper.insert(orders);
        //创建订单明细对象
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        //插入n条订单明细数据
        orderDetailMapper.insertBatch(orderDetailList);

        //清空购物车数据
        shoppingCartMapper.cleanByUserId(userId);

        //封装VO对象并返回
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    @Override
    public PageResult pageOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        //获取当前用户id
       // Long userId = BaseContext.getCurrentId();
      //  ordersPageQueryDTO.setUserId(userId);
        if(ordersPageQueryDTO.getUserId()==null){
            log.info("用户id为空，当前是商家在查询订单详情，id：{}", BaseContext.getCurrentId());
        }else{
            log.info("当前是用户在查询订单详情，id：{}",ordersPageQueryDTO.getUserId());
        }

        //分页查询订单
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        List<Orders> ordersList = ordersMapper.pageOrders(ordersPageQueryDTO);


        //根据查到的订单id查询订单详情
        List<OrderVO> orderVOList = new ArrayList<>();
        //类型转换
         Page<Orders> page = (Page<Orders>)ordersList;
         if(page!=null && page.getResult()!=null) {
             //遍历订单列表,封装订单详情
             for (Orders order : page) {
                 //获取订单id
                 Long orderId = order.getId();
                 List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(orderId);
                 OrderVO orderVO = new OrderVO();
                 BeanUtils.copyProperties(order, orderVO);
                 orderVO.setOrderDetailList(orderDetailList);
                 orderVOList.add(orderVO);
             }
             return new PageResult(page.getTotal(), orderVOList);
         }
         //订单为空，返回null
         return null;
    }


    @Override
    public OrderVO getOrderDetailById(Long id) {
        //根据id查询订单信息
        Orders orders = ordersMapper.selectOrderById(id);
        if(orders != null){
            List<OrderDetail> orderDetailList = orderDetailMapper.selectByOrderId(id);
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDetailList(orderDetailList);
            return orderVO;
        }
        return null;
    }

    @Override
    public void confirm(OrdersDTO ordersDTO) {
        Orders orders = new Orders();
        orders.setId(ordersDTO.getId());
        //查询订单信息
        orders = ordersMapper.selectOrderById(ordersDTO.getId());
        //判断订单是否存在
        if(orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //判断订单状态,订单为待接单和已支付状态才能确认接单
        if(!Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED) && !Objects.equals(orders.getPayStatus(), Orders.PAID)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //确认接单
        orders.setStatus(Orders.CONFIRMED);
        safeUpdateOrder(orders);

    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        //创建订单对象并设置属性
        Orders orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .build();


        orders = ordersMapper.selectOrderById(orders.getId());
        //判断订单是否存在
        if(orders.getId() == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //判断订单状态,订单为待接单状态才能确认接单
        if(!Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //判断订单支付状态
        if(!Objects.equals(orders.getPayStatus(), Orders.PAID)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelTime(LocalDateTime.now());
        //退款，目前无法实现，暂时直接修改订单状态
        orders.setPayStatus(Orders.REFUND);
        safeUpdateOrder(orders);
    }

    @Override
    public void delivery(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders = ordersMapper.selectOrderById(orders.getId());
        if(orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(!Objects.equals(orders.getStatus(), Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        safeUpdateOrder(orders);
    }

    @Override
    public void complete(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders = ordersMapper.selectOrderById(orders.getId());
        if(orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(!Objects.equals(orders.getStatus(), Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.COMPLETED);
        //订单送达时间设置为当前时间
        orders.setDeliveryTime(LocalDateTime.now());
        safeUpdateOrder(orders);
    }


    //安全更新订单信息的方法，订单不存在会抛出异常，避免出现全表更新
    private void safeUpdateOrder(Orders orders){

        if(orders.getId() == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        ordersMapper.updateById(orders);
    }

}
