package com.pro.sell.service;


import com.pro.sell.dto.OrderDTO;

/**
 * 买家相关业务
 */
public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);
}
