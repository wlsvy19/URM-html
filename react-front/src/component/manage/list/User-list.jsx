import React from 'react'
import { Table, Button, Input, Form } from 'antd'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class UserSearch extends React.Component {
  state = {
    vlsible: false,
  }

  componentDidMount() {
    this.props.search()
  }

  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },

    clickAdd: e => {
      this.props.edit()

    },

    renderButton: (render) => {
      return !this.props.onlySearch && render
    },
  }

  render() {
    const { getFieldDecorator } = this.props.form
    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label={locale['label.id']}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.name']} >{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label={locale['label.dept']}>{getFieldDecorator("dept")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item className="search-buttons row">
              <Button onClick={this.method.clickSearch} icon="search" title={locale['label.search']} />
              {this.method.renderButton(
                <div className="inline">
                  <Button onClick={this.method.clickAdd} icon="plus" title={locale['label.add']} />
                </div>
              )}
            </Form.Item>
          </div>
        </Form>
      </div>
    )
  }
}

class UserList extends React.Component {
  state = {
    items: [],
    onDbClick: undefined,
  }

  method = {
    getAuthStr: (key) => {
      let obj = {}
      let list = this.props.authList
      for (let i = 0; i < list.length; i++) {
        let it = list[i]
        if (it.id === key) {
          obj = it
          break
        }
      }
      return obj.name
    },

    clickEdit: (id) => {
      this.props.edit(id)
    },

    search: (param) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: 'api/' + this.props.path,
        data: param,
        success: function (list) {
          $this.setState({ items: list })
        },

      })
    },

    renderButton: (render) => {
      return !this.props.onlySearch && render
    },
  }

  onRow = (record, index) => {
    return {
      onDoubleClick: e => { if (this.props.onDbClick) this.props.onDbClick(record) }
     }
  }

  render() {
    return (

      <div className="urm-list">
        <WrappedUserSearch {...this.props} search={this.method.search} />
        <Table className="table-striped" dataSource={this.state.items} pagination={false}
            bordered size={"small"} scroll={{ x: 1200, y: 500 }} rowKey="id" onRow={this.onRow}>
          <Table.Column title={locale['label.id']} dataIndex="id" width="130px" />
          <Table.Column title={locale['label.name']} dataIndex="name" width="130px" />
          <Table.Column title={locale['label.dept']} dataIndex="dept" width="130px" />
          <Table.Column title={locale['label.position']} dataIndex="positionName" width="130px" />
          <Table.Column title={locale['label.grade']} dataIndex="gradeName" width="130px" />
          <Table.Column title={locale['label.generalTel']} dataIndex="generalTelNo" width="130px" />
          <Table.Column title={locale['label.officeTel']} dataIndex="officeTelNo" width="130px" />
          <Table.Column title={locale['label.mobile']} dataIndex="celNo" width="130px" />
          <Table.Column title={locale['label.auth']} dataIndex="authId" width="130px" render={(val) => (this.method.getAuthStr(val))} />
          {this.method.renderButton(
            <Table.Column title="Operations" className="operations" width="100px" render={(val) =>
              (<Button onClick={e => this.method.clickEdit(val.id)} icon="edit" />)} />
          )}
        </Table>
      </div>

    );
  }
}

const WrappedUserSearch = Form.create({ name: 'user_search' })(UserSearch)
export default UserList
