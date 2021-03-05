<template>
  <el-table ref="map" :data="data" border stripe>
    <el-table-column type="selection" width="40" v-if="!isTemplate"/>
    <el-table-column :label="$t('label.index')" prop="sno" width="70" align="center"/>
    <el-table-column :label="$t('label.fieldName')" width="150" class-name="edit-cell">
        <template slot-scope="scope">
        <el-input v-model="scope.row.engName"/>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.fieldLocalName')" width="150" class-name="edit-cell">
        <template slot-scope="scope">
        <el-input v-model="scope.row.name"/>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.fieldType')" width="120" class-name="edit-cell">
        <template slot-scope="scope">
        <el-select v-model="scope.row.type">
            <el-option value="C" label="Character"/>
            <el-option value="N" label="Number"/>
            <el-option value="D" label="Date"/>
            <el-option value="B" label="Binary"/>
        </el-select>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.dFormat')" class-name="edit-cell">
        <template slot-scope="scope">
        <el-input v-model="scope.row.dateFormat"/>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.len')" width="70" class-name="edit-cell">
        <template slot-scope="scope">
        <el-input v-model="scope.row.length"/>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.nullable')" width="75" class-name="edit-cell">
        <template slot-scope="scope">
        <YesNoSelect :data="scope.row.nullable"/>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.isKey')" width="85" class-name="edit-cell">
        <template slot-scope="scope">
        <YesNoSelect :data="scope.row.keyYN"/>
        </template>
    </el-table-column>
    <el-table-column :label="$t('label.isSQL')" width="120" class-name="edit-cell">
        <template slot-scope="scope">
        <YesNoSelect :data="scope.row.sqlYN"/>
        </template>
    </el-table-column>
  </el-table>
</template>
<script>
import YesNoSelect from '../YesNoSelect'

export default {
  props: {
    data: {
      type: Array,
      default: function () {
        return []
      }
    },
    isTemplate: {
      type: Boolean,
      default: false,
    },
  },
  components: {
    YesNoSelect,
  },
  computed: {
    selection: function() {
      return this.$refs.map.selection
    },
  }
}
</script>