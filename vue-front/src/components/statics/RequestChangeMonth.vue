<template>
  <div class="urm-pannel">
    <ChangeList ref="list" @edit="handleEdit"/>
  </div>
</template>

<script>
import ChangeList from './list/RequestChangeList'
export default {
  components: {
    ChangeList,
  }
}
</script>