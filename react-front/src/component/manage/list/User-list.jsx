import React from 'react'
import {
	Table, Button, Input, Select,Form,Modal} from 'antd'

import { default as urmUtils } from '../../../urm-utils'

const KINDS = urmUtils.codeKey

const { Option } = Select

class UserSearch extends React.Component {
	state = {
		confirmDirty: false, 
		autoCompleteResult: [], 
		vlsible: false, 
		confirmLoading: false, 
	}

	showModal = (e) => {
		this.setState({
			visible: true,
		})
	}

	handleCancel = e => {
		this.setState({
			visible: false,
		})
	}

	handleSubmit = (e) => { 
		e.preventDefault();
		this.props.form.validateFieldsAndScroll((err, values) => { 
			if (!err) { 
				this.setState({
					confirmLoading: true,
				});

				setTimeout(() => {
					this.setState({
						visible: false,
						confirmLoading: false,
					});
				}, 1000);
			}
		})
	}

	handleConfirmBlur = e => {
		const { value } = e.target;
		this.setState({ confirmDirty: this.state.confirmDirty || !!value })
	}

	compareToFirstPassword = (rule, value, callback) => {
		const { form } = this.props;
		if (value && value !== form.getFieldValue('password')) { 
			callback('')
		} else {
			callback()
		}
	}

	validateToNextPassword = (rule, value, callback) => {
		const { form } = this.props;
		if (value && this.state.confirmDirty) {
			form.validateFields(['confirm'], { force: true })
		}
		callback()
	}

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

		renderOpts: (key) => {
			return urmUtils.getSubListByKey(this.props.codeList, "kind", KINDS[key]).map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
		},
	}

	render() {
		const { getFieldDecorator } = this.props.form
		const { visible, confirmLoading } = this.state
		const authCode = getFieldDecorator('prefix', {
			initialValue: 'URM ADMIN',
		})(
			<Select style={{ width: 200 }}>
				<Option value="">USER ADMIN</Option>
				<Option value="">USER VIEWER</Option>
				<Option value="">SYSTEM ADMIN</Option>
				<Option value="">SYSTEM VIEWER</Option>
				<Option value="">INTERFACE ADMIN</Option>
				<Option value="">INTERFACE VIEWER</Option>
				<Option value="">USER</Option>
			</Select>
		)

		return (
			<div className="search-bar">
				<div className="row">
					<Form.Item label="Name" colon={false}>{getFieldDecorator("name")(<Input size="small" className="search-id" />)}</Form.Item>
					<Form.Item label="ID" colon={false}>{getFieldDecorator("id")(<Input size="small" className="search-name" />)}</Form.Item>
					<Form.Item label="Dept" colon={false}>{getFieldDecorator("dept")(<Input size="small" className="search-id" />)}</Form.Item>

					<Form.Item style={{ marginLeft: "15px" }}>
						<Button onClick={this.method.clickSearch} icon="search" />
						<Button onClick={this.showModal} icon="plus" />
					</Form.Item>

					<Modal
					style={{ top: 20 }}
					title=""
					visible={this.state.visible}
					confirmLoading={confirmLoading}
					onOk={this.handleSubmit}
					onCancel={this.handleCancel}
					okText="Save"
					cancelText="Cancel"
					destroyOnClose={true} 
				>

					<Form labelCol={{ span: 7 }} wrapperCol={{ span: 10 }} onSubmit={this.handleSubmit}>

						<Form.Item label="ID">
							{getFieldDecorator('modal-id', {
								rules: [{
									required: true,
									message: 'Check ID'
								},
								],
							})(<Input />)}
						</Form.Item>

						<Form.Item label="Name">
							{getFieldDecorator('modal-name', {
								rules: [{ required: true, message: 'Check Name' }],
							})(<Input />)}
						</Form.Item>

						<Form.Item label="Password" hasFeedback>
							{getFieldDecorator('modal-password', {
								rules: [
									{
										required: true,
										message: 'Check Password',
									},
									{
										validator: this.validateToNextPassword,
									},
								],
							})(<Input.Password style={{ width: 200 }} />)}
						</Form.Item>

						<Form.Item label="Confrim Password" hasFeedback>
							{getFieldDecorator('modal-confirm', {
								rules: [
									{
										required: true,
										message: 'Different Password',
									},
									{
										validator: this.compareToFirstPassword,
									},

								],
							})(<Input.Password onBlur={this.handleConfirmBlur} style={{ width: 200 }} />)}
						</Form.Item>

						<Form.Item label="Dept">
							<Input />
						</Form.Item>

						<Form.Item label="Grade">
							<Input />
						</Form.Item>

						<Form.Item label="Position">
							<Input />
						</Form.Item>

						<Form.Item label="General Tel">

							<Input />
						</Form.Item>

						<Form.Item label="Office Tel">

							<Input />
						</Form.Item>

						<Form.Item label="Mobile">

							<Input />
						</Form.Item>

						<Form.Item label="Auth Code" addonBefore={authCode}>
							{getFieldDecorator('modal-authId', {
								rules: [
									{
										required: true,
										message: 'Check Auth Code'
									}
								],
							})
								(	<Select style={{ width: 200 }}>
				<Option value="">USER ADMIN</Option>
				<Option value="">USER VIEWER</Option>
				<Option value="">SYSTEM ADMIN</Option>
				<Option value="">SYSTEM VIEWER</Option>
				<Option value="">INTERFACE ADMIN</Option>
				<Option value="">INTERFACE VIEWER</Option>
				<Option value="">USER</Option>
			</Select>)}
						</Form.Item>

					</Form>
				</Modal>

				</div>
			</div>
		)
	}
}

class UserList extends React.Component {
	state = {
		items: [],
	}

	method = {
		getTypeStr: (kind, key) => {
			let obj = {}
			let list = this.props.codeList
			for (let i = 0; i < list.length; i++) {
				let it = list[i]
				if (it.kind === kind && it.code === key) {
					obj = it
					break
				}
			}
			return obj.name
		},
		handleResize: (e, { size }) => {
			this.setState(({ columns }) => {
				const nextColumns = [...columns]
				return { columns: nextColumns }
			})
		}
	}

	render() {
		return (

			<div className="urm-list">
				<Table dataSource={this.state.items} pagination={false} bordered size={"small"} scroll={{ y: 500 }} rowKey="id" className="table-striped">
					<Table.Column title="ID" dataIndex="id" width="130px" />
					<Table.Column title="Name" dataIndex="name" width="130px" />
					<Table.Column title="Dept" dataIndex="dept" width="130px" />
					<Table.Column title="Position" dataIndex="positionNm" width="130px" />
					<Table.Column title="Grade Num" dataIndex="gradeNm" width="130px" />
					<Table.Column title="General Tel" dataIndex="generalTelNo" width="130px" />
					<Table.Column title="Office Tel" dataIndex="officeTelNo" width="130px" />
					<Table.Column title="Mobile" dataIndex="celNo" width="130px" />
					<Table.Column title="Auth" dataIndex="authId" width="130px" />
				</Table>
			</div>

		);
	}
}

const WrappedUserSearch = Form.create({ name: 'user_search' })(UserSearch)
// const WrappedUserAdd = Form.create({ name: 'user_add' })(UserAdd)
export default UserList
export { WrappedUserSearch }
// export { WrappedUserAdd }
