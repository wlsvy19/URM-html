import React, { useState } from 'react'
import { Table, Button } from 'antd'
import { Form, Input } from 'antd'

import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class BizSearch extends React.Component {
  componentDidMount() {
    this.props.search()
    
    let $el = document.querySelectorAll('.search-bar input.ant-input')
    let $this = this
    $el.forEach((it) => {
      it.addEventListener('keydown', function(e) {
        if (e && e.keyCode === 13) {
          $this.props.search($this.props.form.getFieldsValue())
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
            <Form.Item label={locale['label.bizNm']}>{getFieldDecorator("name")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level1.code']}>{getFieldDecorator("part1Id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level1.name']}>{getFieldDecorator("part1Name")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level2.code']}>{getFieldDecorator("part2Id")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item label={locale['label.biz.level2.name']}>{getFieldDecorator("part2Name")(<Input size="small" className="search-id" />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} title={locale['label.search']} />
              {this.method.renderButton(
                <Button icon="plus" onClick={this.method.clickAdd} title={locale['label.add']} />
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
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/code/business',
        data: param,
        success: function(list) {
          $this.setState({items: list})
        },
      })
    },

    updateList: (list) => {
      this.setState({items: list})
    },
  }

  render() {
    return (
      <div className="urm-list">
        <WrappedBizSearch {...this.props} search={this.method.search} add={this.method.handleAdd} />
        <EditableTable {...this.props} items={this.state.items} update={this.method.updateList} />
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
  const [ colWidth, setColWidth ] = useState(65)
  
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
        console.log('Level2 ID 를 입력하세요.')
        return false
      }
      if (!biz.part2Name || biz.par21Name.trim().length === 0) {
        console.log('Level2 명 을 입력하세요.')
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
        }
      })
    },
    
    clickCancel: (id) => {
      setEditingKey('')
    },

    renderButton: (render) => {
      return !props.onlySearch && render
    },

    renderOperations: (val, record, idx) => {
      let render = undefined
      if (method.isEditing(record)) {
        setColWidth(85)
        render = <div>
          <Button icon="save" onClick={e => { method.clickSave(idx) }} title={locale['label.save']} />
          <Button icon="close" onClick={method.clickCancel} title={locale['label.cancel']} />
        </div>
      } else {
        render = <Button icon="edit" onClick={e => { method.clickEdit(record.id) }} title={locale['label.modify']} />
      }
      return render
    },
  }
  
  return (
    <Table className="table-striped" rowClassName="editable-row"
      dataSource={props.items} pagination={false} bordered
      size="small" scroll={{ y: 500 }} rowKey="id"
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
        <Table.Column className="operations" width={colWidth}
          render={(val, record, idx) => (method.renderOperations(val, record, idx))} />
      )}
    </Table>
  );
})
const WrappedBizSearch = Form.create({name:'biz_search'})(BizSearch)
export default BizList