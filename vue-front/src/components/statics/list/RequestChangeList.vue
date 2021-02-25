<template>
  <div class="urm-list">
    <el-table ref="table" :height="listHeight" border class="table-striped">
      <el-table-column label="변경일자" prop="chgDate" width="150"/>
      <el-table-column label="변경요청">
        <el-table-column label="온라인" prop="stat1o" />
        <el-table-column label="배치" prop="stat1b" />
        <el-table-column label="디퍼드" prop="stat1d"/>
        <el-table-column label="TOTAL" prop="stat1" />
      </el-table-column>
      <el-table-column label="변경완료">
        <el-table-column label="온라인" prop="stat2o"/>
        <el-table-column label="배치" prop="stat2b"/>
        <el-table-column label="디퍼드" prop="stat2d"/>
        <el-table-column label="TOTAL" prop="stat2"/>
      </el-table-column>
      <el-table-column label="삭제요청">
        <el-table-column label="온라인" prop="stat3o"/>
        <el-table-column label="배치" prop="stat3b"/>
        <el-table-column label="디퍼드" prop="stat3d"/>
        <el-table-column label="TOTAL" prop="stat3"/>
      </el-table-column>
      <el-table-column label="삭제완료">
        <el-table-column label="온라인" prop="stat4o"/>
        <el-table-column label="배치" prop="stat4b"/>
        <el-table-column label="디퍼드" prop="stat4d"/>
        <el-table-column label="TOTAL" prop="stat4"/>
      </el-table-column>
    </el-table>
  </div>
</template>