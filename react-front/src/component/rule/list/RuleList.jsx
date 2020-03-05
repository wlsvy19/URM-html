import React from 'react'
import * as urmsc from '../../../urm-utils'

class RuleSearch extends React.Component {
  componentDidMount() {
    this.props.search()
  }
  
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
    clickAdd: e => {
      this.props.edit()
    },
    
    renderButton: (render) => {
      if (!this.props.onlySearch) {
        return render
      }
      return undefined
    },
  }
}

class RuleList extends React.Component {
  state = {
    items: [],
    onDbClick: undefined,
  }

  method = {
    getTypeStr: (kind, key) => {
      let obj = {}
      let list = this.props.codeList
      for(let i=0; i < list.length; i++) {
        let it = list[i]
        if (it.kind === kind && it.code === key) {
          obj = it
          break
        }
      }
      return obj.name
    },

    clickEdit: (id) => {
      this.props.edit(id)
    },

    clickDelete: (ids) => {
      console.log('delete')
    },

    search: (param) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/' + this.props.path,
        data: param,
        success: function(list) {
          $this.setState({items: list})
        },
      })
    },

    renderButton: (render) => {
      if (!this.props.onlySearch) {
        return render
      }
      return undefined
    },
  }
}

RuleList.defaultProps = {
  onlySearch: false
}

export default RuleList
export {RuleSearch}