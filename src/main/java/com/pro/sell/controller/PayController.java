package com.pro.sell.controller;

import com.lly835.bestpay.model.PayResponse;
import com.pro.sell.dto.OrderDTO;
import com.pro.sell.enums.ResultEnum;
import com.pro.sell.execption.SellException;
import com.pro.sell.service.OrderService;
import com.pro.sell.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 微信支付功能：
 *
 */
@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    //创建支付订单以及支付
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        //1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2. 发起支付
//        PayResponse payResponse = payService.create(orderDTO);
//
//        map.put("payResponse", payResponse);
//        map.put("returnUrl", returnUrl);
//
//        return new ModelAndView("pay/create", map);
        //test
        map.put("msg", ResultEnum.ORDER_PAY_SUCCESS.getMessage());
        map.put("url", "/sell//buyer/product/list");
        return new ModelAndView("pay/createtest", map);
    }

    /**
     * 微信异步通知商户支付结果
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回给微信处理结果，如果不回应，就一直会收到异步通知
        return new ModelAndView("pay/success");
    }

}
