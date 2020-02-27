import React from 'react'

class RuleSearch extends React.Component {
  componentDidMount() {
    this.props.search()
  }
  
  method = {
    clickSearch: e => {
      this.props.search(this.props.form.getFieldsValue())
    },
    
    clickAdd: e => {
      this.props.add()
    },
  }

}

class RuleList extends React.Component {
  state = {
    items: [],
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
    
    handleResize: (e, { size }) => {
      this.setState(({ columns }) => {
        const nextColumns = [...columns]
        return { columns: nextColumns }
      })
    },
    
    renderButton: (render) => {
      if (this.props.operation) {
        return render
      }
      return undefined
    }
  }

}

RuleList.defaultProps = {
  operation: false
}

export default RuleList
export {RuleSearch}