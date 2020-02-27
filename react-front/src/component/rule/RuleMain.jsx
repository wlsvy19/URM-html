import React from 'react'
import {default as urmUtils} from '../../urm-utils'

class RuleMain extends React.Component {
  componentDidMount() {
    console.log('mounted')
  }

  method = {
    setCode: (list) => {
      let $this = this
      urmUtils.ajax({
        type: 'GET',
        url:  '/URM/code/common',
        success: function(res) {
          res.forEach(function(it) {
            list.push(it);
          })
          $this.refs.searchBar.setState({})
        }
      })
    },
    
    search: (param) => {
      //page === undefined && (page = 1)
      //console.log('page : ', page)

      let paramStr = ''
      for (let key in param) {
        let value = param[key]
        if (value !== undefined && value.length > 0) {
          paramStr += '&' + key + '=' + value
        }
      }
    
      let $this = this
      urmUtils.ajax({
        type: 'GET',
        url: '/URM/' + this.state.path + '?' + paramStr.substring(1),
        success: function(list) {
          let $list = $this.refs.list
          $list.setState({items: list})
        }
      })
    },
  
    handleAdd: () => {
      this.refs.editor.setState({visible: true, item: {}})
    },

    handleEdit: (id) => {
      this.method._get(id)
    },
    
    _get: (id) => {
      let $this = this
      urmUtils.ajax({
        type: 'GET',
        url: '/URM/' + this.state.path + '/' + id,
        success: function(obj) {
          let $editor = $this.refs.editor
          console.log(obj)
          $editor.setState({visible: true, item: obj})
        }
      })
    },
  }
}

export default RuleMain