<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.systemId')">
          <el-input v-model="sparam.id" class="search-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.systemName')">
          <el-input v-model="sparam.name" class="search-name"/>
        </el-form-item>
        <el-form-item :label="$t('label.host')">
          <el-input v-model="sparam.name" class="search-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.systemType')">
          <el-select v-model="sparam.type" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.devType')">
          <el-select v-model="sparam.devType" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in devTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
        <el-button @click="clickEdit()" v-if="!onlySearch">{{$t('label.add')}}</el-button>
        <el-button @click="clickDelete('selected')" v-if="!onlySearch">{{$t('label.delete')}}</el-button>
      </div>
    </div>
    
    <el-table ref="table" :data="items" :height="listHeight" border class="table-striped">
      <el-table-column type="selection" width="40"/>
      <el-table-column :label="$t('label.systemId')" prop="id" width="130"/>
      <el-table-column :label="$t('label.systemName')" prop="name"/>
      <el-table-column :label="$t('label.systemType')" width="100">
        <template slot-scope="scope">
          <span>{{getTypeStr('sysType', scope.row.type)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.devType')" width="90">
        <template slot-scope="scope">
          <span>{{getTypeStr('devType', scope.row.devType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.host')" prop="hostId" width="165"/>
      <el-table-column :label="$t('label.ip')" prop="ip" width="165"/>
      <el-table-column :label="$t('label.port')" prop="port" width="85"/>
      <el-table-column :label="$t('label.dbType')" width="95">
        <template slot-scope="scope">
          <span>{{getTypeStr('dbType', scope.row.dbType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.dbName')" prop="dbName" width="155"/>
      <el-table-column width="100" class-name="edit-cell operations">
        <template slot-scope="scope">
          <div>
            <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row.id)"/>
            <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row.id)" plain/>
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
      path: 'system',
      listHeight: 'calc(100vh - 165px)',
      sparam: {
        ...this.sparam,
        type: '',
        devType: '',
      },
    }
  },
  methods: {
    clickUsed (id) {
      console.log('used', id)
    },
  },
  computed: {
    sysTypes: function () {
      let kind = RuleUtil.CODEKEY.sysType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
    devTypes: function () {
      let kind = RuleUtil.CODEKEY.devType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>