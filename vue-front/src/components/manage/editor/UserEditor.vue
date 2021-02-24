<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px" :inline="true">
      <div class="row">
        <el-form-item :label="$t('label.id')">
          <el-input v-model="item.id" class="size-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.name')">
          <el-input v-model="item.name" class="size-id"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.pass')" prop="id">
          <el-input v-model="item.password" class="size-id"/>
          <el-input v-model="item.password" class="size-id"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.dept')">
          <el-input v-model="item.deptName" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.position')">
          <el-input v-model="item.positionName" class="size-id"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.grade')">
          <el-input v-model="item.gradeName" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.generalTel')">
          <el-input v-model="item.generalTelNo" class="size-id"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.officeTel')">
          <el-input v-model="item.officeTelNo" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.mobile')">
          <el-input v-model="item.celNo" class="size-id"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.auth')">

        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import RuleEditor from '../../rule/editor/RuleEditor'
export default {
  mixins: [RuleEditor],
}
</script>