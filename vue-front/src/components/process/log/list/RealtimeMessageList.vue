<template>
  <div class="urm-list">
    <el-table :data="data" border stripe>
      <el-table-column label="Index" prop="messageIndex" width="65"/>
      <el-table-column label="필드명" prop="fieldName"/>
      <el-table-column label="값" width="400" class="edit-cell">
        <template slot-scope="scope">
          <el-input v-model="scope.row.fieldValue" readonly/>
        </template>
      </el-table-column>
    </el-table>

    <hr/>
    <el-input type="textarea" v-model="message" readonly/>
  </div>
</template>
<script>
export default {
  props: {
    data: {
      type: Array,
      default: () => [],
    },
  },
  computed: {
    message: function () {
      let msg = ''
      if (this.data) {
        this.data.forEach((mf) => {
          msg += mf.fieldValue
        })
      }
      return msg
    },
  },
}
</script>