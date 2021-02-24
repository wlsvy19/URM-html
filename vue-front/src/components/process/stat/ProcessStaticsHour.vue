<template>
  <div class="urm-pannel">
    <h1>ProcessStaticsHour</h1>
    <StaticsHourList ref="list"  @edit="handleEdit"/>
  </div>
</template>

<script>
import StaticsHourList from './list/StaticsHourList'

export default {
  components: {
    StaticsHourList,
  },
}
</script>