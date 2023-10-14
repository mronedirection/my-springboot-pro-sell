package com.pro.sell.utils;


import com.pro.sell.enums.CodeEnum;

/**
 * 根据code获取对应的枚举类
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
