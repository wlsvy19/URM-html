<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px" :inline="true">
      <div class="row">
        <el-form-item :label="$t('label.systemId')" prop="id">
          <el-input v-model="item.id" class="size-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.systemCd')" prop="code">
          <el-input v-model="item.code" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.systemName')" prop="name">
          <el-input v-model="item.name" class="size-id"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.systemType')">
          <el-select v-model="item.type" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.devType')">
          <el-select v-model="item.devType" class="search-id">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.host')">
          <el-input v-model="item.hostId" class="size-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.ip')">
          <el-input v-model="item.ip" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.port')">
          <el-input v-model="item.port" class="size-id"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.id')">
          <el-input v-model="item.userId" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.pass')">
          <el-input v-model="item.userPasswd" class="size-id"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.etcRemark')">
          <el-input type="textarea"/>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import RuleEditor from './RuleEditor'

export default {
    mixins: [RuleEditor],
}
</script>

<style scoped>
div.data-header {
  display: flex;
}
div.data-header .el-button--small {
  margin: 0;
}
div.data-header > .data-row-buttons {
  margin-left: auto;
}
</style>