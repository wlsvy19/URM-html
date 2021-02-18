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
.urm-header {
  margin-top: 10px;
  padding-left: 10px;
  font-weight: bold;
}

.advanced-search-bar, .search-bar {
  border: 1px solid #aab3b3;
  margin-top: 10px;
  background-color: #e6f7ff;
}
.advanced-search-bar .search-name, .search-bar .search-name {
  width: 180px;
}

.search-bar {
  display: flex;
}
.search-bar .search-id {
  width: 135px;
}

.search-buttons {
  margin-left: auto;
  margin-right: 8px;
}

.urm-list .table-striped {
  margin-top: 10px;
}
</style>
