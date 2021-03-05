import React from 'react'
import { Table, Button, Popover } from 'antd'
import moment from 'moment'

import RuleList from './RuleList'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

export default class HistoryList extends RuleList {
  componentDidMount() {
    this.method.search(this.props.reqId)
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size="small" scroll={{ x: 2800, y: 500 }} rowKey="chgSeq">
          <Table.Column title={locale['label.interfaceId']} dataIndex="interfaceId" width="120px"/>
          <Table.Column title={locale['label.interfaceType']} dataIndex="interfaceType" width="80px" render={(val) => ( this.method.getTypeStr('infType', val) )} />
          <Table.Column title={locale['label.requestName']} dataIndex="name" width="180px" />
          <Table.Column title={locale['label.changeStatus']} dataIndex="chgStat" render={(val) =>  ( this.method.getTypeStr('chgStat', val) )} />
          <Table.Column title={locale['label.processStatus']} dataIndex="processStat"  render={(val) => ( this.method.getTypeStr('procStat', val) )} />
          <Table.Column title="업무구분" dataIndex="jobType" />
          <Table.Column title="싱크타입" dataIndex="syncType" render={(val) => ( this.method.getTypeStr('syncType', val) )}/>
          <Table.Column title="송신방식" dataIndex="sendSystemType" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title="수신방식" dataIndex="rcvSystemType" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title={locale['label.sourceSystem']} dataIndex="sendSystem.name" width="110px" />
          <Table.Column title="송신업무" dataIndex="sendJobCode.part2Name" />
          <Table.Column title={locale['label.targetSystem']} dataIndex="rcvSystem.name" width="110px" />
          <Table.Column title="수신업무" dataIndex="rcvJobCode.part2Name" />
          <Table.Column title="2PC 여부" dataIndex="tpcYN" />
          <Table.Column title="전송유형" dataIndex="trType" render={(val) => ( this.method.getTypeStr('trType', val) )}/>
          <Table.Column title="요청 데이터 매핑" dataIndex="reqDataMappingId" width="120px" />
          <Table.Column title="응답 데이터 매핑" dataIndex="resDataMappingId" width="120px" />
          <Table.Column title="송신담당자" dataIndex="sendAdminId" width="100px" />
          <Table.Column title="수신담당자" dataIndex="rcvAdminId" width="100px" />
          <Table.Column title="등록자" dataIndex="regId" width="75px" />
          <Table.Column title={locale['label.changeId']} dataIndex="chgId" width="75px" />
          <Table.Column title={locale['label.changeDate']} dataIndex="chgDate" width="150px" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )}/>
          <Table.Column title="쿼리" dataIndex="sqlPlain" width="85px" render={(val) => (
            <Popover placement="topRight" title="쿼리" trigger="click" content={<div className="history-popover">{val}</div>}>
              <Button>쿼리</Button>
            </Popover>)
          } />
          <Table.Column title="기타 요청" dataIndex="etcRemark" width="115px" render={(val) => (
            <Popover placement="topRight" title="기타 요청" trigger="click" content={<div className="history-popover">{val}</div>}>
              <Button>기타 요청</Button>
            </Popover>)
          } />
          <Table.Column title="요청 응답" dataIndex="eaiRemark" width="115px" render={(val) => (
            <Popover placement="topRight" title="요청 응답" trigger="click" content={<div className="history-popover">{val}</div>}>
              <Button>요청 응답</Button>
            </Popover>)
          } />
        </Table>
      </div>
    );
  }
}
