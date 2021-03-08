<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.bizNm')">
          <el-input v-model="sparam.name" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level1.code')">
          <el-input v-model="sparam.part1Id" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level1.name')">
          <el-input v-model="sparam.part1Name" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level2.code')">
          <el-input v-model="sparam.part2Id" class="search-id" @change="search"/>
        </el-form-item>
        <el-form-item :label="$t('label.biz.level2.name')">
          <el-input v-model="sparam.part2Name" class="search-id" @change="search"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
        <el-button @click="clickEdit()" v-if="!onlySearch">{{$t('label.add')}}</el-button>
        <el-button @click="clickDelete('selected')" v-if="!onlySearch">{{$t('label.delete')}}</el-button>
      </div>
    </div>

    <el-table ref="table" :data="items" @row-dblclick="handleRowDblclick" :height="listHeight"
        border stripe :row-key="$randomRowKey" :cell-class-name="cellClass">
      <el-table-column type="selection" width="40" v-if="!onlySearch"/>
      <el-table-column :label="$t('label.bizId')" prop="id" width="130"/>
      <el-table-column :label="$t('label.bizNm')" width="200">
        <template slot-scope="scope">
          <span v-if="!scope.row.editable">{{scope.row.name}}</span>
          <el-input v-model="scope.row.name" v-if="scope.row.editable"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.biz.level1.code')">
        <template slot-scope="scope">
          <span v-if="!scope.row.editable">{{scope.row.part1Id}}</span>
          <el-input v-model="scope.row.part1Id" v-if="scope.row.editable"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.biz.level1.name')">
        <template slot-scope="scope">
          <span v-if="!scope.row.editable">{{scope.row.part1Name}}</span>
          <el-input v-model="scope.row.part1Name" v-if="scope.row.editable"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.biz.level2.code')">
        <template slot-scope="scope">
          <span v-if="!scope.row.editable">{{scope.row.part2Id}}</span>
          <el-input v-model="scope.row.part2Id" v-if="scope.row.editable"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.biz.level2.name')">
        <template slot-scope="scope">
          <span v-if="!scope.row.editable">{{scope.row.part2Name}}</span>
          <el-input v-model="scope.row.part2Name" v-if="scope.row.editable"/>
        </template>
      </el-table-column>
      <el-table-column width="85" class-name="edit-cell operations">
        <template slot-scope="scope">
          <div v-if="!scope.row.editable">
            <el-tooltip :content="$t('label.modify')" placement="top" :open-delay="500" >
              <el-button icon="el-icon-edit" @click.stop="clickEdit(scope.row)"/>
            </el-tooltip>
            <el-tooltip :content="$t('label.delete')" placement="top" :open-delay="500" :enterable="false">
              <el-button icon="el-icon-delete" type="danger" @click.stop="clickDelete(scope.row)" plain/>
            </el-tooltip>
          </div>
          <div v-if="scope.row.editable">
            <el-tooltip :content="$t('label.save')" placement="top" :open-delay="500" :enterable="false">
              <el-button icon="el-icon-receiving" @click.stop="clickSave(scope.row)"/>
            </el-tooltip>
            <el-tooltip :content="$t('label.cancel')" placement="top" :open-delay="500" :enterable="false">
              <el-button icon="el-icon-close" @click.stop="clickCancel(scope.row)" plain/>
            </el-tooltip>
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
      default: () => [],
    },
  },
  data () {
    return {
      listHeight: 'calc(100vh - 175px)',
      sparam: {
        name: '',
        part1Id: '',
        part1Name: '',
        part2Id: '',
        part2Name: '',
      },
    }
  },
  methods: {
    search () {
      this.$emit('search', this.sparam)
    }, // search

    clickEdit (row) {
      if (row) {
        console.log('edit', row)
        row.editable ? (row.editable = true) : this.$set(row, 'editable', true)
      } else {
        this.items.push({
          editable: true,
          name: '',
          part1Id: '',
          part1Name: '',
          part2Id: '',
          part2Name: '',
        })
      }
    }, // clickEdit

    clickDelete (key) {
      let ids = []
      if (key === 'selected') {
        let checkedList = this.$refs.table.selection
        if (checkedList.length <= 0) {
          this.$message({message: this.$t('message.1004'), type: 'warning'})
          return
        }
        // TODO handle added item
      } else if (key.id) {
        ids.push(key.id)
      } else {
        this.items.some((it, idx) => {
          if (it.__randomKey__ == key.__randomKey__) {
            this.items.splice(idx, 1)
            return true
          }
        })
        return
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
          url: '/api/code/business/delete',
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

    clickSave (row) {
      if (!row.part2Id || row.part2Id.trim().length === 0) {
        this.$message.warning('Level2 ID 를 입력하세요.')
        return false
      }
      if (!row.part2Name || row.part2Name.trim().length === 0) {
        this.$message.warning('Level2 명 을 입력하세요.')
        return false
      }

      console.log('save', row)
      const loading = this.$startLoading()
      this.$http({
        method : 'POST',
        url: '/api/code/business',
        data: row,
      }).then(() => {
        this.$message.success('저장 되었습니다.')
        this.search()
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // clickSave

    clickCancel (row) {
      row.editable = false
    }, // clickCancel

    handleRowDblclick (row) {
      this.$emit('row-dblclick', row)
    }, // handleRowDblclick

    cellClass (scope) {
      if (scope.row.editable && 1 < scope.columnIndex && scope.columnIndex < 7) {
        return 'edit-cell'
      }
    }, // cellClass
  },
  mounted () {
    if (!this.onlySearch) {
      this.search()
    }
  },
}
</script>