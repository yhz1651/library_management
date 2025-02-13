<template>
  <div style="height: 100vh; overflow: hidden; position: relative">

    <el-card class="cover" v-if="loginAdmin.id"><!--   账号密码正确时弹出滑块验证  -->
      <!--   滑块验证   -->
      <slide-verify :l="42"
                    :r="10"
                    :w="310"
                    :h="155"
                    slider-text="向右滑动"
                    @success="onSuccess"
                    @fail="onFail"
                    @refresh="onRefresh"
      ></slide-verify>
    </el-card>

    <div style="width: 500px; height: 400px; background-color: white; border-radius: 10px;
    margin: 150px auto; padding: 50px">
      <div style="margin: 30px; text-align: center; font-size: 30px; font-weight: bold; color: dodgerblue">登 录</div>
      <el-form :model="admin" :rules="rules" ref="loginForm">
        <el-form-item prop="username">  <!--  prop中字段和rules中字段对应  -->
          <el-input placeholder="请输入账号" size="medium" prefix-icon="el-icon-user" v-model="admin.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" show-password size="medium" prefix-icon="el-icon-lock" v-model="admin.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; margin-top: 20px" size="medium" type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import Cookies from 'js-cookie'

export default {
  name: "Login",
  data() {
    return {
      loginAdmin: {},
      admin: {},
      rules: { // 表单验证
        username: [
          {required: true, message: '请输入姓名', trigger: 'blur'}, //
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}, //
          {min: 3, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs["loginForm"].validate((valid) => { // 表单验证
        if (valid) {
          request.post('/admin/login', this.admin).then(res => {
            if (res.code === '200') { // 成功
              this.loginAdmin = res.data  // 滑块组件就出现了
            } else { // 失败
              this.$notify.error(res.msg) // 显示错误信息
            }
          })
        }
      })
    },
    onSuccess() { // 滑块验证通过之后触发的
      Cookies.set('admin', JSON.stringify(this.loginAdmin)) // 将当前登录的用户信息存到cookies里
      this.$notify.success("登录成功")
      this.$router.push('/') // 跳转到主页
    },
    onFail() {
      console.log('onFail')
    },
    onRefresh() {
      console.log('refresh')
    }
  }
}
</script>

<style> /*页面居中置顶*/
.cover {
  width: fit-content;
  background-color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
</style>
