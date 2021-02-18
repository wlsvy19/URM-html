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
        let initCode = this.props.initCode
        urmsc.ajax({
          type: 'GET',
          url:  '/URM/api/code/common',
          success: function(res) {
            initCode(res)
          },
        })
      }
      
      /* set auth */
      if (this.props.authCode === undefined) {
        let initAuth = this.props.initAuth
        urmsc.ajax({
          type: 'GET',
          url:  '/URM/api/code/auth',
          success: function(res) {
            initAuth(res)
          },
        })
      }
    },
      
    logout: e => {
      let logout = this.props.logout
      urmsc.ajax({
        type: 'GET',
        url: '/URM/logout',
        dataType: 'text',
        beforeSend: function () {
          sessionStorage.removeItem('URMUser')
          logout()
        },
      })
    },
  }
  
  disabled = (page) => {
    let viewAuth = urmProps['page.' + page + '.view']
    let authId = this.props.userInfo.auth
    return !(this.props.logged && viewAuth.split(',').indexOf(authId) > -1)
  }

  render() {
    return (
      <div className="urm-menu">
        <div className="urm-logo">
          <Link to="/" onClick={this.method.clickMain}>
            <img src="/URM/logo.gif" alt="logo" />
            <span>EAI 요건 관리 시스템</span>
          </Link>
        </div>
        
        <Menu onClick={this.method.handleClick} selectedKeys={[this.state.current]} mode="horizontal">
          <Menu.Item key="request" disabled={this.disabled('request')}><Link to="/request">{urmProps['page.request.name']}</Link></Menu.Item>
          <Menu.Item key="data" disabled={this.disabled('data')}><Link to="/data">{urmProps['page.data.name']}</Link></Menu.Item>
          <Menu.Item key="system" disabled={this.disabled('system')}><Link to="/system">{urmProps['page.system.name']}</Link></Menu.Item>
          <Menu.SubMenu title={<span>{urmProps['page.manage.name']}</span>}>
            <Menu.Item key="user" disabled={this.disabled('user')}><Link to="/user">{urmProps['page.user.name']}</Link></Menu.Item>
            <Menu.Item key="biz" disabled={this.disabled('manage')}><Link to="/manage/biz">업무 코드</Link></Menu.Item>
          </Menu.SubMenu>
          <Menu.SubMenu title={urmProps['page.stat.name']} disabled={this.disabled('stat')}>
            <Menu.ItemGroup title="요건진행상태">
              <Menu.Item key="process1"><Link to="/stat/process1">일별 진행상태</Link></Menu.Item>
              <Menu.Item key="process2"><Link to="/stat/process2">월별 진행상태</Link></Menu.Item>
            </Menu.ItemGroup>
            <Menu.ItemGroup title="요건변경상태">
              <Menu.Item key="change1"><Link to="/stat/change1">일별 변경상태</Link></Menu.Item>
              <Menu.Item key="change2"><Link to="/stat/change2">월별 변경상태</Link></Menu.Item>
            </Menu.ItemGroup>
          </Menu.SubMenu>
          <Menu.SubMenu title={urmProps['page.process.name']} disabled={this.disabled('process')}>
            <Menu.ItemGroup title="거래처리로그">
              <Menu.SubMenu key="ldev" title="개발">
                <Menu.Item key="dlog1"><Link to="/process/dlog1">REALTIME</Link></Menu.Item>
                <Menu.Item key="dlog2"><Link to="/process/dlog2">BATCH</Link></Menu.Item>
                <Menu.Item key="dlog3"><Link to="/process/dlog3">DEFERRED</Link></Menu.Item>
              </Menu.SubMenu>
              <Menu.SubMenu key="ltest" title="테스트">
                <Menu.Item key="tlog1"><Link to="/process/tlog1">REALTIME</Link></Menu.Item>
                <Menu.Item key="tlog2"><Link to="/process/tlog2">BATCH</Link></Menu.Item>
                <Menu.Item key="tlog3"><Link to="/process/tlog3">DEFERRED</Link></Menu.Item>
              </Menu.SubMenu>
              <Menu.SubMenu key="lprod" title="운영">
                <Menu.Item key="plog1"><Link to="/process/plog1">REALTIME</Link></Menu.Item>
                <Menu.Item key="plog2"><Link to="/process/plog2">BATCH</Link></Menu.Item>
                <Menu.Item key="plog3"><Link to="/process/plog3">DEFERRED</Link></Menu.Item>
              </Menu.SubMenu>
            </Menu.ItemGroup>
            <Menu.ItemGroup title="거래처리통계">
              <Menu.SubMenu key="sdev" title="개발">
                <Menu.Item key="dstat1"><Link to="/process/dstat1">시간별 REALTIME</Link></Menu.Item>
                <Menu.Item key="dstat2"><Link to="/process/dstat2">시간별 BATCH</Link></Menu.Item>
                <Menu.Item key="dstat3"><Link to="/process/dstat3">시간별 DEFERRED</Link></Menu.Item>
                <Menu.Item key="dstat4"><Link to="/process/dstat4">일자별 REALTIME</Link></Menu.Item>
                <Menu.Item key="dstat5"><Link to="/process/dstat5">일자별 BATCH</Link></Menu.Item>
                <Menu.Item key="dstat6"><Link to="/process/dstat6">일자별 DEFERRED</Link></Menu.Item>
              </Menu.SubMenu>
              <Menu.SubMenu key="stest" title="테스트">
                <Menu.Item key="tstat1"><Link to="/process/tstat1">시간별 REALTIME</Link></Menu.Item>
                <Menu.Item key="tstat2"><Link to="/process/tstat2">시간별 BATCH</Link></Menu.Item>
                <Menu.Item key="tstat3"><Link to="/process/tstat3">시간별 DEFERRED</Link></Menu.Item>
                <Menu.Item key="tstat4"><Link to="/process/tstat4">일자별 REALTIME</Link></Menu.Item>
                <Menu.Item key="tstat5"><Link to="/process/tstat5">일자별 BATCH</Link></Menu.Item>
                <Menu.Item key="tstat6"><Link to="/process/tstat6">일자별 DEFERRED</Link></Menu.Item>
              </Menu.SubMenu>
              <Menu.SubMenu key="sprod" title="운영">
                <Menu.Item key="pstat1"><Link to="/process/pstat1">시간별 REALTIME</Link></Menu.Item>
                <Menu.Item key="pstat2"><Link to="/process/pstat2">시간별 BATCH</Link></Menu.Item>
                <Menu.Item key="pstat3"><Link to="/process/pstat3">시간별 DEFERRED</Link></Menu.Item>
                <Menu.Item key="pstat4"><Link to="/process/pstat4">일자별 REALTIME</Link></Menu.Item>
                <Menu.Item key="pstat5"><Link to="/process/pstat5">일자별 BATCH</Link></Menu.Item>
                <Menu.Item key="pstat6"><Link to="/process/pstat6">일자별 DEFERRED</Link></Menu.Item>
              </Menu.SubMenu>
            </Menu.ItemGroup>
          </Menu.SubMenu>
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