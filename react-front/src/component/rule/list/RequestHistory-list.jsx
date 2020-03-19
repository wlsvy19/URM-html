import React from 'react'
import { Table, Button, Popover } from 'antd'
import moment from 'moment'

import RuleList from './RuleList'

export default class HistoryList extends RuleList {
  componentDidMount() {
    this.method.search(this.props.scparam)
  }

  render() {
    return (
      <div className="urm-list">
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ x: 1900, y: 500 }} rowKey="chgSeq">
          <Table.Column title="InterfaceID" dataIndex="interfaceId" width="120px"/>
          <Table.Column title="Type" dataIndex="interfaceType" width="80px" render={(val) => ( this.method.getTypeStr('infType', val) )} />
          <Table.Column title="Name" dataIndex="name" width="180px" />
          <Table.Column title="ReqState" dataIndex="chgStat" width="100px" render={(val) =>  ( this.method.getTypeStr('chgStat', val) )} />
          <Table.Column title="State" dataIndex="processStat" width="80px" render={(val) => ( this.method.getTypeStr('procStat', val) )} />
          <Table.Column title="SendMethod" dataIndex="sendSystemType" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title="RcvMethod" dataIndex="rcvSystemType" width="100px" render={(val) => ( this.method.getTypeStr('sysType', val) )}/>
          <Table.Column title="SendSystem" dataIndex="sendSystem.name" width="95px" />
          <Table.Column title="SendJobCode" dataIndex="sendJobCode.part2Name" width="105px" />
          <Table.Column title="RcvSystem" dataIndex="rcvSystem.name" width="90px" />
          <Table.Column title="RcvJobCode" dataIndex="rcvJobCode.part2Name" width="100px" />
          <Table.Column title="SendAdmin" dataIndex="sendAdminId" width="85px" />
          <Table.Column title="RcvAdmin" dataIndex="rcvAdminId" width="85px" />
          <Table.Column title="Register" dataIndex="regId" width="75px" />
          <Table.Column title="Changer" dataIndex="chgId" width="75px" />
          <Table.Column title="LastUpdate" dataIndex="chgDate" width="150px" render={(val) => ( moment(val).format('YYYY-MM-DD HH:mm') )}/>/>
          <Table.Column title="Query" dataIndex="sqlPlain" width="85px" render={(val) => (
            <Popover placement="topRight" title="Query" trigger="click" content={<div className="history-popover">{val}</div>}>
              <Button>Query</Button>
            </Popover>)
          } />
          <Table.Column title="Etc" dataIndex="etcRemark" width="65px" render={(val) => (
            <Popover placement="topRight" title="Etc" trigger="click" content={<div className="history-popover">{val}</div>}>
              <Button>Etc</Button>
            </Popover>)
          } />
          <Table.Column title="EAIRemark" dataIndex="eaiRemark" width="115px" render={(val) => (
            <Popover placement="topRight" title="EAIRemark" trigger="click" content={<div className="history-popover">{val}</div>}>
              <Button>EAI Remark</Button>
            </Popover>)
          } />
        </Table>
      </div>
    );
  }
}
