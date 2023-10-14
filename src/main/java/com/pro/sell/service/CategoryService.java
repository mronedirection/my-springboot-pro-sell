package com.pro.sell.service;

import com.pro.sell.dataobject.ProductCategory;

import java.util.List;

/**
 * 商品种类相关业务
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
