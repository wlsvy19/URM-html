import React from 'react'
import { message } from 'antd'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

export default class RequestEditor extends React.Component {
  state = {
    visible: false,
    item: {},
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.visible !== nextState.visible) {
      return true
    }
    if (this.state.item.chgDate !== nextState.item.chgDate) {
      return true
    }
    return false
  }

  method = {
    handleCancel: e => {
      this.setState({visible: false})
    },
    
    validator: (data) => {
      if (!data.name || data.name.trim().length === 0) {
        message.warning(locale['message.1003'])
        return false
      }
      if (this.customMethod && !this.customMethod.validator(data)) {
        return false
      }
      return true
    }
  }
  
  childMethod = {
    save: (saveItem) => {
      console.log(saveItem)
      if (!this.method.validator(saveItem)) {
        return false
      }
      
      this.props.save(saveItem)
    },
    
    setItem: (form, item, customSetter) => {
      form.resetFields()
      if (item.id) {
        let fields = form.getFieldsValue()
        let formItem = {}
        for (let key in fields) {
          if (typeof item[key] !== 'object') {
            formItem[key] = item[key]
          }
        }
        if (customSetter) customSetter(formItem, item)
        form.setFieldsValue(formItem)
      }
    },
    
    makeRuleObj: (form, item) => {
      let obj = {}
      let data = form.getFieldsValue()
      
      let keys = Object.keys(item)
      let dataKeys = Object.keys(data)
      if (dataKeys.length > keys.length) {
        dataKeys.forEach((it) => {
          if (keys.indexOf(it) === -1) keys.push(it)
        })
      }
      
      keys.forEach((key) => {
        if (!item[key] || typeof item[key] !== 'object') {
          obj[key] = (key in data) ? data[key] : item[key]
        }
      })
      return obj
    },
    
    isPageSave: () => {
      return urmsc.isPageEdit(this.props.path, 'save', this.props.userInfo.auth)
    }
  }
}
