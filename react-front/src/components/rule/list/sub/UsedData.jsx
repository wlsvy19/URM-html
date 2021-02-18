import React from 'react'
import { Table, Button } from 'antd'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

export default class UsedData extends React.Component {
 state = {
   items: [],
  }

  componentDidMount() {
    this.method.search(this.props.dataId)
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    return false
  }

  method = {
    search: (param) => {
      let setItems = this.method.setItems
      urmsc.ajax({
        type: 'GET',
        url: 'api/data/used',
        data: param,
        success: function(list) {
          list.forEach((it, idx) => {
            it.key = idx
          })
          setItems(list)
        },
      })
    },
    
    setItems: (list) => {
      this.setState({items: list})
    },
    
    renderButton: () => {
      return this.props.isEditor && <div className="row" style={{marginTop: 5}}>
        <span>{locale['message.0008']}</span>
        <div className="flex-right"><Button onClick={this.props.onDbClick}>{locale['label.confirm']}</Button></div>
      </div>
    }
  }

  render() {
    return (
      <div className="urm-list">
        {this.method.renderButton()}
        <Table className="table-striped"
            dataSource={this.state.items} pagination={false} bordered
            size="small" scroll={{ y: 500 }}>
          <Table.Column title={locale['label.requestId']} dataIndex="reqId" width="150px" />
          <Table.Column title={locale['label.requestName']} dataIndex="reqName" />
          <Table.Column title={locale['label.dataMapId']} dataIndex="mapId" width="150px" />
          <Table.Column title={locale['label.dataMapName']} dataIndex="mapName" />
        </Table>
      </div>
    );
  }
}

UsedData.defaultProps = {
  isEditor: true
}
