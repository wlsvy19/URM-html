<template>
  <div class="urm-list" >
    <div class="search-bar">
        <div class="search-buttons">
          <el-button @click="clickSearch">다시읽기</el-button>
          <el-button @click="clickDelete" type="danger" plain>{{$t('label.delete')}}</el-button>
        </div>
    </div>
    <el-table ref="table" :data="items" border class="table-striped">
      <el-table-column type="selection" width="40"/>
      <el-table-column label="매핑 ID" prop="id" width="135px"/>
      <el-table-column label="매핑 명" prop="name" width="180px"/>
      <el-table-column label="송신데이터" prop="sourceDataId" width="130px"/>
      <el-table-column label="수신데이터" prop="targetDataId" width="130px"/>
      <el-table-column label="사용 요건 ID" prop="requestId" width="145px"/>
      <el-table-column label="사용 요건 명" prop="requestName"/>
      <el-table-column label="사용 인터페이스 ID" prop="interfaceId" width="165px"/>
    </el-table>
  </div>
</template>

<script>
export default {
  props: ['items'],
  data () {
    return {
      path: '/api/datamap',
      sparam: {
        id: '',
      },
    }
  },
	methods: {
    clickSearch () {
      console.log('click refresh')
      const loading = this.$startLoading()
      this.$http.get(this.path, {
      }).then(response => {
        this.items = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() =>{
        loading.close()
      })
    }, // clickSearch

    clickDelete () {
      let ids = this.$refs.table.selection.map((it) => it.id)
      console.log('select: ' , ids)
      if (ids.length <= 0) {
        this.$message({message: this.$t('message.1004'), type: 'warning'})
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
          url: '/api/datamap/delete',
          data: ids,
        }).then(response => {
          let res = response.data
          if (res.code === 0) {
            this.$message({message: 'delete ' + this.path.toUpperCase() + ' [ ' + res.obj + ' ]', type: 'success'})
            this.clickSearch()
          } else if (res.code === 1) {
            this.$message({message: '삭제 실패하였습니다. - '})
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
		
	}
}
</script>