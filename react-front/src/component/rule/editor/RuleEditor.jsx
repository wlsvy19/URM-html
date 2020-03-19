import React from 'react'

export default class RequestEditor extends React.Component {
  state = {
    visible: false,
    item: {},
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.visible !== nextState.visible) {
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
        console.log('please input name.')
        return false
      }
      if (this.customMethod && !this.customMethod.validator(data)) {
        return false
      }
      return true
    },
  }
  
  childMethod = {
    save: (saveItem) => {
      console.log(saveItem)
      if (!this.method.validator(saveItem)) {
        return false
      }
      
      //console.log('save')
      //return false
      
      this.props.save(saveItem)
    },
    
    setItem: (form, item, customSetter) => {
      if (item.id) {
        let fields = form.getFieldsValue()
        let formItem = {}
        for (let key in fields) {
          formItem[key] = item[key]
        }
        if (customSetter) customSetter(formItem, item)
        form.setFieldsValue(formItem)
      } else {
        form.resetFields()
      }
    },
    
    makeRuleObj: (form, item) => {
      let obj = {}
      let data = form.getFieldsValue()
      
      let keys = Object.keys(item)
      let dataKeys = Object.keys(data)
      if (dataKeys > keys) {
        dataKeys.forEach((it) => {
          if (keys.indexOf(it) === -1) keys.push(it)
        })
      }
      
      keys.forEach((key) => {
        if (item[key] === null || typeof item[key] !== 'object') {
          obj[key] = (key in data) ? data[key] : item[key]
        }
      })
      return obj
    }
  }
}
