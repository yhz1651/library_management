<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px; margin-left: 10px; margin-top: 10px; font-size: 30px">编辑管理员</div>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" style="width: 80%" label-width="150px">
      <el-form-item label="用户名" prop="username"> <!--prop和rules对应-->
        <el-input v-model="form.username" placeholder="请输入用户名" disabled></el-input>
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
  name: "Edit",
  data() {
    return {
      form: {}
    }
  },
  created() {
    const id = this.$route.query.id // 获取传过来的url里的id
    request.get('/admin/' + id).then(res => {
      this.form = res.data // 数据初始化渲染
    })
  },
  methods: {
    save() {
      request.put('/admin/update', this.form).then(res => {
        if (res.code === '200') {
          this.$notify.success('更新成功')
          this.$router.push("/adminList") // 跳转到list界面
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
