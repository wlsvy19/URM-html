import React from 'react'
import { Button, Modal, DatePicker } from 'antd'
import { Form, Input, Select, Radio, Switch, Checkbox } from 'antd'
import moment from 'moment'

import RuleEditor from './RuleEditor'
import {default as urmUtils} from '../../../urm-utils'

const KINDS = urmUtils.codeKey

class RequestEditor extends RuleEditor {
  constructor(props) {
    super(props)
    this.state.path = 'request'
  }
  
  method = {
    ...this.method,
    
    _customSetter: (setter, item, $form) => {
      setter.testDate = [moment(item.testStartYMD), moment(item.testEndYMD)]
      setter.sendSystemName = item.sendSystem.name
      setter.rcvSystemName = item.rcvSystem.name
    },
    
    _makeSaveItem: (data, children) => {
      data.testStartYMD = data.testDate[0].format('YYYYMMDD')
      data.testEndYMD = data.testDate[1].format('YYYYMMDD')
      data.sendSystemId = children.sendSystem ? children.sendSystem.id : this.state.item.sendSystemId
      data.rcvSystemId = children.rcvSystem ? children.rcvSystem.id : this.state.item.rcvSystemId
      console.log(data)
    },
  }

  render() {
    let $form

    return (
      <Modal visible={this.state.visible} onCancel={e => { this.method.handleCancel($form) }} width="1080px" footer={null} >
        <div style={{textAlign: "right", marginRight: "20px"}}>
          <Button onClick={(e) => { this.method.clickSave($form, this.method._makeSaveItem) }}>Save</Button>
        </div>
        <WrappedRequestEditor codeList={this.props.codeList} wrappedComponentRef={($this) => {
          $form = $this
          if ($this && this.state.visible) {
            this.method.setItem($this.props.form, this.state.item, this.method._customSetter)
          }
        }} />
      </Modal>
    );
  }
}

class RequestEditorForm extends React.Component {
  state = {
    children: {},
    useDataMap: false,
  }

  method = {
    renderOpts: (key) => {
      return urmUtils.getSubListByKey(this.props.codeList, "kind", KINDS[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
    
    changeDataMapUsage: e => {
      this.setState({useDataMap: e.target.checked})
    },
    
    setDataMap: (type) => {
      if (type === 'req') {
        this.setState({children: {reqDataMap: {}}})
        this.props.form.setFieldsValue({reqDataMappingId: 'test'})
      } else if (type === 'res') {
        this.setState({children: {resDataMap: {}}})
        this.props.form.setFieldsValue({resDataMappingId: 'test'})
      }
    },
    
    setSystem: (type) => {
      if (type === 'rcv') {
        this.setState({children: {rcvSystem: {}}})
        //this.props.form.setFieldsValue({rcvSystemName: ''})
      } else if (type === 'send') {
        this.setState({children: {sendSystem: {}}})
        //this.props.form.setFieldsValue({sendSystemName: ''})
      }
    }
  }

  render() {
    const { getFieldDecorator } = this.props.form
    const { RangePicker } = DatePicker

    return (
      <div className="urm-editor">
        <div className="row">
          <Form.Item label="Request ID" colon={false}>{getFieldDecorator("id")(<Input size="small" className="size-id" readOnly />)}</Form.Item>
          <Form.Item label="Type" colon={false}>
            {getFieldDecorator("interfaceType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {this.method.renderOpts("infType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ProcessState" colon={false}>
            {getFieldDecorator("processStat", {initialValue: "1"})(<Select size={"small"} className="size-name">
              {this.method.renderOpts("procStat")}
            </Select>)}
          </Form.Item>
          <Form.Item label="ChangeState" colon={false}>
            {getFieldDecorator("chgStat", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {this.method.renderOpts("chgStat")}
            </Select>)}
          </Form.Item>
        </div>
        <div className="row">
          <Form.Item label="Request Name" colon={false}>{getFieldDecorator("name")(<Input size="small" style={{width: "220px"}} />)}</Form.Item>
          <Form.Item label="syncType" colon={false}>
            {getFieldDecorator("syncType", {initialValue: "1"})(<Select size={"small"} className="size-id">
              {this.method.renderOpts("syncType")}
            </Select>)}
          </Form.Item>
          <Form.Item label="Interface ID" colon={false}>{getFieldDecorator("interfaceId")(<Input size="small" className="size-id" />)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="JobType" colon={false}>{getFieldDecorator("jobType")(<Input size="small" className="size-id" />)}</Form.Item>
          <Form.Item label="Is EAI" colon={false}>{getFieldDecorator("eaiYN", {valuePropName: "checked", initialValue: true})(<Switch size="small" />)}</Form.Item>
          <Form.Item label="Is 2pc" colon={false}>{getFieldDecorator("tpcYN", {valuePropName: "checked"})(<Switch size="small" />)}</Form.Item>
          <Form.Item label="TrType" colon={false}>
            {getFieldDecorator("trType", {initialValue: "2"})(<Radio.Group >
              <Radio value="1">OneWay</Radio>
              <Radio value="2">BothWay</Radio>
            </Radio.Group>)}</Form.Item>
        </div>
        <div className="row">
          <Form.Item label="TestDate" colon={false}>{getFieldDecorator("testDate", {
            initialValue: [moment(), moment().add(7, 'd')]
          })(<RangePicker size="small" style={{width: "220px"}} />)}</Form.Item>
          <Form.Item label="UseDataMap" colon={false}>{getFieldDecorator("dataMapYN", {valuePropName: "checked"})(<Checkbox onChange={this.method.changeDataMapUsage} />)}</Form.Item>
          <Form.Item label="Request" colon={false}>{getFieldDecorator("reqDataMappingId")(<Input.Search size="small" readOnly disabled={!this.state.useDataMap} onSearch={vars => { this.method.setDataMap('req') }} className="size-id" />)}</Form.Item>
          <Form.Item label="Reaponse" colon={false}>{getFieldDecorator("resDataMappingId")(<Input.Search size="small" readOnly disabled={!this.state.useDataMap} onSearch={vars => { this.method.setDataMap('res') }} className="size-id" />)}</Form.Item>
        </div>
        <div className="row">
          <div className="half-row">
            <Form.Item label="sendMethod" colon={false}>
              {getFieldDecorator("sendSystemType", {initialValue: "3"})(<Select size={"small"} className="size-id">
                {this.method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="SendSystem" colon={false}>{getFieldDecorator("sendSystemName")(<Input.Search size="small" readOnly onSearch={vars => { this.method.setSystem('send') }} className="size-id" />)}</Form.Item>
            <Form.Item label="SourceBizCode" colon={false}>{getFieldDecorator("sendJobCodeId")(<Input.Search size="small" readOnly className="size-id" />)}</Form.Item>
            <Form.Item label="sendAdmin" colon={false}>{getFieldDecorator("sendAdminId")(<Input.Search size="small" readOnly className="size-id" />)}</Form.Item>
          </div>
          <div className="half-row">
            <Form.Item label="rcvMethod" colon={false}>
              {getFieldDecorator("rcvSystemType", {initialValue: "3"})(<Select size={"small"} className="size-id">
                {this.method.renderOpts("sysType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="ReceiveSystem" colon={false}>{getFieldDecorator("rcvSystemName")(<Input.Search size="small" readOnly onSearch={vars => { this.method.setSystem('rcv') }} className="size-id" />)}</Form.Item>
            <Form.Item label="TargetBizCode" colon={false}>{getFieldDecorator("rcvJobCodeId")(<Input.Search size="small" readOnly className="size-id" />)}</Form.Item>
            <Form.Item label="ReceiveAdmin" colon={false}>{getFieldDecorator("rcvAdminId")(<Input.Search size="small" readOnly className="size-id" />)}</Form.Item>
          </div>
        </div>
        <div className="row">
          <Form.Item label="EtcRemark" colon={false} className="half-row">{getFieldDecorator("etcRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
          <Form.Item label="EaiRemark" colon={false} className="half-row">{getFieldDecorator("eaiRemark")(<Input.TextArea className="urm-remark" />)}</Form.Item>
        </div>
      </div>
    );
  }
}

const WrappedRequestEditor = Form.create({name:'request_editor'})(RequestEditorForm)
export default RequestEditor