import React from 'react'
import { Table, Button, Modal, message } from 'antd'
import { Form, Input } from 'antd'

import SubModal from '@/components/SubModal'
import UserList from '@/components/manage/list/User-list'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class TransferForm extends React.Component {
  componentDidMount() {
    if (!this.method.isPageDelete()) {
      let user = this.props.userInfo
      this.props.form.setFieldsValue({userId: user.id})
      this.props.search({loginUser: user.id})
    }
  }
  
  method = {
    confirm: e => {
      let userId = this.props.form.getFieldValue('userId')
      let trnUserId = this.props.form.getFieldValue('trnUserId')
      if (!userId || userId.trim().length === 0) {
        message.warning('담당자가 선택되지 않았습니다.')
        return false
      }
      if (!trnUserId || trnUserId.trim().length === 0) {
        message.warning('이관자가 선택되지 않았습니다.')
        return false
      }
      
      this.props.confirm(trnUserId, userId)
    },

    setUser: (type) => {
      if (!this.method.isPageDelete) return false
      let ref = this.refs.usrList
      let form = this.props.form
      
      let callback = (user) => {
        if (type === 'now') {
          this.props.search({loginUser: user.id})
          form.setFieldsValue({userId: user.id})
        } else if (type === 'trn') {
          form.setFieldsValue({trnUserId: user.id})
        }
        ref.setState({visible: false})
      }

      let childState = {
        list: {onDbClick: callback}
      }
      ref.setState({visible: true, childState: childState})
    },

    isPageDelete: () => {
      let auth = this.props.userInfo ? this.props.userInfo.auth : ''
      return urmsc.isPageEdit('request', 'delete', auth)
    }
  }

  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="담당자">
              {getFieldDecorator("userId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setUser('now') }} />)}
            </Form.Item>
            <Form.Item label="이관자">
              {getFieldDecorator("trnUserId")(<Input size="small" readOnly allowClear className="search-id" onClick={e => { this.method.setUser('trn') }} />)}
            </Form.Item>
            <Form.Item className="search-buttons row">
              <Button onClick={this.method.confirm}>이관</Button>
            </Form.Item>
          </div>
        </Form>

        <SubModal ref="usrList" width="1110px" bodyStyle={{maxHeight: 600, overflow: 'auto'}}>
          <UserList key="list" path="user" authList={this.props.authList} onlySearch={true} />
        </SubModal>
      </div>
    );
  }
}

class RequestTransfer extends React.Component {
  state = {
    items: [],
    selectors: [],
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    if (this.props.codeList && this.props.codeList.length !== nextProps.codeList.length) {
      return true
    }
    return false
  }
  
  method = {
    getTypeStr: (key, val) => {
      let kind = urmsc.CODEKEY[key]
      let list = this.props.codeList
      let obj = {}
      
      for(let i=0; i < list.length; i++) {
        let it = list[i]
        if (it.kind === kind && it.code === val) {
          obj = it
          break
        }
      }
      return obj.name
    },

    search: (param) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: 'api/request/list',
        data: param,
        success: function(res) {
          _this.setState({ items: res })
        },
      })
    },

    transfer: (trnUserId, userId) => {
      let tmp = [...this.state.selectors]
      if (tmp.length === 0) {
        message.warning('선택된 요건이 없습니다.')
        return false
      }
      tmp.forEach((it) => {
        it.regId = trnUserId
        it.sendAdminId = trnUserId
        it.rcvAdminId = trnUserId
      })
      let _this = this
      Modal.confirm({
        autoFocusButton: 'cancel',
        content: tmp.length + ' 의 요건이 선택되었습니다. 이관하시겠습니까?',
        cancelText: 'NO',
        okText: 'YES',
        onOk() {
          urmsc.ajax({
            type: 'POST',
            url: 'api/request/transfer',
            data: JSON.stringify(tmp),
            contentType: 'applicaion/json; charset=UTF-8',
            success: function(res) {
              message.success(res + ' 의 요건이 성공적으로 이관되었습니다.')
              _this.method.search({loginUser: userId})
            }
          })
        },
        onCancel() {
          message.info('취소되었습니다.')
        }
      })
    },
  }
  
  rowSelection = {
    columnWidth: 40,
    onSelect: (record, selected, selectedRows, nativeEvent) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it)
      })
      this.setState({selectors: tmp})
    },
    onSelectAll: (selected, selectedRows, changeRows) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it)
      })
      this.setState({selectors: tmp})
    },
  }
  
  searchForm = null

  render() {
    return (
      <div className="urm-list">
        <WrappedTransferForm {...this.props} search={this.method.search} confirm={this.method.transfer}
          wrappedComponentRef={ref => { if (ref) this.searchForm = ref.props.form }}/>
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size="small" scroll={{ y: 500 }} rowKey="id"
          rowSelection={this.rowSelection}>
          <Table.Column title={locale['label.requestId']} dataIndex="id" width="145px" />
          <Table.Column title={locale['label.requestName']} dataIndex="name" width="180px" />
          <Table.Column title={locale['label.interfaceType']} dataIndex="interfaceType" render={(val) => ( this.method.getTypeStr('infType', val) )} />
          <Table.Column title={locale['label.interfaceId']} dataIndex="interfaceId" width="120px"/>
          <Table.Column title={locale['label.sourceSystem']} dataIndex="sendSystem.name" width="110px"/>
          <Table.Column title={locale['label.targetSystem']} dataIndex="rcvSystem.name" width="110px"/>
          <Table.Column title="등록자" dataIndex="regId" width="110px"/>
          <Table.Column title="송신자" dataIndex="sendAdminId" width="110px"/>
          <Table.Column title="수신자" dataIndex="rcvAdminId" width="110px"/>
        </Table>
      </div>
    );
  }
}

const WrappedTransferForm = Form.create({name:'transfer_form'})(TransferForm)
export default RequestTransfer