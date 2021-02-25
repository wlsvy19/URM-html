<template>
  <div class="urm-list">
    <el-table ref="table" :height="listHeight" border class="table-striped">
      <el-table-column label="처리날짜" prop="processDate" />
      <el-table-column label="인터페이스ID" prop="interfaceId" width="200"/>
      <el-table-column label="총건수" prop="totalCount"/>
      <el-table-column label="성공건수" prop="successCount"/>
      <el-table-column label="실패건수" prop="failCount"/>
      <el-table-column label="평균처리시간" prop="processTime"/>
    </el-table>
  </div>
</template>