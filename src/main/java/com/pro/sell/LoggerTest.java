package com.pro.sell;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author RenHao
 * @create 2023-08-24 11:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    @Test
    public void test1(){
        String name = "sell";
        String password = "123456";
        log.debug("debug...");
        log.info("name: " + name + " password: " + password);
        log.info("name: {}, password: {}", name, password);
        log.error("error...");
    }

}
