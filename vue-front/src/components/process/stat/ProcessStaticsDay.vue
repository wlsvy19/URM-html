<template>
  <div class="urm-pannel">
    <h1>ProcessStaticsDay</h1>
    <StaticsDayList ref="list"  @edit="handleEdit"/>
  </div>
</template>

<script>
import StaticsDayList from './list/StaticsDayList'

export default {
  components: {
    StaticsDayList,
  },
}
</script>