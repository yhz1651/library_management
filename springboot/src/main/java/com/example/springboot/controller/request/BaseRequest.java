package com.example.springboot.controller.request;

import lombok.Data;

@Data
public class BaseRequest {
    // 定义默认值
    private Integer pageNum = 1; // 页码
    private Integer pageSize = 10; // 页面大小
}
