package com.pro.sell.controller;

import com.pro.sell.dataobject.ProductCategory;
import com.pro.sell.dataobject.ProductInfo;
import com.pro.sell.service.CategoryService;
import com.pro.sell.service.ProductService;
import com.pro.sell.utils.ResultVOUtil;
import com.pro.sell.vo.ProductInfoVO;
import com.pro.sell.vo.ProductVO;
import com.pro.sell.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家端，操作商品相关功能
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 买家端查看上架商品列表
     * @Cacheable: 缓存层的注解：
     *          参数： cacheNames表示redis的key的目录
     *                key表示实际的redis中的key，如不写，默认取
     *                只有cacheNames与key都相同，才能说明是同一条redis的数据
     *          作用： 第一次执行方法访问数据库并将数据保存到redis中，
     *                并通过参数的cacheNames和key唯一标识这条数据
     *                之后都是访问Redis读取数据，提高读取数据的速度
     * @param sellerId
     * @return
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "product",key = "123")
//    @Cacheable(cacheNames = "product", key = "#sellerId", condition = "#sellerId.length() > 3", unless = "#result.getCode() != 0")
    public ResultVO list(@RequestParam(value = "sellerId", required = false) String sellerId){
        //1.查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //2.查询所有上架商品的类目(一次性查询)
//        ArrayList<Integer> categoryTypeList = new ArrayList<>();
//        //传统方法
//        for(ProductInfo productInfo : productInfoList){
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
        //lamdba表达式
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //3.数据拼装(拼装成ResultVO返回到前端)
        List<ProductVO> productVOList = new ArrayList<>();
        for(ProductCategory productCategory : productCategoryList){
            //根据productCategory拼装ProductVO
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());
            //将ProductInfo放入productInfoVOList
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for(ProductInfo productInfo : productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            //将productInfoVOList放入productVO
            productVO.setProductInfoVOList(productInfoVOList);
            //将productVO放入productVOList
            productVOList.add(productVO);
        }
        //根据productVOList生成ResultVO
        return ResultVOUtil.success(productVOList);
    }


}
