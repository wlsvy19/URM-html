<template>
  <div class="urm-list" >
    <div class="search-bar">
        <div class="search-buttons">
          <el-button @click="search">다시읽기</el-button>
          <el-button @click="clickDelete('selected')" type="danger" plain>{{$t('label.delete')}}</el-button>
        </div>
    </div>
    <el-table ref="table" :data="items" @row-dblclick="handleRowDblclick" border stripe>
      <el-table-column type="selection" width="40"/>
      <el-table-column label="매핑 ID" prop="id" width="135px"/>
      <el-table-column label="매핑 명" prop="name" width="180px"/>
      <el-table-column label="송신데이터" prop="sourceDataId" width="130px"/>
      <el-table-column label="수신데이터" prop="targetDataId" width="130px"/>
      <el-table-column label="사용 요건 ID" prop="requestId" width="145px"/>
      <el-table-column label="사용 요건 명" prop="requestName"/>
      <el-table-column label="사용 인터페이스 ID" prop="interfaceId" width="165px"/>
    </el-table>
  </div>
</template>

<script>
import RuleList from '@/components/rule/list/RuleList'

export default {
  mixins: [RuleList],
  data () {
    return {
      path: 'datamap',
    }
  },
}
</script>