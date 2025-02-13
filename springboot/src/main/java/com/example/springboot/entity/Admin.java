package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

// lombok自动生成getter和setter方法
@Data
public class Admin {
    private Integer id;
    private String username;
    private String phone;
    private String password;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updatetime;
    private boolean status;

}
