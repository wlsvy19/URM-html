<template>
  <div class="urm-editor">
    <div class="editor-buttons">
      <el-button @click="clickSave" plain>{{$t('label.save')}}</el-button>
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
        if (!item.password !== item.confirm) {
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

      const $el = this.$createElement
      let $vue = this
      let $input = $el('el-input', {
        style: { width: '220px' },
        model: {
          callback: function (e) {
            $vue.checkedId = e
          },
        },
        nativeOn: {
          input: function (e) {
            e.target.value = $vue.checkedId
          },
        },
      }, null)

      let content = $el('div', {
        style: { textAlign: 'center' }
      }, [
        $input,
        $el('el-button', {
          on: { click: this.checkId }
        }, this.$t('label.idConfirm'))
      ])

      this.$confirm(content, this.$t('label.modifyUserInfo'), confirmProp).then(() => {
        this.item.id = this.checkedId.trim()
      }).catch(() => {}).finally(() => {
        this.usableId = false
        $input.elm.querySelector('input').value = ''
      })
    }, // showIdChecker

    checkId () {
      // eslint-disable-next-line
      const idRegExp =  /^[0-9a-zA-Z_\-\.]*$/
      let id = this.checkedId.trim()
      if (id.length === 0) {
        return false
      } else if (!id.match(idRegExp)) {
        this.$message({message: '유효하지 않은 아이디입니다.', type: 'error'})
      }

      const loading = this.$startLoading()
      this.$http.get('/api/user/check', {
        params: {id: id},
      }).then(response => {
        if (response.data > 0) { // duplicate
          this.$message({message: this.$t('message.2002'), type: 'warning'})
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
}
</script>
<style scoped>
.user-input {
  width: 230px;
}
</style>