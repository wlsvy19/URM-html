import React from 'react'
import { Table, Button } from 'antd'

import * as urmsc from '@/urm-utils'
import SubModal from '@/components/SubModal'
import RealtimeMessageList from './RealtimeMessage-list'

export default class LogRealtimeList extends React.Component {
  state = {
    items: [],
    details: [],
  }

  method = {
    getDetails: (record, _this) => {
      let params = {
        type: _this.props.devType,
        processDate: record.processDate,
        globalId: record.globalId,
      }
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/log/online/detail',
        data: params,
        success: function(res) {
          _this.setState({details: res})
        }
      })
    },
    
    getResult: (val) => {
      let text = '성공'
      let color = '#000000'
      if(val !== 0){
        color = '#ff0000'
        text = '실패'
      }
      return <span style={{color: color}}>{text}</span>
    },
    
    getResultDetail: (val) => {
      let text = '실패'
      let color = '#ff0000'
      if(!val || val.length === 0){
        color = '#000000'
        text = '성공'
      }
      return <span style={{color: color}}>{text}</span>
    },
    
    getMessage: (record, _this) => {
      let params = {
        type: _this.props.devType,
        processDate: record.processDate,
        interfaceId: record.interfaceId,
        serialNumber: record.serialNumber,
      }
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/log/online/message',
        data: params,
        success: function(res) {
          _this.refs.msgModal.setState({visible: true})
          
          let msg = _this.method.setMsg(res)
          _this.refs.msgList.setState({items: res, msg: msg})
        }
      })
    },
    
    setMsg: (res) => {
      let message = ''
      if(res != null) {
        for(let i = 0; i < res.length; i++) {
          message += res[i].fieldValue
        }
      }
      return message
    }
  }

  getRowKey = (record) => {
    return this.state.items.indexOf(record)
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped upper-list" scroll={{y: 400}}
          dataSource={this.state.items} pagination={false} bordered
          size="small" rowKey={this.getRowKey}
          onRow={(record, index) => {
            return {
              onDoubleClick: e => { this.method.getDetails(record, this) }
            }
          }
        }>
          <Table.Column title="인터페이스 ID" dataIndex="interfaceId" width="145px" />
          <Table.Column title="Global ID" dataIndex="globalId" width="300px" />
          <Table.Column title="시작 시간" dataIndex="startTime" width="135px" />
          <Table.Column title="종료 시간" dataIndex="endTime" width="135px" />
          <Table.Column title="처리 시간" dataIndex="processTime" width="135px" />
          <Table.Column title="결과" width="75px" render={(val) => ( this.method.getResult(val.errorCode) )} />
          <Table.Column title="에러코드" dataIndex="errorCode" width="120px" />
        </Table>

        <hr />

        <Table className="table-striped"
            dataSource={this.state.details} pagination={false} bordered
            size="small" rowKey="serialNumber">
          <Table.Column title="송신단계" width="85px" render={(val) => ( val.processSeq === 0 ? val.stepIndex : '' )} />
          <Table.Column title="수신단계" width="85px" render={(val) => ( val.processSeq === 0 ? '' : val.stepIndex )} />
          <Table.Column title="처리시스템" dataIndex="serverName" width="155px" ellipsis />
          <Table.Column title="모델명" dataIndex="modelId" width="145px" />
          <Table.Column title="시스템ID" dataIndex="appId" width="130px" />
          <Table.Column title="시작 시간" dataIndex="startTime" width="115px" />
          <Table.Column title="종료 시간" dataIndex="endTime" width="115px" />
          <Table.Column title="처리 시간" dataIndex="processTime" width="115px" />
          <Table.Column title="결과" width="65px" render={(val) => ( this.method.getResultDetail(val.errMessage) )} />
          <Table.Column title="에러메시지" dataIndex="errMessage" ellipsis />
          <Table.Column title="전문헤더" dataIndex="header" width="90px"
            render={(val, record) => (<Button size="small" onClick={e => { this.method.getMessage(record, this) }}>보기</Button>)} />
        </Table>
        
        <SubModal ref="msgModal" width="800px">
          <RealtimeMessageList ref="msgList" />
        </SubModal>
      </div>
    );
  }
}