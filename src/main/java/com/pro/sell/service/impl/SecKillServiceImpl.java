package com.pro.sell.service.impl;


import com.pro.sell.execption.SellException;
import com.pro.sell.service.RedisLock;
import com.pro.sell.service.SecKillService;
import com.pro.sell.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 秒杀功能具体实现
 */
@Service
public class SecKillServiceImpl implements SecKillService {

    private static final int TIMEOUT = 10 * 1000; //超时时间 10s

    @Autowired
    private RedisLock redisLock;

    /**
     * 国庆活动，毛巾特价，限量10000份
     */
    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;
    static {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("08", 10000);
        stock.put("08", 10000);
    }

    private String queryMap(String productId) {
        return "国庆活动，毛巾特价，限量"
                + products.get(productId)
                +"份，还剩：" + stock.get(productId)+" 份，"
                +" 该商品成功下单用户数目："
                +  orders.size() +" 人" ;
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    //可以使用synchronized关键字对方法进行修饰，从而解决高并发情况下超卖的问题；
    //缺点：访问速度慢；无法细粒度的处理；
    @Override
    public void orderProductMockDiffUser(String productId) {
        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(productId, String.valueOf(time))){
            //如果没有拿到锁
            throw new SellException(101, "网络开小差了，换个姿势再试试~~");
        }

        //1.查询该商品库存，为0则活动结束。
        int stockNum = stock.get(productId);
        if(stockNum == 0) {
            throw new SellException(100,"活动结束");
        }else {
            //2.下单(模拟不同用户openid不同)
            orders.put(KeyUtil.genUniqueKey(),productId);
            //3.减库存
            stockNum =stockNum-1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId,stockNum);
        }

        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }
}
