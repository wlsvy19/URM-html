<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" :disabled="!isSaveAuth">{{$t('label.save')}}</el-button>
    </div>
    <el-form label-width="135px">
      <div class="row">
        <el-form-item :label="$t('label.id')">
          <el-input v-model="item.id" class="user-input" @click.native="showIdChecker" disabled/>
        </el-form-item>
        <el-form-item :label="$t('label.name')">
          <el-input v-model="item.name" class="user-input"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.pass')">
          <el-input type="password" v-model="item.password" class="user-input"/>
          <el-input type="password" v-model="item.confirm" style="margin-left: 5px; width: 360px;"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.dept')">
          <el-input v-model="item.deptName" class="user-input"/>
        </el-form-item>
        <el-form-item :label="$t('label.position')">
          <el-input v-model="item.positionName" class="user-input"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.grade')">
          <el-input v-model="item.gradeName" class="user-input"/>
        </el-form-item>
        <el-form-item :label="$t('label.generalTel')">
          <el-input v-model="item.generalTelNo" class="user-input"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.officeTel')">
          <el-input v-model="item.officeTelNo" class="user-input"/>
        </el-form-item>
        <el-form-item :label="$t('label.mobile')">
          <el-input v-model="item.celNo" class="user-input"/>
        </el-form-item>
      </div>
      <div class="row">
        <el-form-item :label="$t('label.auth')">
          <el-select v-model="item.authId" class="user-input">
            <el-option v-for="auth in this.$store.state.auths" :value="auth.id" :label="auth.name" :key="auth.id"/>
          </el-select>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      default: () => {},
    }
  },
  data () {
    return {
      idCheckerShow: false,
      checkedId: '',
      usableId: false,
    }
  },
  methods: {
    clickSave () {
      if (!this.validator()) {
        return
      }
      this.$emit('save', this.item)
    }, // clickSave

    validator () {
      let item = this.item
      if (!item.id || item.id.length === 0) {
        this.$message({message: '[' +  this.$t('label.id') + '] ' + this.$t('message.1015'), type: 'warning'})
        return false
      }
      if (!item.name || item.name.trim().length === 0) {
        this.$message({message: '[' +  this.$t('label.name') + '] ' + this.$t('message.1015'), type: 'warning'})
        return false
      }
      if (item.password) {
        if (!item.password || item.password.trim().length === 0) {
          this.$message({message: '[' +  this.$t('label.pass') + '] ' + this.$t('message.1015'), type: 'warning'})
          return false
        }
        if (item.password !== item.confirm) {
          this.$message({message: this.$t('message.1016'), type: 'warning'})
          return false
        }
      }
      return true
    }, // validator

    showIdChecker () {
      if (this.item.id) {
        return
      }

      let inputEl = (
        <el-input style="width: 220px;" nativeOnInput={(ev) => this.handleCkInput(ev)}
          nativeOnKeydown={(ev) => this.handleCkKeydown(ev)}/>
      )
      let contentEl = (
        <div style="text-align: center;">
          {inputEl}
          <el-button on-click={this.checkId}>{this.$t('label.idConfirm')}</el-button>
        </div>
      )

      let confirmProp = {
        confirmButtonText: this.$t('label.idUse'),
        cancelButtonText: 'NO',
        closeOnClickModal: false,
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action !== 'confirm' || this.usableId) {
            done()
          }
        },
      }
      this.$confirm(contentEl, this.$t('label.modifyUserInfo'), confirmProp).then(() => {
        this.item.id = this.checkedId.trim()
      }).catch(() => {}).finally(() => {
        this.usableId = false
        this.checkedId = ''
        inputEl.elm.querySelector('input').value = ''
      })
    }, // showIdChecker

    handleCkInput (ev) {
      ev.data && (this.checkedId += ev.data)
      ev.target.value = this.checkedId
    }, // handleCkInput

    handleCkKeydown (ev) {
      if (ev.keyCode === 13) {
        this.checkId()
        return
      }
      let el = ev.target
      if (el.selectionStart !== el.selectionEnd) {
        this.checkedId = this.checkedId.substring(0, el.selectionStart)
      } else if (ev.keyCode === 8 || ev.keyCode === 46) {
        this.checkedId = this.checkedId.substring(0, el.selectionStart - 1)
      }
    }, // handleCkKeydown

    checkId () {
      // eslint-disable-next-line
      const idRegExp =  /^[0-9a-zA-Z_\-\.]*$/
      let id = this.checkedId.trim()
      if (id.length === 0) {
        return false
      } else if (!id.match(idRegExp)) {
        this.$message({message: '유효하지 않은 아이디입니다.', type: 'error'})
        return false
      }

      const loading = this.$startLoading()
      this.$http.get('/api/user/check', {
        params: {id: id},
      }).then(response => {
        if (response.data > 0) { // duplicate
          this.$message({message: this.$t('message.2002'), type: 'error'})
        } else {
          this.$message({message: this.$t('message.2001'), type: 'success'})
          this.usableId = true
        }
      }).catch(error => {
        this.$handleHttpError(error)
      }).finally(() => {
        loading.close()
      })
    }, // checkId
  },
  computed: {
    isSaveAuth: function () {
      return true
    },
  },
}
</script>
<style scoped>
.user-input {
  width: 230px;
}
</style>