import React from 'react'
import * as urmsc from '../../urm-utils'

class RuleMain extends React.Component {
  componentDidMount() {
    console.log('mounted')
  }

  method = {
    setCode: () => {
      if (this.props.commonCode === undefined) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url:  '/URM/code/common',
          success: function(res) {
            $this.props.initCode(res)
          },
          error: function(res) {
            console.log(res, this.response)
          }
        })
      }
    },

    handleEdit: (id) => {
      if (id) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url: '/URM/' + this.state.path + '/' + id,
          success: function(obj) {
            let $editor = $this.refs.editor
            console.log(obj)
            $editor.setState({visible: true, item: obj})
          }
        })
      } else {
        this.refs.editor.setState({visible: true, item: {}})
      }
    },
  }

  getCode = () => {
    let codeList = this.props.commonCode
    return codeList ? codeList : []
  }
}

export default RuleMain