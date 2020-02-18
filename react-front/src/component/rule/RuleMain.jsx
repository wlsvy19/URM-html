import React from 'react'
import {default as urmUtils} from '../../urm-utils'

class RuleMain extends React.Component {
  componentDidMount() {
    console.log('mounted')
  }

  method = {
    setCode: (list) => {
      let $this = this
      urmUtils.ajax('GET', '/URM/code/common', '', true, function() {
        if(this.readyState === 4 && this.status === 200) {
          let res = JSON.parse(this.response)
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
      urmUtils.ajax('GET', '/URM/' + this.state.path + '?' + paramStr.substring(1), '', true, function() {
        let $list = $this.refs.list
        if(this.readyState === 4 && this.status === 200) {
          let list = JSON.parse(this.response)
          list.forEach(function (it, idx) {
            it.key = idx
          })

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
      urmUtils.ajax('GET', '/URM/' + this.state.path + '/' + id, '', true, function() {
        let $editor = $this.refs.editor
        if(this.readyState === 4 && this.status === 200) {
          let obj = JSON.parse(this.response)
          console.log(obj)
          $editor.setState({visible: true, item: obj})
        }
      })
    },
  }
}

export default RuleMain