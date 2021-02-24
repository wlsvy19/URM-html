<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px" :inline="true">
      <div class="row">
        <el-form-item :label="$t('label.requestId')" prop="id">
          <el-input v-model="item.id" class="size-id" readonly/>
        </el-form-item>
        <el-form-item :label="$t('label.requestName')" prop="name">
          <el-input v-model="item.id" class="size-name"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.interfaceType')">
          <el-select v-model="item.type" class="search-id" prop="interfaceType">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.processStatus')">
          <el-select v-model="item.type" class="search-id" prop="processStat">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.changeStatus')">
          <el-select v-model="item.type" class="search-id" prop="chgStat">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.syncType')">
          <el-select v-model="item.type" class="search-id" prop="syncType">
            <el-option value="" label="ALL"/>
            <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.interfaceId')" prop="interfaceId">
          <el-input v-model="item.id" class="size-id"/>
        </el-form-item>
        <el-form-item :label="$t('label.eaiYn')" prop="eaiYN">
          <el-checkbox />
        </el-form-item>
        <el-form-item :label="$t('label.2pcYn')" prop="tpcYN">
          <el-checkbox />
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.jobType')" prop="jobType">
          <el-input v-model="item.id" class="size-id" />
        </el-form-item>
        <el-form-item :label="$t('label.trType')" prop="trType">
          <el-radio-group v-model="radio">
            <el-radio :label="3">단방향</el-radio>
            <el-radio :label="6">양방향</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('label.testDate')" prop="testDate">
            <el-date-picker v-model="chgDate" type="daterange" align="right" start-placeholder="Start Date" end-placeholder="End Date" default-value="2010-10-01" class="search-id"/>
        </el-form-item>
      </div>

      <div class="row">
        <el-form-item :label="$t('label.isMapping')" prop="dataMapYN">
          <el-checkbox />
        </el-form-item>
      </div>
      <hr/>
      <div class="row">
        <div class="col-1">
          <div class="row">
            <el-form-item :label="$t('label.sourceMethod')">
              <el-select v-model="item.type" class="size-id">
                <el-option value="" label="ALL"/>
                <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('label.sourceSystem')">
              <el-input v-model="item.sendSystemName" class="size-id"/>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="$t('label.sourceService')" prop="jobType">
              <el-input v-model="item.id" class="size-id" />
            </el-form-item>
            <el-form-item :label="$t('label.sourceInfo')" prop="sendDbInfo">
              <el-input class="size-id" />
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="$t('label.sndJobCode')">
              <el-input v-model="item.sendSystemName" class="size-id" prop="sendJobCodeName"/>
            </el-form-item>
            <el-form-item :label="$t('label.sourceAdmin')">
              <el-input v-model="item.sendSystemName" class="size-id" prop="sendAdminName"/>
            </el-form-item>
          </div>
        </div>

        <div class="col-1">
          <div class="row">
            <el-form-item :label="$t('label.targetMethod')">
              <el-select v-model="item.type" class="size-id">
                <el-option value="" label="ALL"/>
                <el-option v-for="type in sysTypes" :value="type.code" :label="type.name" :key="type.code"/>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('label.targetSystem')">
              <el-input v-model="item.rcvSystemName" class="size-id"/>
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="$t('label.targetService')" prop="jobType">
              <el-input v-model="item.id" class="size-id" />
            </el-form-item>
            <el-form-item :label="$t('label.targetInfo')" prop="sendDbInfo">
              <el-input class="size-id" />
            </el-form-item>
          </div>
          <div class="row">
            <el-form-item :label="$t('label.rcvJobCode')">
              <el-input v-model="item.sendSystemName" class="size-id" prop="sendJobCodeName"/>
            </el-form-item>
            <el-form-item :label="$t('label.targetAdmin')">
              <el-input v-model="item.sendSystemName" class="size-id" prop="sendAdminName"/>
            </el-form-item>
          </div>
        </div>
      </div>

      <hr/>
      <div class="row">
        <el-form-item :label="$t('label.etcRequest')">
          <el-input type="textarea"/>
        </el-form-item>
        <el-form-item :label="$t('label.eaiRequest')">
          <el-input type="textarea"/>
        </el-form-item>
      </div>
      <hr/>
      <div class="row">
        <el-form-item :label="$t('label.attachmentFile')">
          <el-input/>
          <el-upload/>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import RuleEditor from './RuleEditor'

export default {
    mixins: [RuleEditor],
    data() {
      return {
        radio: 3
      }
    }
}
</script>