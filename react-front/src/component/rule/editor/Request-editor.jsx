import React, { useEffect } from 'react'
import { Button, Modal, DatePicker } from 'antd'
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
        return false
      }
      if (!data.rcvSystemId || data.rcvSystemId.trim().length === 0) {
        return false
      }
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
    // TODO subscribe...
    // eslint-disable-next-line
  }, [item]);

  let sysList = null;
  let bizList = null;
  let usrList = null;
  let mapList = null;
  let dataMap = null;

  let method = {
    renderOpts: (key) => {
      return urmsc.getSubListByKey(props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },

    clickSave: e => {
      let children = {
        sendSystem: item.sendSystem,
        rcvSystem: item.rcvSystem,
        sendJobCode: item.sendJobCode,
        rcvJobCode: item.rcvJobCode
      }
      console.log(form.getFieldsValue(), children)
      props.save(form, children, method.makeSaveItem)
    },

    initialSetter: (formItem, propsItem) => {
      formItem.testDate = [moment(propsItem.testStartYMD), moment(propsItem.testEndYMD)]
      formItem.sendSystemName = propsItem.sendSystem.name
      formItem.rcvSystemName = propsItem.rcvSystem.name
      formItem.sendJobCodeName = propsItem.sendJobCode.part2Name + '(' + propsItem.sendJobCode.part2Id + ')'
      formItem.rcvJobCodeName = propsItem.rcvJobCode.part2Name + '(' + propsItem.rcvJobCode.part2Id + ')'
    },

    makeSaveItem: (saveItem, data, children) => {
      saveItem.testStartYMD = data.testDate[0].format('YYYYMMDD')
      saveItem.testEndYMD = data.testDate[1].format('YYYYMMDD')
      saveItem.sendSystemId = children.sendSystem ? children.sendSystem.id : item.sendSystemId
      saveItem.rcvSystemId = children.rcvSystem ? children.rcvSystem.id : item.rcvSystemId
    },

    setDataMap: (type) => {
      let ref = mapList
      urmsc.ajax({
        type: 'GET',
        url: '/URM/datamap',
        success: function(res) {
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
          //ref.method.changeChildState({items: res})
        }
      })
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
      urmsc.ajax({
        type: 'GET',
        url: '/URM/system',
        success: function(res) {
          let callback = (system) => {
            if (type === 'rcv') {
              item.rcvSystem = system
              form.setFieldsValue({rcvSystemName: system.name})
            } else if (type === 'send') {
              item.sendSystem = system
              form.setFieldsValue({sendSystemName: system.name})
            }
            ref.setState({visible: false})
          }
          
          ref.setState({visible: true})
          //ref.method.changeChildState({items: res, onDbClick: callback})
        }
      })
    },

    setBizCode: (type) => {
      let ref = bizList
      urmsc.ajax({
        type: 'GET',
        url: '/URM/code/business',
        success: function(res) {
          let callback = (biz) => {
            let name = biz.part2Name + '(' + biz.part2Id + ')'
            if (type === 'rcv') {
              item.rcvJobCode = biz
              form.setFieldsValue({rcvJobCodeName: name})
            } else if (type === 'send') {
              item.sendJobCode = biz
              form.setFieldsValue({sendJobCodeName: name})
            }
            ref.setState({visible: false})
          }
          
          ref.setState({visible: true})
          ref.method.changeChildState({items: res, onDbClick: callback})
        }
      })
    },

    setUser: (type) => {
      let ref = usrList
      urmsc.ajax({
        type: 'GET',
        url: '/URM/user',
        success: function(res) {
          let callback = (user) => {
            let name = user.mame + '(' + user.positionNm + ')'
            if (type === 'rcv') {
              item.rcvUser = user
              form.setFieldsValue({rcvUserName: name})
            } else if (type === 'send') {
              item.sendUser = user
              form.setFieldsValue({sendUserName: name})
            }
            ref.setState({visible: false})
          }
          
          ref.setState({visible: true})
          ref.method.changeChildState({items: res, onDbClick: callback})
        }
      })
    },

    renderSendSystem: () => {
      let type = form.getFieldValue('sendSystemType')
      if (type === '2') {
        return (
          <Form.Item label="Query">{getFieldDecorator("sql")(<Input size="small" readOnly className="size-id" />)}</Form.Item>
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
            {getFieldDecorator("dbCrudType", {initialValue: "1"})(<Select size={"small"} className="size-id">
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

  return (
    <div className="urm-editor">
      <div style={{textAlign: "right", marginRight: "20px"}}>
        <Button onClick={method.clickSave}>Save</Button>
      </div>
      <Form colon={false}>
        <div className="row">
          <Form.Item label="Request ID">{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="Request Name">{getFieldDecorator("name")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="Type">
            {getFieldDecorator("interfaceType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {method.renderOpts("infType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ProcessState">
            {getFieldDecorator("processStat", {initialValue: "1"})(<Select size={"small"} className="size-name">
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
          })(<RangePicker size="small" allowClear={false} style={{width: "220px"}} />)}</Form.Item>
          <Form.Item label="UseDataMap">{getFieldDecorator("dataMapYN", {valuePropName: "checked", initialValue: false})(<Checkbox />)}</Form.Item>
          {method.renderDataMap()}
        </div>
        <hr/>
        <div className="row">
          <div className="col-1">
            <Form.Item label="sendMethod">
              {getFieldDecorator("sendSystemType", {initialValue: "3"})(<Select size={"small"} className="size-id">
                {method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="SendSystem">
              {getFieldDecorator("sendSystemName")(<Input.Search size="small" readOnly onSearch={vars => { method.setSystem('send') }} className="size-id" />)}
            </Form.Item>
            <Form.Item label="SendService">{getFieldDecorator("sendServerName")(<Input size="small" className="size-id" />)}</Form.Item>
            <Form.Item label="SendInfo">{getFieldDecorator("sendDbInfo")(<Input size="small" className="size-id" />)}</Form.Item>
            <Form.Item label="SourceBizCode">
              {getFieldDecorator("sendJobCodeName")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setBizCode('send') }} />)}
            </Form.Item>
            <Form.Item label="sendAdmin">
              {getFieldDecorator("sendAdminId")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setUser('send') }} />)}
            </Form.Item>
          </div>
          <div className="col-1">
            <Form.Item label="rcvMethod">
              {getFieldDecorator("rcvSystemType", {initialValue: "3"})(<Select size={"small"} className="size-id">
                {method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="ReceiveSystem">
              {getFieldDecorator("rcvSystemName")(<Input.Search size="small" readOnly onSearch={vars => { method.setSystem('rcv') }} className="size-id" />)}
            </Form.Item>
            <Form.Item label="ReceiveService">{getFieldDecorator("rcvServerName")(<Input size="small" className="size-id" />)}</Form.Item>
            <Form.Item label="ReceiveInfo">{getFieldDecorator("rcvDbInfo")(<Input size="small" className="size-id" />)}</Form.Item>
            <Form.Item label="TargetBizCode">{getFieldDecorator("rcvJobCodeName")
              (<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setBizCode('rcv') }} />)}
            </Form.Item>
            <Form.Item label="ReceiveAdmin">
              {getFieldDecorator("rcvAdminId")(<Input.Search size="small" readOnly className="size-id" onSearch={vars => { method.setUser('rcv') }} />)}
            </Form.Item>
          </div>
        </div>
        <hr/>
        <div className="row">
          <Form.Item label="EtcRemark" className="half-row">{getFieldDecorator("etcRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
          <Form.Item label="EaiRemark" className="half-row">{getFieldDecorator("eaiRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
        </div>
      </Form>
      
      <SubModal ref={(list) => { sysList = list }} width="1120px"
        render={() => (<SystemList ref="child" path="system" codeList={props.codeList} onlySearch={true} />)} />
      
      <SubModal ref={(list) => { bizList = list }} width="980px"
        render={() => (<BizList ref="child" />)} />
      
      <SubModal ref={(list) => { usrList = list }} width="980px"
        render={() => (<UserList ref="child" path="user" />)} />
      
      <SubModal ref={(list) => { mapList = list }} width="1030px"
        render={() => (<DataMapList ref="child" path="datamap" />)} />
      
      <DataMapEditor ref={(map) => { dataMap = map }} path="datamap" codeList={props.codeList} onlySearch={true} />
    </div>
  );
}

const WrappedRequestEditor = Form.create({name:'request_editor'})(RequestEditorForm)
export default RequestEditor