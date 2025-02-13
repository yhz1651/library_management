package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.LoginDTO;
import com.example.springboot.controller.request.AdminPageRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.controller.request.UserPageRequest;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.IAdminService;
import com.example.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin // 实现跨域
@RestController // restful风格的controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    IAdminService AdminService;

    @PutMapping("/password") // 修改密码
    public Result password(@RequestBody PasswordRequest request) {
        AdminService.changePass(request);
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request) {
        return Result.success(AdminService.login(request));
    }

    @PostMapping("/save") // 增 insert
    public Result save(@RequestBody Admin admin) {
        AdminService.save(admin);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}") // 删 delete
    public Result delete(@PathVariable Integer id) { // restful风格，url不带参数名字，只带参数值
        AdminService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update") // 改 update
    public Result update(@RequestBody Admin admin) {
        admin.setUpdatetime(new Date());
        AdminService.update(admin);
        return Result.success();
    }

    @GetMapping("/{id}") // 查 select
    public Result getById(@PathVariable Integer id) {
        Admin admin = AdminService.getById(id);
        return Result.success(admin);
    }

    @GetMapping("/list")
    public Result list() {
        List<Admin> list = AdminService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result page(AdminPageRequest PageRequest) { // 不能使用基类，否则只能传pagenum和pagesize，还需要传其他多个条件
        return Result.success(AdminService.page(PageRequest));
    }
}
