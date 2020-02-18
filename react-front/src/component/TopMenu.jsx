import React from 'react';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';

import logo from '../assets/logo.gif'

class TopMenu extends React.Component {
  constructor(props) {
    super(props)
    
    let pathname = window.location.pathname
    let pathArr = pathname.split('/')
    let last = pathArr[pathArr.length - 1]
    
    this.state = {
      current: last
    }
  }

  handleClick = e => {
    this.setState({
      current: e.key,
    })
  }

  render() {
    return (
      <div className="urm-menu">
        <div className="urm-logo">
          <img src={logo} alt="logo" height="29" />
          <span>EAI 요건 관리 시스템</span>
        </div>
        <Menu onClick={this.handleClick} selectedKeys={[this.state.current]} mode="horizontal">
          <Menu.Item key="request"><Link to="request">Request</Link></Menu.Item>
          <Menu.Item key="data"><Link to="data">Data</Link></Menu.Item>
          <Menu.Item key="system"><Link to="system">System</Link></Menu.Item>
          <Menu.SubMenu title={<span>Manage</span>}>
            <Menu.Item key="user"><Link to="user">User</Link></Menu.Item>
            <Menu.Item key="biz"><Link to="biz">Business Code</Link></Menu.Item>
          </Menu.SubMenu>
          <Menu.SubMenu title={<span>Result</span>}>
            <Menu.Item key="log"><Link to="/URM">Process Log</Link></Menu.Item>
            <Menu.Item key="stat"><Link to="/URM">Statics</Link></Menu.Item>
          </Menu.SubMenu>
          <Menu.SubMenu title={<span>SubMenu</span>}>
            <Menu.ItemGroup title="group1">
              <Menu.Item key="group1"><Link to="/URM">URM</Link></Menu.Item>
            </Menu.ItemGroup>
            <Menu.ItemGroup title="group2">
              <Menu.Item key="group2"><Link to="/URM">URM</Link></Menu.Item>
            </Menu.ItemGroup>
          </Menu.SubMenu>
        </Menu>
      </div>
    );
  }
}

export default TopMenu;