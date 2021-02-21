<template>
  <div>
    <el-menu :default-active="activePage" mode="horizontal" @select="handleSelect">
      <el-menu-item index="main"><img src="@/assets/logo.gif"/></el-menu-item>
      <el-menu-item index="request">{{$t('urm.request')}}</el-menu-item>
      <el-menu-item index="data">{{$t('urm.data')}}</el-menu-item>
      <el-menu-item index="system">{{$t('urm.system')}}</el-menu-item>
      <el-submenu index="Manage">
        <template slot="title">{{$t('urm.manage')}}</template>
        <el-menu-item index="user">{{$t('urm.user')}}</el-menu-item>
      </el-submenu>
    </el-menu>
  </div>
</template>
<script>
export default {
  data () {
    return {
      activePage: 'main',
    }
  },

  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
      this.$router.push(key)
    }, // handleSelect
  }, // methods

  mounted () {
    let routePath = this.$route.path
    console.log('TopMenu mounted', routePath)
    if (routePath && routePath.length > 0) {
      this.activePage = routePath.substring(1)
      if (this.activePage === '')  {
        this.activePage = 'main'
      }
    } else {
      this.activePage = 'main'
    }
    console.log('mounted TopMenu')
  }, // mounted

  computed: {
    hasAuth: function () {
      return true
    }
  },
}
</script>