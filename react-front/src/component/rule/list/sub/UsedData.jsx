import React from 'react'
import { Table, Button } from 'antd'

import ResizeableTitle from '../../ResizeableTitle'
import * as urmsc from '../../../../urm-utils'

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
        url: '/URM/data/used',
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
      return !this.props.onlySearch && <div className="row">
        <span>변경하실 경우 아래의 요건에 영항이 있습니다.</span><div className="flex-right"><Button icon="save" onClick={this.props.onDbClick} /></div>
      </div>
    }
  }

  render() {
    const { colWidth } = this.state

    return (
      <div className="urm-list">
        <div className="row">
          <span>변경하실 경우 아래의 요건에 영항이 있습니다.</span><div className="flex-right"><Button icon="save" onClick={this.props.onDbClick} /></div>
        </div>
        <Table className="table-striped" components={this.components}
            dataSource={this.state.items} pagination={false} bordered
            size={"small"} scroll={{ y: 500 }}>
          <Table.Column title="Request ID" dataIndex="reqId" width={colWidth[0]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(0) })} />
          <Table.Column title="Request Name" dataIndex="reqName" width={colWidth[1]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(1) })} />
          <Table.Column title="Mapping Id" dataIndex="mapId" width={colWidth[2]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(2) })}  />
          <Table.Column title="Mapping Name" dataIndex="mapName" width={colWidth[3]}
            onHeaderCell={column => ({ width: column.width, onResize: this.method.handleResize(3) })}/>
        </Table>
      </div>
    );
  }
}

UsedData.defaultProps = {
  onlySearch: true
}
