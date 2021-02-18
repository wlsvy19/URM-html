<template>
  <div id="app">
    <System/>
  </div>
</template> 

<script>
// import Data from './components/rule/Data'
import System from './components/rule/System'

export default {
  name: 'App',
  components: {
    // Data,
    System,
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
