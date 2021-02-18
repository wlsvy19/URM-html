<template>
  <div class="urm-list">
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item :label="$t('label.dataId')">
          <el-input v-model="sparam.id" class="search-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.dataName')">
          <el-input v-model="sparam.name" class="search-name"/>
        </el-form-item>
        <el-form-item :label="$t('label.dataType')">
          <el-select v-model="sparam.type" class="search-id">
            <el-option value="" label="All"/>
            <!--dataType options-->
          </el-select>
        </el-form-item>
      </el-form>
      <div class="search-buttons">
        <el-button>{{$t('label.search')}}</el-button>
        <el-button v-if="!false">{{$t('label.add')}}</el-button>
        <el-button v-if="!false">{{$t('label.delete')}}</el-button>
      </div>
    </div>

    <el-table :data="pageList.list" class="table-striped">
      <el-table-column :label="$t('label.id')" prop="id" />
      <el-table-column :label="$t('label.name')" prop="name" />
      <el-table-column :label="$t('label.dataType')" prop="type" />
      <el-table-column :label="$t('label.registId')" prop="regId" />
      <el-table-column :label="$t('label.registDate')" prop="regDate" />
      <el-table-column>
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
      sparam: {
        ...this.sparam,
        type: '',
      },
    }
  },
  methods: {
    clickUsed () {
      console.log('used')
    },
  },
}
</script>