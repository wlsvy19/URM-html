<template>
  <div class="urm-panel">
    <div class="urm-header">디퍼드 거래처리로그</div>
    <div class="search-bar" :style="proLogStyle(this.$route.params)">
      <el-form :inline="true">
        <el-form-item label="처리날짜">
          <el-date-picker type="daterange" style="width: 220px;"/>
        </el-form-item>
        <el-form-item label="인터페이스 아이디">
          <el-input class="search-id"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <LogDeferredList ref="list" @edit="handleEdit"/>
  </div>
</template>

<script>

export default {
  methods: {
    proLogStyle (val) {
      let type = val.server
      let style= ''
      if(type === 'dev') {
        style = 'backgroundColor: #8888ff'
      }else if(val.server === 'test') {
        style = 'backgroundColor: #88ff88'
      } else if(val.server === 'prod') {
        style = 'backgroundColor: #ff8888'
      }
      return style
      } // proLogStyle
  }
}
</script>