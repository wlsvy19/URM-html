<template>
  <div class="urm-list">
    <DataList :items="items" :onlySearch="true" @search="handleSearch" @row-dblclick="handleRowDblclick"/>

    <hr/>
    <el-table :data="fields">
      <el-table-column :label="$t('label.index')" prop="sno" width="65" align="center"/>
      <el-table-column :label="$t('label.fieldName')" prop="engName" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.fieldLocalName')" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.fieldType')" prop="type" width="85" :formatter="getTypeStr"/>
      <el-table-column :label="$t('label.dFormat')" prop="dateFormat" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.len')" prop="length" width="70"/>
      <el-table-column :label="$t('label.nullabel')" prop="nullable" width="75" :formatter="getYNStr"/>
    </el-table>
  </div>
</template>
<script>
export default {
  data () {
    return {
      items: [],
      fields: [],
    }
  },
  methods: {
    handleSearch(sparam) {
      const loading = this.$startLoading()
      this.$http.get('/api/data', {
        params: sparam,
      }).then(response => {
        this.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleSearch

    handleRowDblclick (row) {
      const loading = this.$startLoading()
      this.$http.get('/api/data/field/' + row.id, {
        params: sparam,
      }).then(response => {
        this.fields = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // handleDblclick

    getTypeStr (row, column, cellValue, index) {
      console.log(row, column, cellValue, index)
      switch (cellValue) {
        case 'C': return 'Character'
        case 'N': return 'Number'
        case 'D': return 'Date'
        case 'B': return 'Binary'
      }
    }, // getTypeStr

    getYNStr (row, column, cellValue, index) {
      console.log(row, column, cellValue, index)
      return cellValue ? 'Yes' : 'No'
    }, // getTypeStr
  },
}
</script>