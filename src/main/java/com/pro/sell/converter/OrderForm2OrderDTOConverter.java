package com.pro.sell.converter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pro.sell.dataobject.OrderDetail;
import com.pro.sell.dto.OrderDTO;
import com.pro.sell.enums.ResultEnum;
import com.pro.sell.execption.SellException;
import com.pro.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * //orderForm -> orderDTO
 */
@Slf4j
public class OrderForm2OrderDTOConverter {
    //orderForm -> orderDTO
    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
