<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="140px" style="margin-top: 15px;">
      <div class="row">
        <el-form-item :label="$t('label.systemId')">
          <el-input v-model="item.id" class="size-id" disabled/>
        </el-form-item>
        <el-form-item :label="$t('label.systemCd')">
          <el-input v-model="item.code" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.systemName')">
          <el-input v-model="item.name" class="size-name"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.systemType')">
          <el-select v-model="item.type" class="size-id">
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.devType')">
          <el-select v-model="item.devType" class="size-id">
            <el-option v-for="type in devTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.host')">
          <el-input v-model="item.hostId" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.ip')">
          <el-input v-model="item.ip" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.port')">
          <el-input v-model="item.port" class="size-name"/>
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
      <div v-show="item.type === '2'">
        <div class="row">
          <el-form-item :label="$t('label.dbType')">
            <el-select v-model="item.dbType" class="size-id">
              <el-option v-for="type in dbTypes" :value="type.code" :label="type.name" :key="type.code"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.jdbcType')">
            <el-select v-model="item.jdbcType" class="size-id">
              <el-option value="1" label="1"/>
              <el-option value="2" label="2"/>
              <el-option value="3" label="3"/>
              <el-option value="4" label="4"/>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('label.dbName')">
            <el-input v-model="item.dbName" class="size-name"/>
          </el-form-item>
        </div>
        <div class="row">
          <el-form-item :label="$t('label.connectParam')">
            <el-input type="textarea" v-model="item.dbParams" class="size-default"/>
          </el-form-item>
        </div>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.etcRemark')">
          <el-input type="textarea" v-model="item.remark" class="size-default"/>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import RuleEditor from './RuleEditor'
import RuleUtil from '@/components/rule/RuleUtil'

export default {
  mixins: [RuleEditor],
  props: {
    sysTypes: {
      type: Array,
      default: () => [],
    },
    devTypes: {
      type: Array,
      default: () => [],
    },
  },
  methods: {
    customValidator () {
      if (this.item.type !== '2') {
        this.item.dbType = null
        this.item.jdbcType = null
        this.item.dbName = null
        this.item.dbParams = null
      }
      return true
    },
  },
  computed: {
    dbTypes: function () {
      let kind = RuleUtil.CODEKEY.dbType
      return this.$store.state.codes.filter(code => (code.kind === kind))
    },
  },
}
</script>

<style scoped>
.size-default {
  width: 490px;
}
</style>