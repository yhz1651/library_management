package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Category;
import com.example.springboot.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> list();

    List<Category> listByCondition(BaseRequest baseRequest);// 用基类实现

    void save(Category obj);

    Category getById(Integer id);

    void updateById(Category obj);

    void deleteById(Integer id);

//    Category getByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
//
//    int updatePassword(PasswordRequest request);
//
//    Category getByUsername(String username);
}
