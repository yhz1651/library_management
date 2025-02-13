package com.example.springboot.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 所有参数的构造函数
@NoArgsConstructor // 无参数的构造函数
public class LoginRequest {
    private String username;
    private String password;

}
