import React from 'react'
import { Table, Button, Input, Form } from 'antd'

import BasicList, { BasicSearch } from '@/components/BasicList'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class UserSearch extends BasicSearch {
  componentDidMount() {
    this.props.search()
    
    let $el = document.querySelectorAll('.search-bar.' + this.props.path + ' input.ant-input')
    let _this = this
    $el.forEach((it) => {
      it.addEventListener('keydown', function(e) {
        if (e && e.keyCode === 13) {
          _this.props.search(_this.props.form.getFieldsValue())
        }
      })
    })
  }

  render() {
    const { getFieldDecorator } = this.props.form
    return (
      <div className={'search-bar ' + this.props.path}>
        <Form colon={false}>
          <div className="row">
            <Form.Item label={locale['label.id']}>{getFieldDecorator("id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.name']} >{getFieldDecorator("name")(<Input size="small" className="search-name" />)}</Form.Item>
            <Form.Item label={locale['label.dept']}>{getFieldDecorator("dept")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item className="search-buttons row">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
              {this.method.renderButton(
                <div className="inline">
                  <Button size="small" onClick={this.method.clickAdd}>{locale['label.add']}</Button>
                  <Button size="small" type="danger" onClick={this.method.clickDelete} disabled={!this.props.canDelete()}>{locale['label.delete']}</Button>
                </div>
              )}
            </Form.Item>
          </div>
        </Form>
      </div>
    )
  }
}

class UserList extends BasicList {
  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    if (this.props.authList && this.props.authList.length !== nextProps.authList.length) {
      return true
    }
    return false
  }

  method = {
    ...this.method,
    
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
  }

  render() {
    return (
      <div className="urm-list">
        <WrappedUserSearch {...this.props} search={this.method.search} delete={this.method.clickDelete} canDelete={this.method.isPageDelete} />
        <Table className="table-striped" dataSource={this.state.items} pagination={false}
            bordered size="small" scroll={{ x: 1200, /*y: 500*/ }} rowKey="id"
            rowSelection={this.rowSelection} onRow={this.onRow}>
          <Table.Column title={locale['label.id']} dataIndex="id" width="145px" />
          <Table.Column title={locale['label.name']} dataIndex="name" ellipsis />
          <Table.Column title={locale['label.dept']} dataIndex="deptName" />
          <Table.Column title={locale['label.position']} dataIndex="positionName" />
          <Table.Column title={locale['label.grade']} dataIndex="gradeName" />
          <Table.Column title={locale['label.generalTel']} dataIndex="generalTelNo" />
          <Table.Column title={locale['label.officeTel']} dataIndex="officeTelNo" />
          <Table.Column title={locale['label.mobile']} dataIndex="celNo" />
          <Table.Column title={locale['label.auth']} dataIndex="authId" width="155px" render={(val) => (this.method.getAuthStr(val))} />
          {this.method.renderButton(
            <Table.Column className="operations" width="85px" render={(val) =>
              (<div>
                <Button onClick={e => this.method.clickEdit(val.id)} icon="edit" />
                <Button icon="delete" type="danger" onClick={e => { this.method.clickDelete([val.id]) }} title={locale['label.delete']} disabled={!this.method.isPageDelete()} />
              </div>)}
            />
          )}
        </Table>
      </div>
    );
  }
}

const WrappedUserSearch = Form.create({ name: 'user_search' })(UserSearch)
export default UserList
