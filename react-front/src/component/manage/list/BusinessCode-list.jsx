import React from 'react'
import { Table, Button } from 'antd'
import { Form, Input } from 'antd'

class BizSearch extends React.Component {
  componentDidMount() {
    this.props.search()
  }
  
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
    clickAdd: e => {
      this.props.add()
    },
  }

  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="Name">{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label="Level1 ID">{getFieldDecorator("part1Id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Level1 Name">{getFieldDecorator("part1Name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label="Level2 ID">{getFieldDecorator("part2Id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Level2 Name">{getFieldDecorator("part2Name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item style={{marginLeft: "15px"}}>
              <Button onClick={this.method.clickSearch} icon="search" />
              <Button onClick={this.method.clickAdd} icon="plus" />
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

class BizList extends React.Component {
  state = {
    items: [],
    onDbClick: undefined,
  }

  render() {
    //TODO editable table
    return (
      <div className="urm-list">
        <Table className="table-striped"
          dataSource={this.state.items} pagination={false} bordered
          size={"small"} scroll={{ y: 500 }} rowKey="id"
          onRow={(record, index) => {
            return {
              onDoubleClick: e => { if (this.state.onDbClick) this.state.onDbClick(record) }
            }
          }
        }>
          <Table.Column title="ID" dataIndex="id" width="150px"/>
          <Table.Column title="Name" dataIndex="name" width="250px"/>
          <Table.Column title="Level1 ID" dataIndex="part1Id"/>
          <Table.Column title="Level1 Name" dataIndex="part1Name"/>
          <Table.Column title="Level2 ID" dataIndex="part2Id"/>
          <Table.Column title="Level2 Name" dataIndex="part2Name"/>
        </Table>
      </div>
    );
  }
}

const WrappedBizSearch = Form.create({name:'biz_search'})(BizSearch)
export default BizList
export {WrappedBizSearch}