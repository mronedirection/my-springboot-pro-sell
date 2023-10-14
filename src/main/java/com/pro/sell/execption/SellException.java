package com.pro.sell.execption;


import com.pro.sell.enums.ResultEnum;
import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
