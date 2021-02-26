<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="sparam.interfaceType" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item label="일자 선택">
          <el-date-picker v-model="value1" type="monthrange" range-separator="~" start-placeholder="Start month" end-placeholder="End month" style="width: 220px"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <ProcessList ref="list" @edit="handleEdit"/>
  </div>
</template>



<script>
import ProcessList from './list/RequestProcessList'

export default {
  data () {
    return {
      sparam: {
        ...this.sparam,
        type: '',
      },
    }
  },
  components: {
    ProcessList,
  },
  methods: {

  }
}
</script>