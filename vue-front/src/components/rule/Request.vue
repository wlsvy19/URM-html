<template>
  <div class="urm-panel">
    <RequestList ref="list" @edit="handleEdit"/>
  </div>
</template> 

<script>
import RuleMain from './RuleMain'

export default {
  mixins: [RuleMain],
  data () {
    return {
      path: 'request',
    }
  },
}
</script>