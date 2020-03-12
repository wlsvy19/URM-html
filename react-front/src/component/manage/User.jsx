import React from 'react'
import { connect } from 'react-redux'

import * as urmsc from '../../urm-utils'
import UserList from './list/User-list'
import UserEditor from './editor/User-editor'

import { initCode } from '../../store/modules/rule'

class User extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			path: 'user'
		}
		this.method.setCode()
	}

	componentDidMount() {
		console.log('mounted')
	  }

	method = {
		setCode: () => {
		  if (this.props.commonCode === undefined) {
			let $this = this
			urmsc.ajax({
			  type: 'GET',
			  url:  '/URM/code/user',
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
			console.log('path: '+this.state.path)
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
	  
	

	render() {
		return (
				<div className="urm-panel" >
					<UserList ref="list" path={this.state.path} codeList={this.getCode()} edit={this.method.handleEdit} />
					<UserEditor ref="editor" path={this.state.path} codeList={this.getCode()} />
			</div>
		);
	}
}


const mapStateToProps = ({ rule }) => ({
	commonCode: rule.commonCode,
  })
  
  const mapDispatchToProps = dispatch => ({
	initCode: (code) => dispatch(initCode(code)),
  })

export default connect(
	mapStateToProps,
	mapDispatchToProps
  )(User)

