<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.dataId')">
          <el-input v-model="sparam.id" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.dataName')">
          <el-input v-model="sparam.name" class="search-name" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.dataType')">
          <el-select v-model="sparam.type" class="search-id" @change="search">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in dataTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click.stop="search">{{$t('label.search')}}</el-button>
        <el-button @click.stop="clickEdit()" v-if="!onlySearch">{{$t('label.add')}}</el-button>
        <el-button @click.stop="clickDelete('selected')" type="danger" :disabled="!isDeleteAuth" v-if="!onlySearch" plain>{{$t('label.delete')}}</el-button>
      </div>
    </div>

    <el-table ref="table" :data="items" @row-dblclick="handleRowDblclick" :height="listHeight" border stripe>
      <el-table-column type="selection" width="40" v-if="!onlySearch"/>
      <el-table-column :label="$t('label.id')" prop="id" width="150"/>
      <el-table-column :label="$t('label.name')" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.dataType')" width="100">
        <template slot-scope="scope">
          <span>{{getTypeStr('dataType', scope.row.type)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.registId')" prop="regId" width="200" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.registDate')" width="200">
        <template slot-scope="scope">
          <span>{{getDateStr(scope.row.regDate, 'yyyy-MM-dd HH:mm:ss')}}</span>
        </template>
      </el-table-column>
      <el-table-column width="120" class-name="edit-cell operations" v-if="!onlySearch">
        <template slot-scope="scope">
          <el-tooltip :content="$t('label.modify')" placement="top" :open-delay="500" :enterable="false">
            <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row.id)"/>
          </el-tooltip>
          <el-tooltip :content="$t('label.delete')" placement="top" :open-delay="500" :enterable="false">
            <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row.id)" :disabled="!isDeleteAuth" plain/>
          </el-tooltip>
          <el-tooltip content="영향도" placement="top" :open-delay="500" :enterable="false">
            <el-button icon="el-icon-connection" @click.stop="clickUsed(scope.row.id)"/>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import RuleList from './RuleList'
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  mixins: [RuleList],
  data () {
    return {
      path: 'data',
      sparam: {
        ...this.sparam,
        type: '',
      },
    }
  },
  methods: {
    clickUsed (id) {
      this.$emit('show-used', id, false)
    }, // clickUsed
  },
  computed: {
    dataTypes: function () {
      let kind = RuleUtil.CODEKEY.dataType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>