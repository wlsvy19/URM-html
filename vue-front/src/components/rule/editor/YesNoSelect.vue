<template>
  <el-select v-model="data">
    <el-option :value="true" label="Yes"/>
    <el-option :value="false" label="No"/>
  </el-select>
</template>
<script>
export default {
  props: {
    data: {
      type: Boolean,
      default: true,
    }
  },
}
</script>