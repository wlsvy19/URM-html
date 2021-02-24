<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.dataId')">
          <el-input v-model="sparam.id" class="search-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.dataName')">
          <el-input v-model="sparam.name" class="search-name"/>
        </el-form-item>
        <el-form-item :label="$t('label.dataType')">
          <el-select v-model="sparam.type" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in dataTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
        <el-button @click="clickEdit()" v-if="!onlySearch">{{$t('label.add')}}</el-button>
        <el-button @click="clickDelete('selected')" type="danger" v-if="!onlySearch" plain>{{$t('label.delete')}}</el-button>
      </div>
    </div>

    <el-table ref="table" :data="items" @row-dblclick="handleRowDblclick" :height="listHeight" border class="table-striped">
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
          <span>{{scope.row.regDate}}</span>
        </template>
      </el-table-column>
      <el-table-column width="115" class-name="edit-cell operations">
        <template slot-scope="scope">
          <div>
            <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row.id)"/>
            <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row.id)" plain/>
            <el-button icon="el-icon-connection" @click.stop="clickUsed(scope.row.id)"/>
          </div>
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
      listHeight: 'calc(100vh - 165px)',
      sparam: {
        ...this.sparam,
        type: '',
      },
    }
  },
  methods: {
    clickUsed (id) {
      console.log('used', id)
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