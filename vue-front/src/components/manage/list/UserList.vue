<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.id')">
          <el-input v-model="sparam.id" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.name')">
          <el-input v-model="sparam.name" class="search-name" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.dept')">
          <el-input v-model="sparam.deptName" class="search-id" @change="search"/>
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
      <el-table-column :label="$t('label.id')" prop="id" width="145"/>
      <el-table-column :label="$t('label.name')" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('label.dept')" prop="deptName"/>
      <el-table-column :label="$t('label.position')" prop="positionName"/>
      <el-table-column :label="$t('label.grade')" prop="gradeName"/>
      <el-table-column :label="$t('label.generalTel')" prop="generalTelNo"/>
      <el-table-column :label="$t('label.officeTel')" prop="officeTelNo"/>
      <el-table-column :label="$t('label.auth')" width="170">
        <template slot-scope="scope">
          <span>{{getAuthStr(scope.row.authId)}}</span>
        </template>
      </el-table-column>
      <el-table-column width="85" class-name="edit-cell operations" v-if="!onlySearch">
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
export default {
  props: {
    onlySearch: {
      type: Boolean,
      default: false,
    },
    items: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data () {
    return {
      listHeight: 'calc(100vh - 165px)',
      path: 'user',
      sparam: {
        id: '',
        name: '',
        dept: '',
      },
    }
  },
  methods: {
    search () {
      this.$emit('search', this.sparam)
    }, // search

    clickEdit (id) {
      this.$emit('edit', id)
    }, // clickEdit

    clickDelete (key) {
      let ids = []
      if (key === 'selected') {
        ids = this.$refs.table.selection.map((row) => row.id)
        if (ids.length <= 0) {
          this.$message({message: this.$t('message.1004'), type: 'warning'})
          return
        }
      } else {
        ids.push(key)
      }
      let confirmProp = {
        confirmButtonText: 'YES',
        cancelButtonText: 'NO',
        closeOnClickModal: false,
        dangerouslyUseHTMLString: false,
        type: 'error',
      }
      this.$confirm('정말 삭제 하시겠습니까?', confirmProp).then(() => {
        const loading = this.$startLoading()
        this.$http({
          method : 'POST',
          url: '/api/' + this.path + '/delete',
          data: ids,
        }).then(response => {
          let res = response.data
          if (res.code === 0) {
            this.$message({message: 'delete user [ ' + res.obj + ' ]', type: 'success'})
            this.search()
          } else if (res.code === 1) {
            this.$message({message: '삭제 실패하였습니다. - ' + res.message + ' \n 영향도를 확인하여 주세요.', type: 'warning'})
          } else {
            this.$message({message: res.message, type: 'warning'})
          }
        }).catch(error => {
          this.$handleHttpError(error)
        }).finally(() => {
          loading.close()
        })
      }).catch(() => {})
    }, // clickDelete

    handleRowDblclick (row) {
      this.$emit('row-dblclick', row)
    }, // handleRowDblclick

    getAuthStr (key) {
      let auths = this.$store.state.auths
      let obj = {}
      auths.some((auth) => {
        if (auth.id === key) {
          obj = auth
          return true
        }
      })
      return obj.name
    }, // getAuthStr
  },
  mounted () {
    if (!this.items) {
      this.search()
    }
  }
}
</script>