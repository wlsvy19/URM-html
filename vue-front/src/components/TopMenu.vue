<template>
  <div>
    <el-menu :default-active="activePage" mode="horizontal" @select="handleSelect">
      <el-menu-item index="main"><img src="@/assets/logo.gif"/></el-menu-item>
      <el-menu-item index="/request">{{$t('urm.request')}}</el-menu-item>
      <el-menu-item index="/data">{{$t('urm.data')}}</el-menu-item>
      <el-menu-item index="/system">{{$t('urm.system')}}</el-menu-item>
      <el-submenu index="Manage">
        <template slot="title">{{$t('urm.manage')}}</template>
        <el-menu-item index="/user">{{$t('urm.user')}}</el-menu-item>
        <el-menu-item index="/biz">{{$t('urm.biz')}}</el-menu-item>     
      </el-submenu>
      <el-submenu index="Statics">
        <template slot="title">{{$t('urm.stat')}}</template>
        <el-menu-item-group title="요건진행상태">
          <el-menu-item index="/process/day">일별 진행상태</el-menu-item>
          <el-menu-item index="/process/month">월별 진행상태</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="요건변경상태">
          <el-menu-item index="/change/day">일별 변경상태</el-menu-item>
          <el-menu-item index="/change/month">월별 변경상태</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="Process">
        <template slot="title">{{$t('urm.process')}}</template>
          <el-menu-item-group title="거래처리로그">
            <el-submenu index="ldev">
              <template slot="title">개발</template>
              <el-menu-item index="/log/online/dev">REALTIME</el-menu-item>
              <el-menu-item index="/log/batch/dev">BATCH</el-menu-item>
              <el-menu-item index="/log/deferred/dev">DEFERRED</el-menu-item>
            </el-submenu>
            <el-submenu index="ltest">
              <template slot="title">테스트</template>
              <el-menu-item index="/log/online/test">REALTIME</el-menu-item>
              <el-menu-item index="/log/batch/test">BATCH</el-menu-item>
              <el-menu-item index="/log/deferred/test">DEFERRED</el-menu-item>
            </el-submenu>
            <el-submenu index="lprod">
              <template slot="title">운영</template>
              <el-menu-item index="/log/online/prod">REALTIME</el-menu-item>
              <el-menu-item index="/log/batch/prod">BATCH</el-menu-item>
              <el-menu-item index="/log/deferred/prod">DEFERRED</el-menu-item>
            </el-submenu>
          </el-menu-item-group>

          <el-menu-item-group title="거래처리통계">
            <el-submenu index="sdev">
              <template slot="title">개발</template>
              <el-menu-item index="/stat/hour/online/dev">시간별 REALTIME</el-menu-item>
              <el-menu-item index="/stat/hour/batch/dev">시간별 BATCH</el-menu-item>
              <el-menu-item index="/stat/hour/deferred/dev">시간별 DEFERRED</el-menu-item>
              <el-menu-item index="/stat/day/online/dev">일자별 REALTIME</el-menu-item>
              <el-menu-item index="/stat/day/batch/dev">일자별 BATCH</el-menu-item>
              <el-menu-item index="/stat/day/deferred/dev">일자별 DEFERRED</el-menu-item>
            </el-submenu>
            <el-submenu index="stest">
              <template slot="title">테스트</template>
              <el-menu-item index="/stat/hour/online/test">시간별 REALTIME</el-menu-item>
              <el-menu-item index="/stat/hour/batch/test">시간별 BATCH</el-menu-item>
              <el-menu-item index="/stat/hour/deferred/test">시간별 DEFERRED</el-menu-item>
              <el-menu-item index="/stat/day/online/test">일자별 REALTIME</el-menu-item>
              <el-menu-item index="/stat/day/batch/test">일자별 BATCH</el-menu-item>
              <el-menu-item index="/stat/day/deferred/test">일자별 DEFERRED</el-menu-item>
            </el-submenu>
            <el-submenu index="sprod">
              <template slot="title">운영</template>
              <el-menu-item index="/stat/hour/online/prod">시간별 REALTIME</el-menu-item>
              <el-menu-item index="/stat/hour/batch/prod">시간별 BATCH</el-menu-item>
              <el-menu-item index="/stat/hour/deferred/prod">시간별 DEFERRED</el-menu-item>
              <el-menu-item index="/stat/day/online/prod">일자별 REALTIME</el-menu-item>
              <el-menu-item index="/stat/day/batch/prod">일자별 BATCH</el-menu-item>
              <el-menu-item index="/stat/day/deferred/prod">일자별 DEFERRED</el-menu-item>
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