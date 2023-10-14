package com.pro.sell.handler;


import com.pro.sell.config.ProjectUrlConfig;
import com.pro.sell.execption.ResponseBankException;
import com.pro.sell.execption.SellException;
import com.pro.sell.execption.SellerAuthorizeException;
import com.pro.sell.utils.ResultVOUtil;
import com.pro.sell.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常，出现异常时，使用微信重新登录
    //http://onedir.nat300.top/sell/wechat/qrAuthorize?returnUrl=http://onedir.nat300.top/sell/seller/login
//    @ExceptionHandler(value = SellerAuthorizeException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ModelAndView handlerAuthorizeException() {
//        return new ModelAndView("redirect:"
//        .concat(projectUrlConfig.getWechatOpenAuthorize())
//        .concat("/sell/wechat/qrAuthorize")
//        .concat("?returnUrl=")
//        .concat(projectUrlConfig.getSell())
//        .concat("/sell/seller/login"));
//    }

    //拦截登录异常，出现异常时，自动重新登录
    @ExceptionHandler(value = SellerAuthorizeException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlerAuthorizeException() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "登录状态异常！");
        map.put("url", "/sell/seller/login?openid=abc");
        return new ModelAndView("common/error", map);
    }

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVO handlerSellerException(SellException e) {
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }

    //@ResponseStatus: 返回指定HttpStatus
    @ExceptionHandler(value = ResponseBankException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleResponseBankException() {

    }
}
