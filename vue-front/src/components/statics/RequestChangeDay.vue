<template>
  <div class="urm-pannel">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="일자 선택">
          <el-date-picker v-model="procDate" type="daterange" value-format="yyyyMMdd" range-separator="~" style="width: 220px;"/>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button @click="search">{{$t('label.search')}}</el-button>
      </div>
    </div>
    <ChangeList ref="list" :items="listItem"/>
  </div>
</template>

<script>
import ChangeList from './list/RequestChangeList'

export default {
  computed: {
    procDate: {
      get: function () {
        return [this.sparam.startDate, this.sparam.endDate]
      },
      set: function (nVal) {
        this.sparam.startDate = nVal[0]
        this.sparam.endDate = nVal[1]
      }
    }
  },

  components: {
    ChangeList,
  },

  data () {
    return {
      path: '/api/stat/change/day',
      listItem: null,
      sparam: {
        type: '',
        startDate: '',
        endDate: '',
      },
    }
  },

  methods: {
    search() {
      const loading = this.$startLoading()
      this.$http.get(this.path, {
        params: this.sparams,
      }).then(response => {
        this.listItem = response.data
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(()=>{
        loading.close()
      })
    },
  }
}
</script>