<template>
  <div class="urm-list">
    <el-table :data="items" :height="listHeight" border stripe>
      <el-table-column label="처리날짜" prop="processDate" min-width="90"/>
      <el-table-column label="인터페이스ID" prop="interfaceId" width="150"/>
      <el-table-column label="항목" prop="countType" min-width="115"/>
      <el-table-column label="00" prop="hour00"/>
      <el-table-column label="01" prop="hour01"/>
      <el-table-column label="02" prop="hour02"/>
      <el-table-column label="03" prop="hour03"/>
      <el-table-column label="04" prop="hour04"/>
      <el-table-column label="05" prop="hour05"/>
      <el-table-column label="06" prop="hour06"/>
      <el-table-column label="07" prop="hour07"/>
      <el-table-column label="08" prop="hour08"/>
      <el-table-column label="09" prop="hour09"/>
      <el-table-column label="10" prop="hour10"/>
      <el-table-column label="11" prop="hour11"/>
      <el-table-column label="12" prop="hour12"/>
      <el-table-column label="13" prop="hour13"/>
      <el-table-column label="14" prop="hour14"/>
      <el-table-column label="15" prop="hour15"/>
      <el-table-column label="16" prop="hour16"/>
      <el-table-column label="17" prop="hour17"/>
      <el-table-column label="18" prop="hour18"/>
      <el-table-column label="19" prop="hour19"/>
      <el-table-column label="20" prop="hour20"/>
      <el-table-column label="21" prop="hour21"/>
      <el-table-column label="22" prop="hour22"/>
      <el-table-column label="23" prop="hour23"/>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      default: () => [],
    },
  },
  data () {
    return {
      listHeight: 'calc(100vh - 195px)',
    }
  },
}
</script>