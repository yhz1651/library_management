package com.example.springboot.controller.dto;

import lombok.Data;

@Data
public class LoginDTO { // 登录时一些的用户信息
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private String token; // token

}
