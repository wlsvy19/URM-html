<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.id')">
          <el-input v-model="sparam.id" class="search-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.name')">
          <el-input v-model="sparam.name" class="search-name"/>
        </el-form-item>
        <el-form-item :label="$t('label.dept')">
          <el-input v-model="sparam.dept" class="search-id" readonly/>
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
      <el-table-column width="85" class-name="edit-cell operations">
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
  },
  data () {
    return {
      listHeight: 'calc(100vh - 165px)',
      sparam: {
        id: '',
        name: '',
        dept: '',
      },
      items: [],
    }
  },
  methods: {
    search () {
      const loading = this.$startLoading()
      let url = '/api/user'
      console.log('search : ' + url, this.sparam)
      this.$http.get(url, {
        params: this.sparam,
      }).then(response => {
        this.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // search

    clickEdit (id) {
      this.$emit('edit', id)
    }, // clickEdit

    clickDelete (key) {
      let ids = []
      if (key === 'selected') {
        let checkedList = this.$refs.table.selection
        if (checkedList.length <= 0) {
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
        let url = '/api/user/delete'
        this.$http({
          method : 'POST',
          url: url,
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
          return auth
        }
      })
      return obj.name
    }, // getAuthStr
  },
  mounted () {
    this.search()
  }
}
</script>