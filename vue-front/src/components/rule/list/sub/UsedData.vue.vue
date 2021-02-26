<template>
  <div class="urm-list">
    <el-table ref="table" border class="table-striped">
      <el-table-column :label="$t('label.requestId')" prop="reqId" width="150"/>
      <el-table-column :label="$t('label.requestName')" prop="reqName"/>
      <el-table-column :label="$t('label.dataMapId')" prop="mapId" width="150"/>
      <el-table-column :label="$t('label.dataMapName')" prop="mapName"/>
    </el-table>
  </div>
</template>