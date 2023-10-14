package com.pro.sell.service.impl;


import com.pro.sell.dataobject.SellerInfo;
import com.pro.sell.repository.SellerInfoRepository;
import com.pro.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
