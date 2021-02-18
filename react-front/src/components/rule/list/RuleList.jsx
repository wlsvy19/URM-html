import BasicList, { BasicSearch } from '@/components/BasicList'
import * as urmsc from '@/urm-utils'

class RuleSearch extends BasicSearch {
  componentDidMount() {
    this.props.form.setFieldsValue(this.props.scparam)
    this.props.search(this.props.form.getFieldsValue())
    
    let $el = document.querySelectorAll('.search-bar.'+ this.props.path + ' input.ant-input')
    let _this = this
    $el.forEach((it) => {
      it.addEventListener('keydown', function(e) {
        if (e && e.keyCode === 13) {
          _this.props.search(_this.props.form.getFieldsValue())
        }
      })
    })
  }

  componentDidUpdate(prevProps, prevState) {
    if (JSON.stringify(this.props.scparam) !== JSON.stringify(prevProps.scparam)) {
      this.props.form.setFieldsValue(this.props.scparam)
    }
  }
}

class RuleList extends BasicList {
  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.items !== nextState.items) {
      return true
    }
    if (this.props.codeList && this.props.codeList.length !== nextProps.codeList.length) {
      return true
    }
    if (JSON.stringify(this.props.scparam) !== JSON.stringify(nextProps.scparam)) {
      return true
    }
    return false
  }

  componentDidUpdate(prevProps, prevState) {
    if (JSON.stringify(this.props.scparam) !== JSON.stringify(prevProps.scparam)) {
      this.method.search(this.props.scparam)
    }
  }

  method = {
    ...this.method,
    
    getTypeStr: (key, val) => {
      let kind = urmsc.CODEKEY[key]
      let list = this.props.codeList
      let obj = {}
      
      for(let i=0; i < list.length; i++) {
        let it = list[i]
        if (it.kind === kind && it.code === val) {
          obj = it
          break
        }
      }
      return obj.name
    },
  }
}

RuleList.defaultProps = {
  onlySearch: false
}

export default RuleList
export {RuleSearch}