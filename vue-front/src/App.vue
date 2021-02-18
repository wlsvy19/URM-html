<template>
  <div id="app">
    <TopMenu/>
    <div>
      <keep-alive>
        <router-view/>
      </keep-alive>
    </div>
  </div>
</template> 

<script>
import TopMenu from '@/components/TopMenu'

export default {
  name: 'App',
  components: {
    TopMenu,
  },

  created () {
    this.$reloadConfig()
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  color: #2c3e50;
}

.urm-panel {
  margin: 0 30px;
  min-width: 1300px;
}

.advanced-search-bar, .search-bar {
  border: 1px solid #aab3b3;
  margin-top: 10px;
  background-color: #e6f7ff;
}
.advanced-search-bar .search-name, .search-bar .search-name {
  width: 180px;
}

.advanced-search-bar .el-form-item__label {
  width: 135px;
}
.advanced-search-bar .search-id {
  width: 140px;
}
.advanced-search-bar .search-check {
  margin-left: 15px;
}
.advanced-search-bar .search-check .el-form-item__label {
  width: 65px;
}

.search-bar {
  display: flex;
  padding: 5px;
}
.search-bar .search-id {
  width: 135px;
}
.search-bar .el-form-item--small.el-form-item {
  margin-bottom: 0;
}

.search-buttons {
  margin-left: auto;
  margin-right: 8px;
}
.search-buttons .el-button--small {
  margin: 0;
}

.urm-list .table-striped {
  margin-top: 10px;
}
.urm-list .operations .el-button--small {
  margin: 0;
}

.el-menu-item, .el-table--small,
.el-form-item__label, .el-input--small, .el-button--small {
  font-size: 16px;
}

.el-table table.el-table__body td.edit-cell {
  padding: 0;
}

.el-table table.el-table__body td.edit-cell .cell {
  padding-right: 1px;
  padding-left: 1px;
}
</style>
