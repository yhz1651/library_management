<template>
  <div>
    <!--  搜索表单  -->
    <div style="margin-bottom: 5px">
      <el-input style="width: 240px" placeholder="请输入用户名" v-model="params.username"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入邮箱" v-model="params.email"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <!--  表格  -->
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="编号" width="100"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="phone" label="联系方式"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="updatetime" label="更新时间"></el-table-column>
      <el-table-column label="状态" width="230">
        <!--    开关    -->
        <!--scope.row 是当前行数据-->
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              @change="changeStatus(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="350">
        <template v-slot="scope">
          <!--          scope.row 是当前行数据-->
          <el-button type="primary" @click="$router.push('/editAdmin?id='+scope.row.id)">编辑</el-button>

          <!--    确认框      -->
          <el-popconfirm
              style="margin-left: 10px"
              title="确定删除这行数据吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
          <el-button style="margin-left: 5px" type="warning" @click="handleChangePass(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--  分页  -->
    <div style="margin-top: 20px">
      <el-pagination
          background
          :current-page="params.pageNum"
          :page-size="params.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          layout="prev, pager, next">
      </el-pagination>
    </div>

    <!--  修改密码弹窗  -->
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="新密码" prop="newPass">
          <el-input v-model="form.newPass" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePass">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'List',

  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {}, // cookies中的数据
      tableData: [], // 表格数据
      total: 0, // 表格数据总数
      form: {}, // 表单
      dialogFormVisible: false, // 弹窗开关
      params: { // 参数初始化
        pageNum: 1,
        pageSize: 10,
        username: '',
        phone: '',
        email: ''
      },
      rules: {
        newPass: [
          {required: true, message: '请输入新密码', trigger: ['blur', 'change']},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: ['blur', 'change']}
        ]
      },
    }
  },

  created() {
    this.load()
  },

  methods: {
    changeStatus(row){ // 禁用或启用账号
      if (this.admin.id === row.id && !row.status) { // 禁用自己的账号
        row.status = true
        this.$notify.warning('您的操作不合法')
        return
      }
      request.put('/admin/update', row).then(res => {
        if (res.code === '200') {
          this.$notify.success('操作成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },

    load() { // 加载数据
      request.get('/admin/page', {
        params: this.params // 设置参数，自动拼接url参数
      }).then(res => {
        if (res.code === '200') { // 当成功返回数据时再加载
          this.tableData = res.data.list // 数据
          this.total = res.data.total // 数据条数
        }
      })
    },

    reset() {
      // 重置并加载
      this.params = { // 参数初始化
        pageNum: 1,
        pageSize: 10,
        username: '',
        phone: '',
        email: ''
      }
      this.load(); // 重新加载数据
    },

    handleChangePass(row) { // 修改密码
      this.form = JSON.parse(JSON.stringify(row)) // 保存当前用户的信息
      this.dialogFormVisible = true // 弹窗显示
    },

    savePass() {
      this.$refs["formRef"].validate((valid) => { // 先进行表单验证
        if (valid) {
          request.put('/admin/password', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success("修改成功")
              if (this.form.id === this.admin.id) { // 当前修改的用户id等于当前登录的管理员id，那么修改成功之后需要重新登录（修改自己账号的密码）
                Cookies.remove('admin') // 删除缓存
                this.$router.push('/login') // 跳转到登录界面
                this.$notify.success("请重新登录")
              } else {
                this.load() // 重新加载数据
                this.dialogFormVisible = false // 关闭弹窗
              }
            } else {
              this.$notify.error("修改失败")
            }
          })

        }
      })

    },

    handleCurrentChange(pageNum) { // 点击页码按钮进行分页
      this.params.pageNum = pageNum; // 获取页码
      this.load(); // 重新加载
    },

    del(id) {
      request.delete("/admin/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success('删除成功')
          this.load(); // 重新加载
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
