import React from 'react'
import { Table, Tabs } from 'antd'
import * as urmsc from '@/urm-utils'

export default class LogDeferredList extends React.Component {
  state = {
    items: [],
    details: [],
    errors: [],
    type: 'log',
  }

  method = {
    getDetails: (record, _this) => {
      let params = {
        type: _this.props.devType,
        processDate: record.processDate,
        interfaceId: record.interfaceId,
      }
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/log/deferred/detail',
        data: params,
        success: function(res) {
          _this.setState({details: res})
        }
      })
    },
    
    changeTab: (key) => {
      this.setState({type: key})
    }
  }

  getRowKey = (list, record) => {
    return list.indexOf(record)
  }

  render() {
    return (
      <div className="urm-list">
        <Tabs defaultActiveKey="common" onChange={this.method.changeTab}>
          <Tabs.TabPane tab="로그데이터" key="log">
            <Table className="table-striped upper-list" scroll={{y: 400}}
              dataSource={this.state.items} pagination={false} bordered
              size="small" rowKey={(record) => ( this.getRowKey(this.state.items, record) )}
              onRow={(record, index) => {
                return {
                  onDoubleClick: e => { this.method.getDetails(record, this) }
                }
              }
            }>
              <Table.Column title="인터페이스ID" dataIndex="interfaceId" width="250px" />
              <Table.Column title="시작 시간" dataIndex="startTime" />
              <Table.Column title="종료 시간" dataIndex="endTime" />
              <Table.Column title="총 건수" dataIndex="totalCount" />
              <Table.Column title="성공 건수" dataIndex="successCount" />
              <Table.Column title="실패 건수" dataIndex="failCount" />
            </Table>

            <hr />

            <Table className="table-striped"
                dataSource={this.state.details} pagination={false} bordered
                size="small" rowKey={(record) => ( this.getRowKey(this.state.details, record) )}>
              <Table.Column title="인터페이스ID" dataIndex="interfaceId" width="215px" />
              <Table.Column title="시스템" dataIndex="appId" width="215px" />
              <Table.Column title="처리일자" dataIndex="processDate" />
              <Table.Column title="시작 시간" dataIndex="startTime" />
              <Table.Column title="종료 시간" dataIndex="endTime" />
              <Table.Column title="총 건수" dataIndex="totalCount" />
              <Table.Column title="성공 건수" dataIndex="successCount" />
              <Table.Column title="실패 건수" dataIndex="failCount" />
            </Table>
          </Tabs.TabPane>
          <Tabs.TabPane tab="오류데이터" key="error">
            <Table className="table-striped"
                dataSource={this.state.errors} pagination={false} bordered
                size="small" rowKey={(record) => ( this.getRowKey(this.state.errors, record) )}>
              <Table.Column title="인터페이스ID" dataIndex="interfaceId" width="145px" />
              <Table.Column title="처리 일자" dataIndex="processDate" width="110px" />
              <Table.Column title="에러 시간" dataIndex="messageTime" width="120px" />
              <Table.Column title="시스템" dataIndex="appId" width="145px" />
              <Table.Column title="오류데이터" dataIndex="errorContents" ellipsis />
              <Table.Column title="자세한에러" dataIndex="errorDataContents" ellipsis />
            </Table>
          </Tabs.TabPane>
        </Tabs>
      </div>
    );
  }
}