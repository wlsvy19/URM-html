<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="일자 선택">
          <el-date-picker type="daterange" start-placeholder="Start Date" end-placeholder="End Date" style="width: 220px;"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <ChangeList ref="list" @edit="handleEdit"/>
  </div>
</template>

<script>
import ChangeList from './list/RequestChangeList'

export default {
  components: {
    ChangeList,
  }
}
</script>