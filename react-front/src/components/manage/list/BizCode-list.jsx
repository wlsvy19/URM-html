import React, { useState } from 'react'
import { Table, Button, Modal, message } from 'antd'
import { Form, Input } from 'antd'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class BizSearch extends React.Component {
  componentDidMount() {
    this.props.search()
    
    let $el = document.querySelectorAll('.search-bar.biz input.ant-input')
    let _this = this
    $el.forEach((it) => {
      it.addEventListener('keydown', function(e) {
        if (e && e.keyCode === 13) {
          _this.props.search(_this.props.form.getFieldsValue())
        }
      })
    })
  }
  
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
    clickAdd: e => {
      this.props.add()
    },
    
    clickDelete: e => {
      this.props.delete('selected')
    },

    renderButton: (render) => {
      return !this.props.onlySearch && render
    },
  }

  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar biz">
        <Form colon={false}>
          <div className="row">
            <Form.Item label={locale['label.bizNm']}>{getFieldDecorator("name")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level1.code']}>{getFieldDecorator("part1Id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level1.name']}>{getFieldDecorator("part1Name")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level2.code']}>{getFieldDecorator("part2Id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level2.name']}>{getFieldDecorator("part2Name")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
              {this.method.renderButton(
                <div className="inline">
                  <Button size="small" onClick={this.method.clickAdd}>{locale['label.add']}</Button>
                  <Button size="small" type="danger" onClick={this.method.clickDelete}>{locale['label.delete']}</Button>
                </div>
              )}
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
    selectedKeys: [],
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    if (this.props.onDbClick !== nextProps.onDbClick) {
      return true
    }
    return false
  }

  method = {
    handleAdd: () => {
      let tmp = [...this.state.items]
      tmp.push({id: 'undefined-'+tmp.length})
      this.setState({items: tmp})
    },
    
    search: (param) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/code/business',
        data: param,
        success: function(list) {
          _this.setState({items: list, selectedKeys: []})
        },
      })
    },
    
    clickDelete: (ids) => {
      if (ids === 'selected') {
        ids = this.state.selectedKeys
      }
      if (!ids || ids.length === 0) {
        message.warning(locale['message.1004'])
        return false
      }
      let _this = this
      Modal.confirm({
        autoFocusButton: 'cancel',
        content: '정말 삭제 하시겠습니까?',
        cancelText: 'NO',
        okText: 'YES',
        onOk() {
          urmsc.ajax({
            type: 'POST',
            url: '/URM/api/code/business/delete',
            data: JSON.stringify(ids),
            contentType: 'application/json; charset=UTF-8',
            success: function(res) {
              if (res.code === 0) {
                message.success('delete BUSINESSCODE [ ' + res.obj + ' ]')
                _this.method.deleteList(ids)
              } else if (res.code === 1) {
                message.warning('삭제 실패하였습니다. - ' + res.message + ' \n 영향도를 확인하여 주세요.')
              }
            },
          })
        }
      })
    },
    
    updateList: (list) => {
      this.setState({items: list, selectedKeys: []})
    },
    
    deleteList: (ids) => {
      let tmp = [...this.state.items]
      for (let i = tmp.length-1; i >= 0; i--) {
        let it = tmp[i]
        if (ids.indexOf(it.id) > -1) {
          tmp.splice(i, 1)
        }
      }
      this.setState({items: tmp, selectedKeys: []})
    }
  }

  rowSelection = {
    columnWidth: 40,
    onSelect: (record, selected, selectedRows, nativeEvent) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it.id)
      })
      this.setState({selectedKeys: tmp})
    },
    onSelectAll: (selected, selectedRows, changeRows) => {
      let tmp = []
      selectedRows.forEach((it) => {
        tmp.push(it.id)
      })
      this.setState({selectedKeys: tmp})
    },
  }

  render() {
    return (
      <div className="urm-list">
        <WrappedBizSearch {...this.props} search={this.method.search} add={this.method.handleAdd} delete={this.method.clickDelete} />
        <EditableTable {...this.props} items={this.state.items} update={this.method.updateList}
          delete={this.method.clickDelete} rowSelection={this.rowSelection} />
      </div>
    );
  }
}

BizList.defaultProps = {
  onlySearch: false
}

const EditableTable = Form.create({name:'editable_table'})((props) => {
  const { getFieldDecorator } = props.form
  const [ editingKey, setEditingKey ] = useState('')
  
  let method = {
    isEditing: record => record.id === editingKey,
    
    editableCell: (val, record, dataIndex) => {
      return ( method.isEditing(record) ?
        <Form.Item>{getFieldDecorator(dataIndex, {initialValue: val})(<Input size="small" />)}</Form.Item> : val
      );
    },

    clickEdit: (id) => {
      setEditingKey(id)
    },
    
    clickSave: (idx) => {
      let items = props.form.getFieldValue('items')
      let biz = {id: editingKey}
      /* set item*/
      if (props.items.length > idx) {
        biz = props.items[idx]
      }
      biz = { ...biz, ...items[idx]}
      
      /* validate */
      if (biz.id.startsWith('undefined')) {
        biz.id = ''
      }
      if (!biz.part2Id || biz.part2Id.trim().length === 0) {
        message.warning('Level2 ID 를 입력하세요.')
        return false
      }
      if (!biz.part2Name || biz.part2Name.trim().length === 0) {
        message.warning('Level2 명 을 입력하세요.')
        return false
      }
      
      urmsc.ajax({
        type: 'POST',
        url: '/URM/api/code/business',
        data: JSON.stringify(biz),
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          props.update(res)
          setEditingKey('')
          message.success('저장 되었습니다.')
        },
        error: function(xhr) {
          message.warning('저장에 실패하였습니다. ' + xhr.statusText)
        }
      })
    },
    
    clickCancel: (id) => {
      setEditingKey('')
    },
    
    clickDelete: (val) => {
      props.delete(val)
    },

    renderButton: (render) => {
      return !props.onlySearch && render
    },

    renderOperations: (val, record, idx) => {
      return method.isEditing(record) ?
        <div>
          <Button icon="save" onClick={e => { method.clickSave(idx) }} title={locale['label.save']} />
          <Button icon="close" onClick={method.clickCancel} title={locale['label.cancel']} />
        </div> :
        <div>
          <Button icon="edit" onClick={e => { method.clickEdit(record.id) }} title={locale['label.modify']} />
          <Button icon="delete" type="danger" onClick={e => { method.clickDelete([record.id]) }} title={locale['label.delete']} />
        </div>
    },
  }
  
  return (
    <Table className="table-striped" rowClassName="editable-row"
      dataSource={props.items} pagination={false} bordered
      size="small" /*scroll={{ y: 500 }}*/ rowKey="id" rowSelection={props.rowSelection}
      onRow={(record, index) => {
        return {
          onDoubleClick: e => { if (props.onDbClick) props.onDbClick(record) }
        }
      }
    }>
      <Table.Column title={locale['label.bizId']} dataIndex="id" width="130px"/>
      <Table.Column title={locale['label.bizNm']} dataIndex="name" width="200px"
        render={(val, record, idx) => method.editableCell(val, record, 'items['+idx+'].name')}/>
      <Table.Column title={locale['label.biz.level1.code']} dataIndex="part1Id"
        render={(val, record, idx) => method.editableCell(val, record, 'items['+idx+'].part1Id')}/>
      <Table.Column title={locale['label.biz.level1.name']} dataIndex="part1Name"
        render={(val, record, idx) => method.editableCell(val, record, 'items['+idx+'].part1Name')}/>
      <Table.Column title={locale['label.biz.level2.code']} dataIndex="part2Id"
        render={(val, record, idx) => method.editableCell(val, record, 'items['+idx+'].part2Id')}/>
      <Table.Column title={locale['label.biz.level2.name']} dataIndex="part2Name"
        render={(val, record, idx) => method.editableCell(val, record, 'items['+idx+'].part2Name')}/>
      {method.renderButton(
        <Table.Column className="operations" width="85px"
          render={(val, record, idx) => (method.renderOperations(val, record, idx))} />
      )}
    </Table>
  );
})
const WrappedBizSearch = Form.create({name:'biz_search'})(BizSearch)
export default BizList