import React from 'react'
import { Table, Button } from 'antd'

import ResizeableTitle from '@/component/ResizeableTitle'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

export default class UsedData extends React.Component {
 state = {
   items: [],
   colWidth: [
     150, 250, 150, 250
   ],
  }

  components = {
    header: {
      cell: ResizeableTitle,
    },
  }

  componentDidMount() {
    this.method.search(this.props.scparam)
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    if (JSON.stringify(this.props.scparam) !== JSON.stringify(nextProps.scparam)) {
      return true
    }
    return false
  }

  componentDidUpdate(prevProps, prevState) {
    if (JSON.stringify(this.props.scparam) !== JSON.stringify(prevProps.scparam)) {
      this.method.search(this.props.scparam)
    }
  }

  method = {
    handleResize: index => (e, prop) => {
      console.log(prop)
      if (prop) {
      let { size } = prop
      this.setState(({ colWidth }) => {
        const nextColumns = [...colWidth]
        nextColumns[index] = size.width
        return { colWidth: nextColumns }
      })
      }
      return false
    },
    
    search: (param) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: 'api/data/used',
        data: param,
        success: function(list) {
          list.forEach((it, idx) => {
            it.key = idx
          })
          $this.setState({items: list})
        },
      })
    },
    
    renderButton: () => {
      return this.props.isEditor && <div className="row">
        <span>{locale['message.0008']}</span>
        <div className="flex-right"><Button icon="save" onClick={this.props.onDbClick} title={locale['label.save']} /></div>
      </div>
    }
  }

  render() {
    const { colWidth } = this.state

    return (
      <div className="urm-list">
        {this.method.renderButton()}
        <Table className="table-striped" components={this.components}
            dataSource={this.state.items} pagination={false} bordered
            size={"small"} scroll={{ y: 500 }}>
          <Table.Column title={locale['label.requestId']} dataIndex="reqId" width={colWidth[0]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(0) })} />
          <Table.Column title={locale['label.requestName']} dataIndex="reqName" width={colWidth[1]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(1) })} />
          <Table.Column title={locale['label.dataMapId']} dataIndex="mapId" width={colWidth[2]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(2) })}  />
          <Table.Column title={locale['label.dataMapName']} dataIndex="mapName" width={colWidth[3]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(3) })}/>
        </Table>
      </div>
    );
  }
}

UsedData.defaultProps = {
  isEditor: true
}
