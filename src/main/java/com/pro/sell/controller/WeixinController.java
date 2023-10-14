package com.pro.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 微信授权逻辑，不使用SDK
 * 1.在微信客户端访问：https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
 * 2.修改上面链接中的三个必要参数appId, redirect_uri, scope, 其中redirect_uri需要用到内网穿透工具，使得本机可以当作一个服务器被访问
 * 3.访问上述接口后，会自动回调redirect_uri，进而访问该controller，从而拿到openid
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入auth方法。。。");
        log.info("code={}", code );

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxc04e98f5d9a97247&secret=e05e76975febf731eab62c92e13d0d0b&code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);
    }
}
