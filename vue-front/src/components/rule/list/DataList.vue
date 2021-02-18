<template>
  <div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="ID">
          <el-input v-model="sparam.id" class="search-id"/>
        </el-form-item>
        <el-form-item label="Name" class="search-name">
          <el-input v-model="sparam.name"/>
        </el-form-item>
        <el-form-item label="Type" class="search-id">
          <el-select v-model="sparam.type">
            <el-option value="" label="All"/>
            <!--dataType options-->
          </el-select>
        </el-form-item>
      </el-form>
      <div>
        <el-button>Search</el-button>
        <el-button v-if="!onlySearch">Add</el-button>
        <el-button v-if="!onlySearch">Delete</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import RuleList from './RuleList'

export default {
  mixin: [RuleList],
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