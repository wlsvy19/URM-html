import React from 'react'
import RuleMain from '../rule/RuleMain'
import UserList, { WrappedUserSearch, WrappedUserAdd } from './list/User-list'

import './user.css'
const CODE = []

class User extends RuleMain {
	constructor(props) {
		super(props)
		this.state = {
			path: 'user',
		}
		this.method.setCode(CODE)
	}

	const 

	render() {
		return (
			<div className="top-panel">
				<div className="urm-panel" >
					<WrappedUserSearch ref="searchBar" codeList={CODE} search={this.method.search} />
					<UserList ref="list" codeList={CODE} edit={this.method.handleEdit} />
				</div >
			</div>
		); 
	}
}

export default User