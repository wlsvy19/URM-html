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

div.row {
  display: flex;
}

.pointer {
  cursor: pointer;
}

.urm-panel {
  margin: 0 30px;
  min-width: 1300px;
}

.search-bar {
  border: 1px solid #aab3b3;
  margin-top: 10px;
  background-color: #e6f7ff;
  display: flex;
  padding: 5px;
}
.search-bar .search-id {
  width: 135px;
}
.search-bar .search-name {
  width: 180px;
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

.urm-editor > .editor-buttons {
  text-align: right;
  margin-top: 10px;
  margin-right: 20px;
}
.urm-editor .size-id {
  width: 175px;
}
.urm-editor .size-name {
  width: 220px;
}


.el-menu-item, .el-submenu__title {
  font-size: 16px;
}
.el-table--small, .el-form-item__label,
.el-input--small, .el-button--small, .el-menu-item-group__title {
  font-size: 15px;
}

.el-table table.el-table__body td.edit-cell {
  padding: 0;
}
.el-table table.el-table__body td.edit-cell .cell {
  padding-right: 1px;
  padding-left: 1px;
}

.el-pagination {
  user-select: none;
  text-align: center;
}
div.el-pagination {
  margin-top: 15px;
}

button.el-button {
  padding: 8px 10px;
}

.el-input .el-input__inner {
  padding: 0 8px;
}
.el-textarea .el-textarea__inner {
  padding: 5px 8px;
}

.el-input.is-disabled .el-input__inner {
    color: #737373;
    cursor: text !important;
}

.el-dialog__body {
  padding-top: 0;
}
</style>
