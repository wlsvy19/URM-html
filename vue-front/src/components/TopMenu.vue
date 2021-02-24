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
        <el-menu-item index="biz">{{$t('urm.biz')}}</el-menu-item>     
      </el-submenu>
      <el-submenu index="Stat">
        <template slot="title">{{$t('urm.stat')}}</template>
        <el-menu-item-group>
          <span slot="title">요건진행상태</span>
          <el-menu-item index="process1">일별 진행상태</el-menu-item>
          <el-menu-item index="process2">월별 진행상태</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group>
          <span slot="title">요건변경상태</span>
          <el-menu-item index="change1">일별 변경상태</el-menu-item>
          <el-menu-item index="change2">월별 변경상태</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="">
        <template slot="title">{{$t('urm.process')}}</template>
          <el-menu-item-group>
            <span slot="title">거래처리로그</span>
              <el-submenu index="ldev">
                <template slot="title">개발</template>
                <el-menu-item index="dlog1">REALTIME</el-menu-item>
                <el-menu-item index="dlog2">BATCH</el-menu-item>
                <el-menu-item index="dlog3">DEFERRED</el-menu-item>
              </el-submenu>
              <el-submenu index="ltest">
                <template slot="title">테스트</template>
                <el-menu-item index="tlog1">REALTIME</el-menu-item>
                <el-menu-item index="tlog2">BATCH</el-menu-item>
                <el-menu-item index="tlog3">DEFERRED</el-menu-item>
              </el-submenu>
              <el-submenu index="lprod">
                <template slot="title">운영</template>
                <el-menu-item index="plog1">REALTIME</el-menu-item>
                <el-menu-item index="plog2">BATCH</el-menu-item>
                <el-menu-item index="plog3">DEFERRED</el-menu-item>
              </el-submenu>
          </el-menu-item-group>

          <el-menu-item-group>
            <span slot="title">거래처리통계</span>
              <el-submenu index="sdev">
                <template slot="title">개발</template>
                <el-menu-item index="dstat1">시간별 REALTIME</el-menu-item>
                <el-menu-item index="dstat2">시간별 BATCH</el-menu-item>
                <el-menu-item index="dstat3">시간별 DEFERRED</el-menu-item>
                <el-menu-item index="dstat4">일자별 REALTIME</el-menu-item>
                <el-menu-item index="dstat5">일자별 BATCH</el-menu-item>
                <el-menu-item index="dstat6">일자별 DEFERRED</el-menu-item>
              </el-submenu>
              <el-submenu index="stest">
                <template slot="title">테스트</template>
                <el-menu-item index="tstat1">시간별 REALTIME</el-menu-item>
                <el-menu-item index="tstat2">시간별 BATCH</el-menu-item>
                <el-menu-item index="tstat3">시간별 DEFERRED</el-menu-item>
                <el-menu-item index="tstat4">일자별 REALTIME</el-menu-item>
                <el-menu-item index="tstat5">일자별 BATCH</el-menu-item>
                <el-menu-item index="tstat6">일자별 DEFERRED</el-menu-item>
              </el-submenu>
              <el-submenu index="sprod">
                <template slot="title">운영</template>
                <el-menu-item index="pstat1">시간별 REALTIME</el-menu-item>
                <el-menu-item index="pstat2">시간별 BATCH</el-menu-item>
                <el-menu-item index="pstat3">시간별 DEFERRED</el-menu-item>
                <el-menu-item index="pstat4">일자별 REALTIME</el-menu-item>
                <el-menu-item index="pstat5">일자별 BATCH</el-menu-item>
                <el-menu-item index="pstat6">일자별 DEFERRED</el-menu-item>
              </el-submenu>
          </el-menu-item-group>


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