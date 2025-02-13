package com.example.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.LoginDTO;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.service.IAdminService;
import com.example.springboot.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdminService implements IAdminService { // 继承接口

    private static final String DEFAULT_PASS = "123456";
    private static final String PASS_SALT = "yhzyhz";

    // Autowired引入其他的类或者接口，可以直接调用方法
    @Autowired
    AdminMapper adminMapper;

    @Override
    public List<Admin> list() {
        return adminMapper.list();
    }


    @Override
    public PageInfo<Admin> page(BaseRequest baseRequest) { // 多条件分页模糊查询
        /*
            PageHelper.startPage()方法特点：
             1.静态方法，传递两个参数（当前页码，每页查询条数）
             2.使用pageHelper 分页的时候，不再关注具体的分页语句，查询全部的语句
             3.自动的对PageHelper.startPage()方法下一行的第一个sql 查询进行分页
        */
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());// 在PageHelper.startPage()方法后面一定要紧跟查询语句
        List<Admin> users = adminMapper.listByCondition(baseRequest);
        return new PageInfo<>(users); // 返回pageInfo对象
    }

    @Override
    public void save(Admin obj) {
//        if (StrUtil.isBlank(obj.getPassword())) {
//            obj.setPassword(DEFAULT_PASS); // 设置默认密码
//        }
        obj.setPassword(securePass(obj.getPassword())); // 设置md5加密

        // 判断用户名是否重复
        try {
            adminMapper.save(obj);
        } catch (DuplicateKeyException e) {
            log.error("数据插入失败， username:{}", obj.getUsername(), e);
            throw new ServiceException("用户名重复");
        }
    }

    @Override
    public Admin getById(Integer id) {
        return adminMapper.getById(id);
    }

    @Override
    public void update(Admin obj) {
        adminMapper.updateById(obj);
    }

    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    @Override
    public LoginDTO login(LoginRequest request) {
        Admin admin = null;
        try {
            admin = adminMapper.getByUsername(request.getUsername());
        } catch (Exception e) {
            log.error("根据用户名{}查询出错", request.getUsername());
            throw new ServiceException("用户名错误");
        }

        if (admin == null) {
            throw new ServiceException("用户名或密码错误");
        }

        // 判断密码是否合法
        String securePass = securePass(request.getPassword()); // md5 加密
        log.error("密码{}",securePass);
        if (!securePass.equals(admin.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        if (!admin.isStatus()) {
            throw new ServiceException("当前用户处于禁用状态，请联系管理员");
        }
        LoginDTO loginDTO = new LoginDTO();
        // springboot自带的BeanUtil类，将admin赋值给logindto（只赋值logindto拥有的变量）
        BeanUtils.copyProperties(admin, loginDTO);

        // 生成token
        String token = TokenUtils.genToken(String.valueOf(admin.getId()), admin.getPassword()); // 通过id和密码生成token
        loginDTO.setToken(token);

        return loginDTO;
    }

    @Override
    public void changePass(PasswordRequest request) {
        // 注意要对新的密码进行加密
        request.setNewPass(securePass(request.getNewPass()));
        // count为影响的行数
        int count = adminMapper.updatePassword(request);
        if (count <= 0) {
            throw new ServiceException("修改失败");
        }
    }

    private String securePass(String password) {
        // md5 加密密码
        return SecureUtil.md5(password);
    }
}
