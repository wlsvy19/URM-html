import React, { useEffect } from 'react'
import { Button, Modal, Popover, DatePicker, message } from 'antd'
import { Form, Input, Select, Radio, Switch, Checkbox, Upload } from 'antd'
import moment from 'moment'

import RuleEditor from './RuleEditor'
import SubModal from '@/components/SubModal'

import DataMapEditor from './DataMap-editor'
import DataMapList from '@/components/rule/list//DataMap-list'
import SystemList from '@/components/rule/list/System-list'
import BizList from '@/components/manage/list/BizCode-list'
import UserList from '@/components/manage/list/User-list'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class RequestEditor extends RuleEditor {
  customMethod = {
    validator: (data) => {
      if (!data.sendSystemId || data.sendSystemId.trim().length === 0) {
        message.warning(locale['message.1008'])
        return false
      }
      if (!data.rcvSystemId || data.rcvSystemId.trim().length === 0) {
        message.warning(locale['message.1009'])
        return false
      }
      if (!data.sendJobCodeId || data.sendJobCodeId.trim().length === 0) {
        message.warning(locale['message.1019'])
        return false
      }
      if (!data.rcvJobCodeId || data.rcvJobCodeId.trim().length === 0) {
        message.warning(locale['message.1019'])
        return false
      }
      if (!data.sendAdminId || data.sendAdminId.trim().length === 0) {
        data.sendAdminId = this.props.userInfo.id
      }
      if (!data.rcvAdminId  || data.rcvAdminId.trim().length === 0) {
        data.rcvAdminId = this.props.userInfo.id
      }
      
      if( !urmsc.isSelectAuth('prcStat', data.processStat, this.props.userInfo.auth) ) {
        message.warning(locale['message.1010']);
        return false
      }
      if( !urmsc.isSelectAuth('chgStat', data.chgStat, this.props.userInfo.auth) ) {
        message.warning('\'변경 신청\' 으로 수정합니다.');
        this.setState({ item: {...this.state.item, chgStat: 0} })
        return false
      }
      return true
    }
  }

  childMethod = {
    ...this.childMethod,

    isPageDelete: () => {
      let auth = this.props.userInfo ? this.props.userInfo.auth : ''
      return urmsc.isPageEdit(this.props.path, 'delete', auth)
    },

    canSave: () => {
      let loginUser = this.props.userInfo
      let item = this.state.item
      let canSave =  this.childMethod.isPageSave()
      if (item.id && item.id.length > 0) {
        canSave = this.childMethod.isPageDelete() || item.regId === loginUser.id ||
          item.sendAdminId === loginUser.id || item.rcvAdminId === loginUser.id
      } 
      return canSave
    },
  }

  render() {
    return (
      <Modal visible={this.state.visible} width="1355px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedRequestEditor codeList={this.props.codeList} authList={this.props.authList}
          item={this.state.item} userInfo={this.props.userInfo} {...this.childMethod} />
      </Modal>
    );
  }
}

const RequestEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form
  const { RangePicker } = DatePicker

  useEffect(() => {
    props.setItem(form, item, method.initialSetter)
    item.subRule = {
      sqlPlain: item.sqlPlain,
      dbCrudType: item.dbCrudType,
      fileCrudType: item.fileCrudType,
    }
    // eslint-disable-next-line
  }, [item]); // Only re-run the effect if props.item changes

  let sysList = null;
  let bizList = null;
  let usrList = null;
  let mapList = null;
  let dataMap = null;

  let method = {
    clickSave: e => {
      let saveItem = method.makeRequestObj()
      props.save(saveItem)
    },

    initialSetter: (formItem, propsItem) => {
      formItem.testDate = [moment(propsItem.testStartYMD), moment(propsItem.testEndYMD)]
      formItem.sendSystemName = propsItem.sendSystem.name
      formItem.rcvSystemName = propsItem.rcvSystem.name
      formItem.sendJobCodeName = propsItem.sendJobCode.part2Name + '(' + propsItem.sendJobCode.part2Id + ')'
      formItem.rcvJobCodeName = propsItem.rcvJobCode.part2Name + '(' + propsItem.rcvJobCode.part2Id + ')'
      formItem.sendAdminName = propsItem.sendAdmin.name + '(' +
      (propsItem.sendAdmin.positionName ? propsItem.sendAdmin.positionName : '--' ) + ')'
      formItem.rcvAdminName = propsItem.rcvAdmin.name + '(' +
      (propsItem.rcvAdmin.positionName ? propsItem.rcvAdmin.positionName : '--' ) + ')'
      formItem.infFileName = propsItem.infFileName ? [{uid: '-1', name: propsItem.infFileName}] : undefined
    },

    makeRequestObj: () => {
      let saveItem = props.makeRuleObj(form, item)
      
      let testDate = form.getFieldValue('testDate')
      saveItem.testStartYMD = testDate[0].format('YYYYMMDD')
      saveItem.testEndYMD = testDate[1].format('YYYYMMDD')
      
      let children = item.subRule
      if (children.sendSystem) saveItem.sendSystemId = children.sendSystem.id
      if (children.rcvSystem) saveItem.rcvSystemId = children.rcvSystem.id
      if (children.sendJobCode) saveItem.sendJobCodeId = children.sendJobCode.id
      if (children.rcvJobCode) saveItem.rcvJobCodeId = children.rcvJobCode.id
      if (children.sendAdmin) saveItem.sendAdminId = children.sendAdmin.id
      if (children.rcvAdmin) saveItem.rcvAdminId = children.rcvAdmin.id
      
      saveItem.sqlPlain = undefined
      saveItem.sql = undefined
      saveItem.dbCrudType = undefined
      saveItem.fileCrudType = undefined
      if (saveItem.sendSystemType === '2') {
        saveItem.sqlPlain = children.sqlPlain
        saveItem.sql = saveItem.sqlPlain
      }
      if (saveItem.rcvSystemType === '2') saveItem.dbCrudType = children.dbCrudType
      if (saveItem.rcvSystemType === '1') saveItem.fileCrudType = children.fileCrudType
      
      let file = form.getFieldValue('infFileName')
      saveItem.infFileName = file ? file[0].name : undefined
      
      return saveItem
    },

    getDataMapList: (type) => {
      let ref = mapList
      let callback = (map) => {
        if (type === 'req') {
          form.setFieldsValue({reqDataMappingId: map.id})
        } else if (type === 'res') {
          form.setFieldsValue({resDataMappingId: map.id})
        }
        ref.setState({visible: false})
      }
      
      let childState = {
        list: { onDbClick: callback }
      }
      ref.setState({visible: true, childState: childState})
    },

    editDataMap: (type, e) => {
      let ref = dataMap
      let id = e.target.value
      let callback = (id) => {
        if (type === 'req') {
          form.setFieldsValue({reqDataMappingId: id})
        } else if (type === 'res') {
          form.setFieldsValue({resDataMappingId: id})
        }
      }
      if (id && id.trim().length) {
        urmsc.ajax({
          type: 'GET',
          url: 'api/datamap/' + id,
          success: function(res) {
            ref.setState({visible: true, item: res, confirm: callback})
          }
        })
      } else {
        ref.setState({visible: true, item: {}, confirm: callback})
      }
    },

    setSystem: (type) => {
      let ref = sysList
      let children = item.subRule
      let callback = (system) => {
        if (type === 'rcv') {
          children.rcvSystem = system
          form.setFieldsValue({rcvSystemName: system.name})
        } else if (type === 'send') {
          children.sendSystem = system
          form.setFieldsValue({sendSystemName: system.name})
        }
        ref.setState({visible: false})
      }
      
      let sysType = '3'
      if (type === 'rcv') {
        sysType = form.getFieldValue('rcvSystemType')
      } else if (type === 'send') {
        sysType = form.getFieldValue('sendSystemType')
      }
      
      let childState = {
        list: { onDbClick: callback, scparam: {type: sysType} }
      }
      ref.setState({visible: true, childState: childState})
    },

    setBizCode: (type) => {
      let ref = bizList
      let children = item.subRule
      let callback = (biz) => {
        let name = biz.part2Name + '(' + biz.part2Id + ')'
        if (type === 'rcv') {
          children.rcvJobCode = biz
          form.setFieldsValue({rcvJobCodeName: name})
        } else if (type === 'send') {
          children.sendJobCode = biz
          form.setFieldsValue({sendJobCodeName: name})
        }
        ref.setState({visible: false})
      }
      
      let childState = {
        list: { onDbClick: callback }
      }
      ref.setState({visible: true, childState: childState})
    },

    setUser: (type) => {
      let ref = usrList
      let children = item.subRule
      let callback = (user) => {
        let name = user.name + '(' + (user.positionName ? user.positionName : '--') + ')'
        if (type === 'rcv') {
          children.rcvAdmin = user
          form.setFieldsValue({rcvAdminName: name})
        } else if (type === 'send') {
          children.sendAdmin = user
          form.setFieldsValue({sendAdminName: name})
        }
        ref.setState({visible: false})
      }
      
      let childState = {
        list: { onDbClick: callback }
      }
      ref.setState({visible: true, childState: childState})
    },

    setQuery: visible => {
      if (!visible) {
        form.setFieldsValue({sqlPlain: form.getFieldValue('sql')})
      } else {
        form.setFieldsValue({sql: item.subRule.sqlPlain})
      }
    },

    preventAction: (file) => {
      let formData = new FormData();
      formData.append('file', file, file.name)
      
      urmsc.ajax({
        type: 'POST',
        url: 'api/request/upload',
        data: formData,
        dataType: 'text',
        contentType: false,
        success: function(res) {
          message.success(locale['message.0007'] + ' - ' + res)
          form.setFieldsValue({infFileName: [{uid: '-1', name: res}]})
        }
      })
      return false
    },
    
    uploadFile: e => {
      let list = e.file.status !== 'removed' ? [] : undefined
      return e && list
    },
    
    renderOpts: (key) => {
      return urmsc.getSubListByKey(props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },

    renderRadios: (key) => {
      return urmsc.getSubListByKey(props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Radio key={it.code} value={it.code}>{it.name}</Radio>)
    },

    renderSendSystem: () => {
      let type = form.getFieldValue('sendSystemType')
      let children = item.subRule
      if (children && type === '2') {
        let sqlProps = {initialValue: children.sqlPlain}
        if (item.type !== '2') {
          let query = form.getFieldValue('sqlPlain')
          children.sqlPlain = (query && query.length > 0) ? query : children.sqlPlain
        }
        
        let textarea = <Form.Item>{getFieldDecorator("sql")(<Input.TextArea style={{width: "390px", height: "200px"}} />)}</Form.Item>
        return (
          <Form.Item label={locale['label.sourceQuery']}>
            <Popover placement="topLeft" title={locale['label.sourceQuery']} trigger="click" content={textarea}
                onVisibleChange={method.setQuery}>
              {getFieldDecorator("sqlPlain", sqlProps)(<Input size="small" readOnly style={{width: "390px"}} />)}
            </Popover>
          </Form.Item>
        );
      }
      return undefined
    },

    renderRcvSystem: () => {
      let type = form.getFieldValue('rcvSystemType')
      let children = item.subRule
      if (children && type === '1') {
        let fileProps = {initialValue: !children.fileCrudType ? '1' : children.fileCrudType}
        if (item.rcvSystemType !== '1') {
          let crud = form.getFieldValue('fileCrudType')
          children.fileCrudType = !crud ? children.fileCrudType : crud
        }
        
        return (
          <Form.Item label={locale['label.targetFileCRUD']}>
            {getFieldDecorator("fileCrudType", fileProps)(<Select size="small" className="size-id">
              {method.renderOpts("fileCrudType")}
            </Select>)}
          </Form.Item>
        );
      } else if (children && type === '2') {
        let dbProps = {initialValue: !children.dbCrudType ? '1' : children.dbCrudType}
        if (item.rcvSystemType !== '2') {
          let crud = form.getFieldValue('dbCrudType')
          children.dbCrudType = !crud ? children.dbCrudType : crud
        }
        
        return (
          <Form.Item label={locale['label.targetDBCRUD']}>
            {getFieldDecorator("dbCrudType", dbProps)(<Select size="small" className="size-name">
              {method.renderOpts("dbCrudType")}
            </Select>)}
          </Form.Item>
        );
      }
      return undefined
    },

    renderDataMap: () => {
      let useDataMap = form.getFieldValue('dataMapYN')
      if (useDataMap) {
        let reqProps = {initialValue: props.item.reqDataMappingId}
        let resProps = {initialValue: props.item.resDataMappingId}
        return (
          <div>
            <Form.Item label="Request">
              {getFieldDecorator("reqDataMappingId", reqProps)(<Input.Search size="small" readOnly allowClear
                onSearch={vars => { method.getDataMapList('req') }} onClick={e => { method.editDataMap('req', e) }} className="size-id" />)}
            </Form.Item>
            <Form.Item label="Response">
              {getFieldDecorator("resDataMappingId", resProps)(<Input.Search size="small" readOnly allowClear
                onSearch={vars => { method.getDataMapList('res') }} onClick={e => { method.editDataMap('res', e) }} className="size-id" />)}
            </Form.Item>
          </div>
        );
      }
      return undefined
    },
    
    changeType: (val, opt) => {
      if (item.id) return false
      let obj = {}
      if (val === '1') {
        obj.sendSystemType = '3'
        obj.rcvSystemType = '3'
        obj.tpcYN = false
        obj.trType = '2'
        obj.dataMapYN = false
      } else if (val === '2') {
        obj.sendSystemType = '1'
        obj.rcvSystemType = '1'
        obj.tpcYN = true
        obj.dataMapYN = false
      } else if (val === '3') {
        obj.sendSystemType = '2'
        obj.rcvSystemType = '2'
        obj.tpcYN = true
        obj.dataMapYN = true
      }
      form.setFieldsValue(obj)
    },
    
    changeSyncType: (val, opt) => {
      if (item.id) return false
      if (val === '2') {
        form.setFieldsValue({trType: '1'})
      } else {
        form.setFieldsValue({trType: '2'})
      }
    },
    
    downloadInfFile: e => {
      let file = form.getFieldValue('infFileName')
      if (!file) return false
      let fileName = file[0].name
      if (!fileName || fileName.length === 0) return false
      
      window.open('api/request/download?fileName='+fileName, '_blank')
      return false
    },
  }
  
  let serviceTitle = (type) => {
    let sysType = '3'
    if (type === 'send') {
      sysType = form.getFieldValue('sendSystemType')
      switch (sysType) {
        case '1': return locale['label.sourceFile']
        case '2': return locale['label.sourceTable']
        default: return locale['label.sourceService']
      }
    } else if (type === 'rcv') {
      sysType = form.getFieldValue('rcvSystemType')
      switch (sysType) {
        case '1': return locale['label.targetFile']
        case '2': return locale['label.targetTable']
        default: return locale['label.targetService']
      }
    }
    return 'Service'
  }
  
  let isOnl = () => {
    return form.getFieldValue('interfaceType') === '1'
  }

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave} disabled={!props.canSave()}>{locale['label.save']}</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label={locale['label.requestId']}>{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label={locale['label.requestName']}>{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.interfaceType']}>
            {getFieldDecorator("interfaceType", {initialValue: "1"})(<Select size="small" className="size-id" onChange={method.changeType}>
              {method.renderOpts("infType")}
            </Select>)}
          </Form.Item>
          <Form.Item label={locale['label.processStatus']}>
            {getFieldDecorator("processStat", {initialValue: "1"})(<Select size="small" className="size-id">
              {method.renderOpts("procStat")}
            </Select>)}
          </Form.Item>
          <Form.Item label={locale['label.changeStatus']}>
            {getFieldDecorator("chgStat", {initialValue: "1"})(<Select size="small" className="size-id">
              {method.renderOpts("chgStat")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.syncType']}>
            {getFieldDecorator("syncType", {initialValue: "1"})(<Select size="small" className="size-id" disabled={!isOnl()} onChange={method.changeSyncType}>
              {method.renderOpts("syncType")}
            </Select>)}
          </Form.Item>
          <Form.Item label={locale['label.interfaceId']}>{getFieldDecorator("interfaceId")(<Input size="small" className="size-id" disabled={!props.isPageDelete()} />)}</Form.Item>
          <Form.Item label={locale['label.eaiYn']}>{getFieldDecorator("eaiYN", {valuePropName: "checked", initialValue: true})(<Switch size="small" />)}</Form.Item>
          <Form.Item label={locale['label.2pcYn']}>{getFieldDecorator("tpcYN", {valuePropName: "checked", initialValue: false})(<Switch size="small" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.jobType']}>{getFieldDecorator("jobType")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label={locale['label.trType']}>
            {getFieldDecorator("trType", {initialValue: "2"})(<Radio.Group className="size-id">
              {method.renderRadios('trType')}
            </Radio.Group>)}
          </Form.Item>
          <Form.Item label={locale['label.testDate']}>{getFieldDecorator("testDate", {
            initialValue: [moment(), moment().add(7, 'd')]
          })(<RangePicker size="small" allowClear={false} className="size-name" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label={locale['label.isMapping']}>{getFieldDecorator("dataMapYN", {valuePropName: "checked", initialValue: false})(<Checkbox className="size-id" />)}</Form.Item>
          {method.renderDataMap()}
        </div>
        <hr/>
        <div className="row">
          <div className="col-1">
            <div className="row">
              <Form.Item label={locale['label.sourceMethod']}>
                {getFieldDecorator("sendSystemType", {initialValue: "3"})(<Select size="small" className="size-id">
                  {method.renderOpts("sysType")}
                </Select>)}
              </Form.Item>
              <Form.Item label={locale['label.sourceSystem']}>
                {getFieldDecorator("sendSystemName")(<Input.Search size="small" readOnly onSearch={vars => { method.setSystem('send') }} className="size-id" />)}
              </Form.Item>
            </div>
            <div className="row">
              <Form.Item label={serviceTitle('send')}>{getFieldDecorator("sendServerName")(<Input size="small" className="size-id" />)}</Form.Item>
              <Form.Item label={locale['label.sourceInfo']}>{getFieldDecorator("sendDbInfo")(<Input size="small" className="size-id" />)}</Form.Item>
            </div>
            <div className="row">
              {method.renderSendSystem()}
            </div>
            <div className="row">
              <Form.Item label={locale['label.sndJobCode']}>
                {getFieldDecorator("sendJobCodeName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setBizCode('send') }} />)}
              </Form.Item>
              <Form.Item label={locale['label.sourceAdmin']}>
                {getFieldDecorator("sendAdminName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setUser('send') }} />)}
              </Form.Item>
            </div>
          </div>
          <div className="col-1">
            <div className="row">
              <Form.Item label={locale['label.targetMethod']}>
                {getFieldDecorator("rcvSystemType", {initialValue: "3"})(<Select size="small" className="size-id">
                  {method.renderOpts("sysType")}
                </Select>)}
              </Form.Item>
              <Form.Item label={locale['label.targetSystem']}>
                {getFieldDecorator("rcvSystemName")(<Input.Search size="small" readOnly onSearch={vars => { method.setSystem('rcv') }} className="size-id" />)}
              </Form.Item>
            </div>
            <div className="row">
              <Form.Item label={serviceTitle('rcv')}>{getFieldDecorator("rcvServerName")(<Input size="small" className="size-id" />)}</Form.Item>
              <Form.Item label={locale['label.targetInfo']}>{getFieldDecorator("rcvDbInfo")(<Input size="small" className="size-id" />)}</Form.Item>
            </div>
            <div className="row">
              {method.renderRcvSystem()}
            </div>
            <div className="row">
              <Form.Item label={locale['label.rcvJobCode']}>
                {getFieldDecorator("rcvJobCodeName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setBizCode('rcv') }} />)}
              </Form.Item>
              <Form.Item label={locale['label.targetAdmin']}>
                {getFieldDecorator("rcvAdminName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setUser('rcv') }} />)}
              </Form.Item>
            </div>
          </div>
        </div>
        <hr/>
        <div className="row">
          <Form.Item label={locale['label.etcRequest']} className="col-1">{getFieldDecorator("etcRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
          <Form.Item label={locale['label.eaiRequest']} className="col-1">{getFieldDecorator("eaiRemark")(<Input.TextArea className="urm-remark" disabled={!props.isPageDelete()} />)}</Form.Item>
        </div>
        <hr/>
        <div className="row">
          <Form.Item label={locale['label.attachmentFile']}>
            {getFieldDecorator('infFileName', {
              valuePropName: 'fileList', getValueFromEvent: method.uploadFile
            })(<Upload beforeUpload={method.preventAction} className="urm-upload">
              <Button icon="select" size="small" />
            </Upload>)}
          </Form.Item>
          <Form.Item style={{paddingTop: 1}}><Button icon="download" type="primary" size="small" onClick={method.downloadInfFile} /></Form.Item>
        </div>
      </Form>
      
      <SubModal ref={(list) => { sysList = list }} width="1300px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
        <SystemList key="list" path="system" codeList={props.codeList} onlySearch={true} />
      </SubModal>
      
      <SubModal ref={(list) => { bizList = list }} width="1240px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
        <BizList key="list" onlySearch={true} />
      </SubModal>
      
      <SubModal ref={(list) => { usrList = list }} width="1080px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
        <UserList key="list" path="user" authList={props.authList} onlySearch={true} />
      </SubModal>
      
      <SubModal ref={(list) => { mapList = list }} width="1140px">
        <DataMapList key="list" path="datamap" userInfo={props.userInfo} />
      </SubModal>
      
      <DataMapEditor ref={(map) => { dataMap = map }} path="datamap" userInfo={props.userInfo} codeList={props.codeList} />
    </div>
  );
}

const WrappedRequestEditor = Form.create({name:'request_editor'})(RequestEditorForm)
export default RequestEditor