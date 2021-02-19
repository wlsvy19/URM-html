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
          <el-select v-model="sparam.type">
            <el-option value="" label="All" class="search-id"/>
            <!--systemType options-->
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.devType')" class="search-buttons">
          <el-select v-model="sparam.type">
            <el-option value="" label="All"/>
            <!--devType options-->
          </el-select>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
        <el-button @click="clickEdit()" v-if="!onlySearch">{{$t('label.add')}}</el-button>
        <el-button @click="clickDelete('selected')" v-if="!onlySearch">{{$t('label.delete')}}</el-button>
      </div>
    </div>
    
    <el-table :data="items" border class="table-striped">
      <el-table-column :label="$t('label.systemId')" prop="id" width="130"/>
      <el-table-column :label="$t('label.systemName')" prop="name"/>
      <el-table-column :label="$t('label.systemType')" prop="type" width="110"/>
      <el-table-column :label="$t('label.devType')" prop="devType" width="90"/>
      <el-table-column :label="$t('label.host')" prop="hostId" width="165"/>
      <el-table-column :label="$t('label.ip')" prop="ip" width="165"/>
      <el-table-column :label="$t('label.port')" prop="port" width="85"/>
      <el-table-column :label="$t('label.dbType')" prop="dbType" width="95"/>
      <el-table-column :label="$t('label.dbName')" prop="dbName" width="165"/>
      <el-table-column width="150" class-name="edit-cell operations">
        <template slot-scope="scope">
          <div>
            <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row.id)"/>
            <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row.id)"/>
            <el-button icon="el-icon-connection" @click.stop="clickUsed(scope.row.id)"/>
          </div>
        </template>
      </el-table-column>
    </el-table> 
  </div>
</template>

<script>
import RuleList from './RuleList'

export default {
  mixins: [RuleList],
  data () {
    return {
      path: 'system',
      sparam: {
        ...this.sparam,
        type: '',
      },
    }
  },
  methods: {
    clickUsed (id) {
      console.log('used', id)
    },
  },
}
</script>