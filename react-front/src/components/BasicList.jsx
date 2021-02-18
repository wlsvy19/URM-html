import React from 'react'
import { message, Modal } from 'antd'
import * as urmsc from '@/urm-utils'

const LIST_STATE = urmsc.LIST_STATE
const locale = urmsc.locale

class BasicSearch extends React.Component {
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
    clickAdd: e => {
      this.props.edit()
    },
    
    clickDelete: e => {
      this.props.delete('selected')
    },
    
    renderButton: (render) => {
      return !this.props.onlySearch && render
    },
  }
}

class BasicList extends React.Component {
  state = {
    items: [],
    selectedKeys: [],
  }

  method = {
    clickEdit: (id) => {
      this.props.edit(id)
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
            url: 'api/' + _this.props.path + '/delete',
            data: JSON.stringify(ids),
            contentType: 'application/json; charset=UTF-8',
            success: function(res) {
              if (res.code === 0) {
                message.success('delete ' + _this.props.path.toUpperCase() + ' [ ' + res.obj + ' ]')
                _this.method._updateItems(LIST_STATE.DELETE, ids)
              } else if (res.code === 1) {
                message.warning('삭제 실패하였습니다. - ' + res.message + ' \n 영향도를 확인하여 주세요.')
              } else {
                message.warning(res.message)
              } 
            },
          })
        }
      })
    },

    search: (param) => {
      let _this = this
      _this.setState({items: []})
      urmsc.ajax({
        type: 'GET',
        url: 'api/' + this.props.path,
        data: param,
        success: function(res) {
          _this.setState({items: res, selectedKeys: []})
        },
      })
    },

    renderButton: (render) => {
      return !this.props.onlySearch && render
    },
    
    _updateItems: (type, data) => {
      let tmp = [...this.state.items]
      
      if (type === LIST_STATE.DELETE) {
        for (let i = tmp.length-1; i >= 0; i--) {
          let it = tmp[i]
          if (data.indexOf(it.id) > -1) {
            tmp.splice(i, 1)
          }
        }
      } else if (type === LIST_STATE.UPDATE) {
        let update = false
        for (let i = 0; i < tmp.length; i++) {
          if (tmp[i].id === data.id) {
            tmp[i] = data
            update = true
            break
          }
        }
        if (!update) tmp.unshift(data)
      }
      
      this.setState({items: tmp, selectedKeys: []})
    },

    isPageDelete: () => {
      let auth = this.props.userInfo ? this.props.userInfo.auth : ''
      return urmsc.isPageEdit(this.props.path, 'delete', auth)
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
    selectedKeys: this.state.seletedKeys
  }

  onRow = (record, index) => {
    return {
      onDoubleClick: e => { if (this.props.onDbClick) this.props.onDbClick(record) }
     }
  }
}

BasicList.defaultProps = {
  onlySearch: false
}

export default BasicList
export {BasicSearch}