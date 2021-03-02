<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-input label="담당자" class="search-id" readonly/>
        <el-input label="이관자" class="search-id" readonly/>
      </el-form>
      <div class="search-buttons">
        <el-button>이관</el-button>
      </div>
    </div>
  </div>
</template>