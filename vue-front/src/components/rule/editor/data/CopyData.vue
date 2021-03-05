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
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>

    <el-table :data="items" @row-dblclick="handleRowDblclick" height="300" border stripe>
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
      <el-table-column width="45" class-name="edit-cell operations">
        <template slot-scope="scope">
          <el-button icon="el-icon-view" @click.stop="viewFields(scope.row.id)"/>
        </template>
      </el-table-column>
    </el-table>

    <hr/>
    <el-table :data="fields" height="300" border stripe>
      <el-table-column :label="$t('label.index')" prop="sno" width="70" align="center"/>
      <el-table-column :label="$t('label.fieldName')" prop="engName" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.fieldLocalName')" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.fieldType')" width="90">
        <template slot-scope="scope">
          <span>{{getTypeStr(scope.row.type)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.dFormat')" prop="dateFormat" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.len')" prop="length" width="70"/>
      <el-table-column :label="$t('label.nullable')" width="75">
        <template slot-scope="scope">
          <span>{{getYNStr(scope.row.nullable)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.isKey')" width="85">
        <template slot-scope="scope">
          <span>{{getYNStr(scope.row.keyYN)}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.isSQL')" width="110">
        <template slot-scope="scope">
          <span>{{getYNStr(scope.row.sqlYN)}}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  data () {
    return {
      sparam: {
        type: '',
      },
      items: [],
      fields: [],
    }
  },
  methods: {
    search() {
      const loading = this.$startLoading()
      this.$http.get('/api/data', {
        params: this.sparam,
      }).then(response => {
        this.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // search

    viewFields (id) {
      const loading = this.$startLoading()
      this.$http.get('/api/data/field/' + id, {
      }).then(response => {
        this.fields = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleRowClick

    handleRowDblclick (row) {
      const loading = this.$startLoading()
      this.$http.get('/api/data/field/' + row.id, {
      }).then(response => {
        this.$emit('confirm', response.data)
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleRowDblclick

    getTypeStr (val) {
      switch (val) {
        case 'C': return 'Character'
        case 'N': return 'Number'
        case 'D': return 'Date'
        case 'B': return 'Binary'
      }
    }, // getTypeStr

    getYNStr (val) {
      return val ? 'Yes' : 'No'
    }, // getYNStr
  },
  mounted () {
    this.search()
  },
  computed: {
    dataTypes: function () {
      let kind = RuleUtil.CODEKEY.dataType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>