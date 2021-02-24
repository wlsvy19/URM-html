<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.bizNm')">
          <el-input v-model="sparam.name" class="search-id" prop="part1Name"/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level1.code')">
          <el-input v-model="sparam.part1Id" class="search-name"/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level1.name')">
          <el-input v-model="sparam.part1Name" class="search-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level2.code')">
          <el-input v-model="sparam.part2Id" class="search-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level2.name')">
          <el-input v-model="sparam.part2Name" class="search-id" readonly/>
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
      <el-table-column :label="$t('label.bizId')" prop="id" width="145"/>
      <el-table-column :label="$t('label.bizNm')" prop="name"/>
      <el-table-column :label="$t('label.biz.level1.code')" prop="part1Id"/>
      <el-table-column :label="$t('label.biz.level1.name')" prop="part1Name"/>
      <el-table-column :label="$t('label.biz.level2.code')" prop="part2Id"/>
      <el-table-column :label="$t('label.biz.level2.name')" prop="part2Name"/>
      <el-table-column :label="$t('label.auth')" width="170">
        <template slot-scope="scope">
          <span>{{getAuthStr(scope.row.authId)}}</span>
        </template>
      </el-table-column>
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
export default {
  props: {
    onlySearch: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      path: 'biz',
      listHeight: 'calc(100vh - 165px)',
      sparam: {
        id: '',
        name: '',
      },
      items: [],
    }
  },
  methods: {
    search () {
      const loading = this.$startLoading()
      console.log('search : ' + this.path, this.sparam)
      this.$http.get('/api/' + this.path, {
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
        let url = '/api/' + this.path + '/delete'
        this.$http({
          method : 'POST',
          url: url,
          data: ids,
        }).then(response => {
          let res = response.data
          if (res.code === 0) {
            this.$message({message: 'delete ' + this.path.toUpperCase() + ' [ ' + res.obj + ' ]', type: 'success'})
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