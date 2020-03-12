import React from 'react'
import {Table, Button, Input, Form} from 'antd'
import UserListButton from './UserListButton'
import * as urmsc from '../../../urm-utils'

class UserSearch extends React.Component {
  state = {
    autoCompleteResult: [],
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
      if (!this.props.onlySearch) {
        return render
      }
      return undefined
    },
  }

  render() {
    const { getFieldDecorator } = this.props.form
    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="ID">{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label="Name" >{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label="Dept">{getFieldDecorator("dept")(<Input size="small" className="search-id" />)}</Form.Item>

            <Form.Item className="search-buttons row">
              <Button onClick={this.method.clickSearch} icon="search" />
              {this.method.renderButton(
                <div className="inline">
                  <Button onClick={this.method.clickAdd} icon="plus" />
                  <Button icon="delete" />
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
    getTypeStr: (kind, key) => {
      let obj = {}
      let list = this.props.authList
      for (let i = 0; i < list.length; i++) {
        let it = list[i]
        if (it.kind === kind && it.code === key) {
          obj = it
          break
        }
      }
      return obj.name
    },

    clickEdit: (id) => {
      console.log('User id: ' + id)
      this.props.edit(id)
    },

    clickDelete: (ids) => {
      console.log('delete')
    },

    search: (param) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/' + this.props.path,
        data: param,
        success: function (list) {
          $this.setState({ items: list })
        },

      })
    },

    renderButton: (render) => {
      if (!this.props.onlySearch) {
        return render
      }
      return undefined
    },
  }
  render() {
    return (

      <div className="urm-list">
        <WrappedUserSearch {...this.props} search={this.method.search} />
        <Table lassName="table-striped" dataSource={this.state.items} pagination={false} bordered size={"small"} scroll={{ y: 500 }} rowKey="id" className="table-striped">
          <Table.Column title="ID" dataIndex="id" width="130px" />
          <Table.Column title="Name" dataIndex="name" width="130px" />
          <Table.Column title="Dept" dataIndex="dept" width="130px" />
          <Table.Column title="Position" dataIndex="positionNm" width="130px" />
          <Table.Column title="Grade Num" dataIndex="gradeNm" width="130px" />
          <Table.Column title="General Tel" dataIndex="generalTelNo" width="130px" />
          <Table.Column title="Office Tel" dataIndex="officeTelNo" width="130px" />
          <Table.Column title="Mobile" dataIndex="celNo" width="130px" />
          <Table.Column title="Auth" dataIndex="authId" width="130px" />
          {this.method.renderButton(
            <Table.Column title="Operations" className="operations" width="100px" render={(val) =>
              (<UserListButton edit={e => { this.method.clickEdit(val.id) }} delete={e => { this.method.clickDelete([val.id]) }} />)} />
          )}
        </Table>
      </div>

    );
  }
}

const WrappedUserSearch = Form.create({ name: 'user_search' })(UserSearch)
export default UserList

