<template>
  <div class="urm-list">
    <el-table :data="items" :height="listHeight" border stripe :row-key="$randomRowKey">
      <el-table-column label="변경일자" prop="chgDate" width="130"/>
      <el-table-column label="요건신청" prop="stat1" min-width="85"/>
      <el-table-column label="재작성요청" prop="stat2" min-width="100"/>
      <el-table-column label="접수완료" prop="stat3" min-width="85"/>
      <el-table-column label="반려" prop="stat4" min-width="55"/>
      <el-table-column label="요건등록완료" prop="stat5" width="130"/>
      <el-table-column label="대표거래테스트" prop="stat6" width="130"/>
      <el-table-column label="테스트중" prop="stat7" min-width="85"/>
      <el-table-column label="테스트완료" prop="stat8" min-width="100"/>
      <el-table-column label="테스트이관요청" prop="stat9" width="130"/>
      <el-table-column label="테스트이관" prop="stat10" min-width="100"/>
      <el-table-column label="운영이관요청" prop="stat11" width="130"/>
      <el-table-column label="운영이관" prop="stat12" min-width="85"/>
      <el-table-column label="TOTAL" prop="count"/>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      default: () => [],
    }
  },
  data () {
    return {
      listHeight: 'calc(100vh - 165px)',
    }
  },
}
</script>