import React from 'react'
import * as urmsc from '../../../urm-utils'

class RequestEditor extends React.Component {
  state = {
    isNew: false,
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
        return false
      }
      if (this.customMethod && this.customMethod.validator(data)) {
        return false
      }
      return true
    },
  }
  
  childMethod = {
    save: (form, children, customSetter) => {
      let data = form.getFieldsValue()
      let saveItem = {}

      if (customSetter) customSetter(saveItem, data, children)
      if (!this.method.validator(data)) {
        return false
      }
      
      urmsc.ajax({
        type: 'POST',
        url: '/URM/' + this.props.path,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(res) {
          console.log(this.response)
        }
      })
    },
    
    setItem: (form, item, customSetter) => {
      console.log('this')
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
  }
}

export default RequestEditor