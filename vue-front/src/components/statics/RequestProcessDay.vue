<template>
  <div class="urm-panel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="sparam.type" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in infTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item label="μΌμ μ ν">
          <el-date-picker v-model="dateRange" type="daterange" value-format="yyyyMMdd" class="no-suffix" :clearable="false" :picker-options="pickerOptions"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>

    <RequestProcessList :items="listItem"/>
  </div>
</template>

<script>
import StaticsMain from './StaticsMain'

export default {
  mixins: [StaticsMain],
  data () {
    return {
      sparam: {
        ...this.sparam,
        type: '',
      },
      pageUrl: '/process/day',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      }
    }
  },
  mounted () {
    let today = this.$convertDateFormat('yyyyMMdd')
    this.sparam.startDate = today
    this.sparam.endDate = today
  },
}
</script>