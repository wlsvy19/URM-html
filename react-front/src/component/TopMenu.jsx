import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux'
import { Menu, Button } from 'antd';

import { initCode, initAuth } from '../store/modules/code'
import { logout } from '../store/modules/auth'
import * as urmsc from '../urm-utils'

const urmProps = require('urmProperties')

class TopMenu extends React.Component {
  constructor(props) {
    super(props)
    
    let pathname = window.location.pathname
    let pathArr = pathname.split('/')
    let last = pathArr[pathArr.length - 1]
    
    this.state = {
      current: last
    }
    this.method.setCode()
  }

  method = {
    handleClick: e => {
      this.setState({ current: e.key, })
    },
    
    clickMain: e => {
      this.setState({ current: 'main' })
    },
    
    setCode: () => {
      /* set common */
      if (this.props.commonCode === undefined) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url:  'api/code/common',
          success: function(res) {
            $this.props.initCode(res)
          },
        })
      }
      
      /* set auth */
      if (this.props.authCode === undefined) {
        let $this = this
        urmsc.ajax({
          type: 'GET',
          url:  'api/code/auth',
          success: function(res) {
            $this.props.initAuth(res)
          },
        })
      }
    },
      
    logout: e => {
      logout()
      /*urmsc.ajax({
        type: 'GET',
        url: '/URM/logout',
        dataType: 'text',
        success: function(res) {
          sessionStorage.removeItem('URMUser')
          logout()
        }
      })*/
    },
  }

  render() {
    return (
      <div className="urm-menu">
        <div className="urm-logo">
          <Link to="/" onClick={this.method.clickMain}>
            <img src="logo.gif" alt="logo" height="29" />
            <span>EAI 요건 관리 시스템</span>
          </Link>
        </div>
        
        <Menu onClick={this.method.handleClick} selectedKeys={[this.state.current]} mode="horizontal">
          <Menu.Item key="request"><Link to="/request">{urmProps['page.request.name']}</Link></Menu.Item>
          <Menu.Item key="data"><Link to="/data">{urmProps['page.data.name']}</Link></Menu.Item>
          <Menu.Item key="system"><Link to="/system">{urmProps['page.system.name']}</Link></Menu.Item>
          <Menu.SubMenu title={<span>관리자</span>}>
            <Menu.Item key="user"><Link to="/user">{urmProps['page.user.name']}</Link></Menu.Item>
            <Menu.Item key="biz"><Link to="/biz">{urmProps['page.biz.name']}</Link></Menu.Item>
          </Menu.SubMenu>
          {/*<Menu.SubMenu title={<span>Result</span>}>
            <Menu.Item key="log"><Link to="/">Process Log</Link></Menu.Item>
            <Menu.Item key="stat"><Link to="/">Statics</Link></Menu.Item>
          </Menu.SubMenu>*/}
        </Menu>
        
        <div className="urm-user">
          <span>User : </span><div className="user-name">{this.props.userInfo.name}</div>
          <Button icon="logout" onClick={this.method.logout} />
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ code, auth }) => ({
  commonCode: code.commonCode,
  authCode: code.authCode,
  logged: auth.logged,
  userInfo: auth.userInfo,
})

const mapDispatchToProps = dispatch => ({
  logout: () => dispatch(logout()),
  initCode: (code) => dispatch(initCode(code)),
  initAuth: (code) => dispatch(initAuth(code)),
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TopMenu)