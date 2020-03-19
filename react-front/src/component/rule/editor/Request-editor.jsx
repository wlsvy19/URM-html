import React, { useEffect } from 'react'
import { Button, Modal, Popover, DatePicker, message } from 'antd'
import { Form, Input, Select, Radio, Switch, Checkbox } from 'antd'
import moment from 'moment'

import RuleEditor from './RuleEditor'
import SubModal from '../SubModal'

import DataMapEditor from './DataMap-editor'
import DataMapList from '../list//DataMap-list'
import SystemList from '../list/System-list'
import BizList from '../../manage/list/BusinessCode-list'
import UserList from '../../manage/list/User-list'

import * as urmsc from '../../../urm-utils'

class RequestEditor extends RuleEditor {
  customMethod = {
    validator: (data) => {
      if (!data.sendSystemId || data.sendSystemId.trim().length === 0) {
        message.warning('please input sendSystemId')
        return false
      }
      if (!data.rcvSystemId || data.rcvSystemId.trim().length === 0) {
        message.warning('please input rcvSystemId')
        return false
      }
      if (!data.sendJobCodeId || data.sendJobCodeId.trim().length === 0) {
        message.warning('please input sendJobCodeId')
        return false
      }
      if (!data.rcvJobCodeId || data.rcvJobCodeId.trim().length === 0) {
        message.warning('please input rcvJobCodeId')
        return false
      }
      if (!data.sendAdminId || data.sendAdminId.trim().length === 0) {
        message.warning('please input sendAdminId')
        return false
      }
      if (!data.rcvAdminId  || data.rcvAdminId.trim().length === 0) {
        message.warning('please input rcvAdminId')
        return false
      }
      
      /*if( !CodeManager.isSelectAuth("processStat", String(cbPrcStat.selectedItem.data) ) ) {
        console.log('message.1010');
        return false;
      }
      if( !CodeManager.isSelectAuth("chgStat", String(cbChgStat.selectedItem.data) ) ) {
        console.log('\'변경 신청\' 으로 수정합니다.');
        data.chgStat = 0;
      }*/
      return true
    },
  }

  render() {
    return (
      <Modal visible={this.state.visible} width="1080px"
          footer={null} onCancel={this.method.handleCancel} className="urm-modal">
        <WrappedRequestEditor codeList={this.props.codeList} item={this.state.item} {...this.childMethod} />
      </Modal>
    );
  }
}

const RequestEditorForm = (props) => {
  const { form, item } = props
  const { getFieldDecorator } = form
  const { RangePicker } = DatePicker

  // Only re-run the effect if props.item changes
  useEffect(() => {
    props.setItem(form, item, method.initialSetter)
    item.children = {}
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

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
      formItem.sendAdminName = propsItem.sendAdmin.name + '(' + propsItem.sendAdmin.positionName + ')'
      formItem.rcvAdminName = propsItem.rcvAdmin.name + '(' + propsItem.rcvAdmin.positionName + ')'
    },

    makeRequestObj: () => {
      let saveItem = props.makeRuleObj(form, item)
      
      let testDate = form.getFieldValue('testDate')
      saveItem.testStartYMD = testDate[0].format('YYYYMMDD')
      saveItem.testEndYMD = testDate[1].format('YYYYMMDD')
      
      let children = item.children
      if (children.sendSystem) saveItem.sendSystemId = children.sendSystem.id
      if (children.rcvSystem) saveItem.rcvSystemId = children.rcvSystem.id
      if (children.sendJobCode) saveItem.sendJobCodeId = children.sendJobCode.id
      if (children.rcvJobCode) saveItem.rcvJobCodeId = children.rcvJobCode.id
      if (children.sendAdmin) saveItem.sendAdminId = children.sendAdmin.id
      if (children.rcvAdmin) saveItem.rcvAdminId = children.rcvAdmin.id
      
      return saveItem
    },

    setDataMap: (type) => {
      let ref = mapList
      // TODO reuse mapping??
      /* let callback = (map) => {
        if (type === 'req') {
          item.reqDataMap = map
          form.setFieldsValue({reqDataMappingId: map.id})
        } else if (type === 'res') {
          item.resDataMap = map
          form.setFieldsValue({resDataMappingId: map.id})
        }
        ref.setState({visible: false})
      } */
      
      ref.setState({visible: true})
    },

    editDataMap: (type, e) => {
      let ref = dataMap
      let id = e.target.value
      if (id && id.trim().length) {
        urmsc.ajax({
          type: 'GET',
          url: '/URM/datamap/' + id,
          success: function(res) {
            ref.setState({visible: true, item: res})
          }
        })
      } else {
        ref.setState({visible: true, item: {}})
      }
    },

    setSystem: (type) => {
      let ref = sysList
      let children = item.children
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
      let children = item.children
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
      let children = item.children
      let callback = (user) => {
        let name = user.name + '(' + user.positionName + ')'
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
        console.log(form.getFieldsValue())
        /*let query = form.getFieldValue('sql')
        let strs = query.split(' ')
        item.children.sql = query
        item.children.sqlPlain = query
        form.setFieldsValue({query: strs[0] + ' ...'})*/
      } else {
        form.setFieldsValue({sql: item.children.sql ? item.children.sql : item.sql})
      }
    },

    renderOpts: (key) => {
      return urmsc.getSubListByKey(props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },

    renderSendSystem: () => {
      let type = form.getFieldValue('sendSystemType')
      if (type === '2') {
        let textarea = <Form.Item>{getFieldDecorator("sql")(<Input.TextArea style={{width: "390px", height: "200px"}} />)}</Form.Item>
        return (
          <Form.Item label="Query">{getFieldDecorator("query")
            (<Popover placement="topLeft" title="Query" trigger="click" content={textarea}
                onVisibleChange={method.setQuery}>
              <Input size="small" readOnly style={{width: "390px"}} />
            </Popover>)}
          </Form.Item>
        );
      }
      return undefined
    },

    renderRcvSystem: () => {
      let type = form.getFieldValue('rcvSystemType')
      if (type === '1') {
        return (
          <Form.Item label="File CRUD">
            {getFieldDecorator("fileCrudType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("fileCrudType")}
            </Select>)}
          </Form.Item>
        );
      } else if (type === '2') {
        return (
          <Form.Item label="DB CRUD">
            {getFieldDecorator("dbCrudType", {initialValue: "1"})(<Select size={"small"} className="size-name">
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
              {getFieldDecorator("reqDataMappingId", reqProps)(<Input.Search size="small" readOnly
                onSearch={vars => { method.setDataMap('req') }} onClick={e => { method.editDataMap('req', e) }} className="size-id" />)}
            </Form.Item>
            <Form.Item label="Response">
              {getFieldDecorator("resDataMappingId", resProps)(<Input.Search size="small" readOnly
                onSearch={vars => { method.setDataMap('res') }} onClick={e => { method.editDataMap('res', e) }} className="size-id" />)}
            </Form.Item>
          </div>
        );
      }
      return undefined
    },
  }
  
  let serviceTitle = (type) => {
    let sysType = '3'
    if (type === 'send') {
      sysType = form.getFieldValue('sendSystemType')
      switch (sysType) {
        case '1': return 'SendFileName'
        case '2': return 'SendTable'
        default: return 'SendService'
      }
    } else if (type === 'rcv') {
      sysType = form.getFieldValue('rcvSystemType')
      switch (sysType) {
        case '1': return 'RcvFileName'
        case '2': return 'RcvTable'
        default: return 'RcvService'
      }
    }
    return 'Service'
  }

  return (
    <div className="urm-editor">
      <div className="editor-buttons">
        <Button onClick={method.clickSave}>Save</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="Request ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="Request Name">{getFieldDecorator("name")(<Input size="small" className="size-name" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="Type">
            {getFieldDecorator("interfaceType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("infType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ProcessState">
            {getFieldDecorator("processStat", {initialValue: "1"})(<Select size={"small"} style={{width: "150px"}}>
              {method.renderOpts("procStat")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ChangeState">
            {getFieldDecorator("chgStat", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("chgStat")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label="syncType">
            {getFieldDecorator("syncType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("syncType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="Interface ID">{getFieldDecorator("interfaceId")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="Is EAI">{getFieldDecorator("eaiYN", {valuePropName: "checked", initialValue: true})(<Switch size="small" />)}</Form.Item>
          <Form.Item label="Is 2pc">{getFieldDecorator("tpcYN", {valuePropName: "checked", initialValue: false})(<Switch size="small" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="JobType">{getFieldDecorator("jobType")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="TrType">
            {getFieldDecorator("trType", {initialValue: "2"})(<Radio.Group >
              <Radio value="1">OneWay</Radio>
              <Radio value="2">BothWay</Radio>
            </Radio.Group>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label="TestDate">{getFieldDecorator("testDate", {
            initialValue: [moment(), moment().add(7, 'd')]
          })(<RangePicker size="small" allowClear={false} className="size-name" />)}</Form.Item>
          <Form.Item label="UseDataMap">{getFieldDecorator("dataMapYN", {valuePropName: "checked", initialValue: false})(<Checkbox />)}</Form.Item>
          {method.renderDataMap()}
        </div>
        <hr/>
        <div className="row">
          <div className="col-1">
            <div className="row">
              <Form.Item label="sendMethod">
                {getFieldDecorator("sendSystemType", {initialValue: "3"})(<Select size={"small"} className="size-id">
                  {method.renderOpts("sysType")}
                </Select>)}
              </Form.Item>
              <Form.Item label="SendSystem">
                {getFieldDecorator("sendSystemName")(<Input.Search size="small" readOnly onSearch={vars => { method.setSystem('send') }} className="size-id" />)}
              </Form.Item>
            </div>
            <div className="row">
              <Form.Item label={serviceTitle('send')}>{getFieldDecorator("sendServerName")(<Input size="small" className="size-id" />)}</Form.Item>
              <Form.Item label="SendInfo">{getFieldDecorator("sendDbInfo")(<Input size="small" className="size-id" />)}</Form.Item>
            </div>
            <div className="row">
              {method.renderSendSystem()}
            </div>
            <div className="row">
              <Form.Item label="SourceBizCode">
                {getFieldDecorator("sendJobCodeName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setBizCode('send') }} />)}
              </Form.Item>
              <Form.Item label="sendAdmin">
                {getFieldDecorator("sendAdminName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setUser('send') }} />)}
              </Form.Item>
            </div>
          </div>
          <div className="col-1">
            <div className="row">
              <Form.Item label="rcvMethod">
                {getFieldDecorator("rcvSystemType", {initialValue: "3"})(<Select size={"small"} className="size-id">
                  {method.renderOpts("sysType")}
                </Select>)}
              </Form.Item>
              <Form.Item label="ReceiveSystem">
                {getFieldDecorator("rcvSystemName")(<Input.Search size="small" readOnly onSearch={vars => { method.setSystem('rcv') }} className="size-id" />)}
              </Form.Item>
            </div>
            <div className="row">
              <Form.Item label={serviceTitle('rcv')}>{getFieldDecorator("rcvServerName")(<Input size="small" className="size-id" />)}</Form.Item>
              <Form.Item label="ReceiveInfo">{getFieldDecorator("rcvDbInfo")(<Input size="small" className="size-id" />)}</Form.Item>
            </div>
            <div className="row">
              {method.renderRcvSystem()}
            </div>
            <div className="row">
              <Form.Item label="TargetBizCode">
                {getFieldDecorator("rcvJobCodeName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setBizCode('rcv') }} />)}
              </Form.Item>
              <Form.Item label="ReceiveAdmin">
                {getFieldDecorator("rcvAdminName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setUser('rcv') }} />)}
              </Form.Item>
            </div>
          </div>
        </div>
        <hr/>
        <div className="row">
          <Form.Item label="EtcRemark" className="col-1">{getFieldDecorator("etcRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
          <Form.Item label="EaiRemark" className="col-1">{getFieldDecorator("eaiRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
        </div>
      </Form>
      
      <SubModal ref={(list) => { sysList = list }} width="1120px">
        <SystemList key="list" path="system" codeList={props.codeList} onlySearch={true} />
      </SubModal>
      
      <SubModal ref={(list) => { bizList = list }} width="1215px">
        <BizList key="list" onlySearch={true} />
      </SubModal>
      
      <SubModal ref={(list) => { usrList = list }} width="980px">
        <UserList key="list" path="user" onlySearch={true} />
      </SubModal>
      
      <SubModal ref={(list) => { mapList = list }} width="1030px">
        <DataMapList key="list" path="datamap" />
      </SubModal>
      
      <DataMapEditor ref={(map) => { dataMap = map }} path="datamap" codeList={props.codeList} onlySearch={true} />
    </div>
  );
}

const WrappedRequestEditor = Form.create({name:'request_editor'})(RequestEditorForm)
export default RequestEditor