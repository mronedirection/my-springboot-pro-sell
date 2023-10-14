package com.pro.sell.service;


import com.pro.sell.dataobject.SellerInfo;

/**
 * 卖家端登录接口
 */
public interface SellerService {

    /**
     * 通过openid查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
