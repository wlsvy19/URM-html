<template>
  <div class="urm-list">
    <div class="row" v-if="byEditor">
      <span style="line-height: 33px;">{{$t('message.0008')}}</span>
      <el-button @click="clickConfirm" class="used-confirm">{{$t('label.confirm')}}</el-button>
    </div>
    <el-table :data="data" height="500" border stripe :row-key="$randomRowKey">
      <el-table-column :label="$t('label.requestId')" prop="reqId" width="150"/>
      <el-table-column :label="$t('label.requestName')" prop="reqName"/>
      <el-table-column :label="$t('label.dataMapId')" prop="mapId" width="150"/>
      <el-table-column :label="$t('label.dataMapName')" prop="mapName"/>
    </el-table>
  </div>
</template>
<script>
export default {
  props: {
    byEditor: {
      type: Boolean,
      default: false,
    },
    data: {
      type: Array,
      defulat: function () {
        return []
      },
    },
  },
  methods: {
    clickConfirm () {
      this.$emit('confirm')
    },
  },
}
</script>
<style scoped>
.used-confirm {
  margin-left: auto;
  margin-right: 20px;
}
</style>