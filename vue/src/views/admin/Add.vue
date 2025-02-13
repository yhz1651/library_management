<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px; margin-left: 10px; margin-top: 10px; font-size: 30px">新增管理员</div>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" style="width: 80%" label-width="150px">
      <el-form-item label="用户名" prop="username"> <!--prop和rules对应-->
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码" type="password"></el-input>
      </el-form-item>
      <el-form-item label="重复密码" prop="password2">
        <el-input v-model="form.password2" placeholder="请再次输入密码" type="password"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
    </el-form>

    <div style="text-align: center; margin-top: 30px">
      <el-button type="primary" @click="save" size="medium">提交</el-button>
    </div>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Add",
  data() {
    // 手机号验证
    const checkPhone = (rule, value, callback) => {
      if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) { // 手机号正则表达式
        callback(new Error('请输入合法的手机号'));
      }
      callback() // callback一定要写
    };
    // 邮箱验证
    const checkEmail = (rule, value, callback) => {
      if (!/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(value)) { // 邮箱正则表达式
        callback(new Error('请输入合法的邮箱'));
      }
      callback()
    };

    // 密码验证
    const validatePassword = (rule, value, callback) => {
      if (value === '') { // 为空
        callback(new Error('请输入密码'));
      } else {
        if (this.form.password2 !== '') {
          this.$refs.ruleForm.validateField('password2'); // 和重复密码绑定
        }
        callback();
      }
    };
    // 重复密码验证
    const validatePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.form.password) { // 比对密码
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      form: {sex: '男'}, // 默认值，不设置则为空
      // 表单验证
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}, //
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ],
        phone: [/* 自定义验证方式 */
          {required: true, validator: checkPhone, trigger: 'blur'} // 自定义校验
        ],
        email: [
          {required: true, validator: checkEmail, trigger: 'blur'}
        ],
        password: [
          {required: true, validator: validatePassword, trigger: ['blur', 'change']},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: ['blur', 'change']}
        ],
        password2: [
          {required: true, validator: validatePassword2, trigger: ['blur', 'change']}
        ],
      }
    }
  },
  methods: {
    save() {
      this.$refs["ruleForm"].validate((valid) => { // 先进行表单验证
        if (valid) {
          request.post('/admin/save', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增成功')
              // 第一种方法
              // this.form = {}
              // 第二种方法 推荐！
              this.$refs["ruleForm"].resetFields(); // 清空表单
            } else {
              this.$notify.error(res.msg)
            }
          })
        } else {

        }
      })

    }
  }
}
</script>
