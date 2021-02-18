<template>
  <div id="app">
    <Data/>
  </div>
</template>

<script>
import Data from './components/rule/Data'

export default {
  name: 'App',
  components: {
    Data,
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  color: #2c3e50;
}

.search-bar {
  display: flex;
}
</style>
