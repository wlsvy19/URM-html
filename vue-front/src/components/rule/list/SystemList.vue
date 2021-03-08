<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.systemId')">
          <el-input v-model="sparam.id" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.systemName')">
          <el-input v-model="sparam.name" class="search-name" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.host')">
          <el-input v-model="sparam.hostId" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.systemType')">
          <el-select v-model="sparam.type" class="search-id" @change="search">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.devType')">
          <el-select v-model="sparam.devType" class="search-id" @change="search">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in devTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
        <el-button @click="clickEdit()" v-if="!onlySearch">{{$t('label.add')}}</el-button>
        <el-button @click="clickDelete('selected')" type="danger" v-if="!onlySearch" plain>{{$t('label.delete')}}</el-button>
      </div>
    </div>
    
    <el-table ref="table" :data="items" @row-dblclick="handleRowDblclick" :height="listHeight" border stripe>
      <el-table-column type="selection" width="40" v-if="!onlySearch"/>
      <el-table-column :label="$t('label.systemId')" prop="id" width="130"/>
      <el-table-column :label="$t('label.systemName')" prop="name" :show-overflow-tooltip="true"/>
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
      <el-table-column :label="$t('label.host')" prop="hostId" width="165" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.ip')" prop="ip" width="165" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.port')" prop="port" width="85"/>
      <el-table-column :label="$t('label.dbType')" width="95">
        <template slot-scope="scope">
          <span>{{getTypeStr('dbType', scope.row.dbType)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.dbName')" prop="dbName" width="155" :show-overflow-tooltip="true"/>
      <el-table-column width="85" class-name="edit-cell operations">
        <template slot-scope="scope">
          <el-tooltip :content="$t('label.modify')" placement="top" :open-delay="500" :enterable="false">
            <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row.id)"/>
          </el-tooltip>
          <el-tooltip :content="$t('label.delete')" placement="top" :open-delay="500" :enterable="false">
            <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row.id)" plain/>
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
  props: {
    sysTypes: {
      type: Array,
      default: function () {
        console.log('set default sysTypes')
        let kind = RuleUtil.CODEKEY.sysType
        return this.$store.state.codes.filter(code => (code.kind === kind))
      }
    },
    devTypes: {
      type: Array,
      default: function () {
        console.log('set default devTypes')
        let kind = RuleUtil.CODEKEY.devType
        return this.$store.state.codes.filter(code => (code.kind === kind))
      }
    },
  },
  data () {
    return {
      path: 'system',
      sparam: {
        ...this.sparam,
        type: '',
        devType: '',
      },
    }
  },
}
</script>