package com.example.common.enums;

public enum OrderStatusEnum {


    CANCEL("已取消"),
    NOTPAY("待支付"),
    NOTSENG("待发货"),
    NOTRECEIVE("待收货"),
    DONE("已完成");


    public  String value;
    OrderStatusEnum(String value){
        this.value=value;
    }

}
