import React from 'react'
import { Table, Input } from 'antd'
import * as urmsc from '@/urm-utils'

export default class LogBatchList extends React.Component {
  state = {
    items: [],
    details: [],
    batchId: '',
  }

  method = {
    getDetails: (record, _this) => {
      let params = {
        type: _this.props.devType,
        batchId: record.batchId,
      }
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/log/batch/detail',
        data: params,
        success: function(res) {
          _this.setState({details: res, batchId: record.batchId})
        }
      })
    },
    
    getStatus: (val) => {
      let text = '미완료'
      let color = '#0000ff'
      if(val === 'Y'){
        color = '#000000'
        text = '성공'
      } else if(val === 'E') {
        color = '#ff0000'
        text = '실패'
      }
      return <span style={{color: color}}>{text}</span>
    }
  }

  getRowKey = (record) => {
    return this.state.details.indexOf(record)
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped upper-list" scroll={{y: 400}}
          dataSource={this.state.items} pagination={false} bordered
          size="small" rowKey="batchId"
          onRow={(record, index) => {
            return {
              onDoubleClick: e => { this.method.getDetails(record, this) }
            }
          }
        }>
          <Table.Column title="인터페이스ID" dataIndex="interfaceId" width="130px" />
          <Table.Column title="상태" dataIndex="status" width="65px" render={(val) => ( this.method.getStatus(val) )} />
          <Table.Column title="총데이터" dataIndex="totalCount" width="85px" />
          <Table.Column title="성공" dataIndex="successCount" width="75px" />
          <Table.Column title="실패" dataIndex="failCount" width="75px" />
          <Table.Column title="시작시간" dataIndex="startTime" width="115px" />
          <Table.Column title="종료시간" dataIndex="endTime" width="115px" />
          <Table.Column title="전송디렉토리" dataIndex="sendFilePath" ellipsis />
          <Table.Column title="전송파일" dataIndex="sendFileName" ellipsis />
          <Table.Column title="수신디렉토리" dataIndex="rcvFilePath" ellipsis />
          <Table.Column title="수신파일" dataIndex="rcvFileName" ellipsis />
        </Table>
        
        <hr />
        <div>BATCH ID : <Input size="small" value={this.state.batchId} /></div>
        <Table className="table-striped"
            dataSource={this.state.details} pagination={false} bordered
            size="small" rowKey={this.getRowKey}>
          <Table.Column title="모델 컴포넌트 명" dataIndex="modelName" width="150px" />
          <Table.Column title="APPID" dataIndex="appId" width="145px" />
          <Table.Column title="처리 내용" dataIndex="processContent" />
          <Table.Column title="처리 결과" dataIndex="result" width="95px" />
          <Table.Column title="처리 시간" dataIndex="processTime" width="120px" />
          <Table.Column title="에러내용" dataIndex="errorContents" ellipsis />
        </Table>
      </div>
    );
  }
}