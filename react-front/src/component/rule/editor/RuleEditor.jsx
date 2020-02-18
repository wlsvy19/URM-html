import React from 'react'
import {default as urmUtils} from '../../../urm-utils'

class RequestEditor extends React.Component {
  state = {
    isNew: false,
    visible: false,
    item: {},
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.visible !== nextState.visible) {
      return true
    } else {
      return false
    }
  }

  method = {
    clickSave: ($form, customSetter) => {
      let data = $form.props.form.getFieldsValue()
      let children = $form.state.children
      
      customSetter(data, children)
      urmUtils.ajax('POST', '/URM/' + this.state.path, JSON.stringify(data), true, function() {
        if(this.readyState === 4 && this.status === 200) {
          console.log(this.response)
        }
      })
      //this.props.save(data)
    },

    handleCancel: ($form) => {
      this.setState({visible: false})
    },
    
    setItem(form, item, customSetter) {
      if (item.id) {
        let fields = form.getFieldsValue()
        let setter = {}
        for (let key in fields) {
          setter[key] = item[key]
        }
        customSetter(setter, item)
        form.setFieldsValue(setter)
      } else {
        form.resetFields()
      }
    },
  }
}

export default RequestEditor