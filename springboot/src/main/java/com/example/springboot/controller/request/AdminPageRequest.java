package com.example.springboot.controller.request;

import lombok.Data;

@Data
public class AdminPageRequest extends BaseRequest{ // 继承pageNum和pageSize
    // 定义其他需要的字段
    private String username;
    private String phone;
    private String email;

}
