<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px; margin-left: 10px; margin-top: 10px; font-size: 30px">会员详情</div>
    <el-form :inline="true" :model="form" style="width: 80%" label-width="150px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" disabled></el-input> <!--disable/readonly-->
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age" disabled></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.sex" disabled></el-input>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.phone" disabled></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" disabled></el-input>
      </el-form-item>
      <el-form-item label="注册时间">
        <el-input v-model="form.createtime" disabled></el-input>
      </el-form-item>
      <el-form-item label="更新时间">
        <el-input v-model="form.updatetime" disabled></el-input>
      </el-form-item>

    </el-form>
    <div style="text-align: center; margin-top: 30px">
      <el-button type="primary" @click="back" size="medium">返回</el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Check",
  data() {
    return {
      form: {}
    }
  },
  created() {
    const id = this.$route.query.id
    request.get('/user/' + id).then(res => {
      this.form = res.data
    })
  },
  methods: {
    back() {
      this.$router.push("/userList")
    }
  }
}
</script>
