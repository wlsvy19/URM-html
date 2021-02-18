<template>
  <div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="시스템 ID">
          <el-input v-model="sparam.id" class="search-id"/>
        </el-form-item>
        <el-form-item label="시스템명">
          <el-input v-model="sparam.name" class="search-name"/>
        </el-form-item>
        <el-form-item label="Host명">
          <el-input v-model="sparam.name" class="search-id"/>
        </el-form-item>
        <el-form-item label="시스템타입">
          <el-select v-model="sparam.type">
            <el-option value="" label="All" class="search-id"/>
            <!--dataType options-->
          </el-select>
        </el-form-item>
        <el-form-item label="개발 구분" class="search-buttons">
          <el-select v-model="sparam.type">
            <el-option value="" label="All"/>
            <!--dataType options-->
          </el-select>
        </el-form-item>
      </el-form>
      <div>
        <el-button>시스템 찾기</el-button>
        <el-button v-if="!onlySearch">Add</el-button>
        <el-button v-if="!onlySearch">Delete</el-button>
      </div>
    </div>
    <div class="urm-list">
      <el-table border style="width: 100%">
        <el-table-column prop="id" label="시스템ID" width="130"></el-table-column>
        <el-table-column prop="name" label="시스템명"> </el-table-column>
        <el-table-column prop="type" label="시스템타입" width="100"></el-table-column>
        <el-table-column prop="devType" label="개발구분" width="90"></el-table-column>
        <el-table-column prop="hostId" label="Host명" width="165"></el-table-column>
        <el-table-column prop="ip" label="IP" width="165"></el-table-column>
        <el-table-column prop="port" label="Port" width="85"></el-table-column>
        <el-table-column prop="dbType" label="DB Type" width="95"></el-table-column>
        <el-table-column prop="dbName" label="DB(Service)Name" width="155"></el-table-column>
        <el-table-column prop="" width="85">
          <el-button type="primary" icon="el-icon-edit" circle></el-button>
          <el-button type="danger" icon="el-icon-delete" circle></el-button>
        </el-table-column>
      </el-table> 
    </div>
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
  }
}
</script>