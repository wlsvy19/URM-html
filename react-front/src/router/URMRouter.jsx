import React from 'react'
import { Route } from 'react-router-dom'
import { KeepAlive } from 'react-keep-alive'

import * as Router from './'

export default class URMRouter extends React.Component {
  render() {
    return (
      <div>
        <Route exact path="/" render={props => (
          <KeepAlive key="main"><Router.Main /></KeepAlive>
        )} />
        
        {/* rule */}
        <Route path="/request" render={props => (
         <KeepAlive key="request"><Router.CodeContainer component={Router.Request} /></KeepAlive>
        )} />
        <Route path="/data" render={props => (
          <KeepAlive key="data"><Router.CodeContainer component={Router.Data} /></KeepAlive>
        )} />
        <Route path="/system" render={props => (
          <KeepAlive key="system"><Router.CodeContainer component={Router.System} /></KeepAlive>
        )} />
        
        {/* manage */}
        <Route path="/user" render={props => (
          <KeepAlive key="user"><Router.CodeContainer component={Router.User} /></KeepAlive>
        )} />
        <Route path="/biz" render={props => (
          <KeepAlive key="biz"><Router.BusinessCode /></KeepAlive>
        )} />
        
        <Router.AuthContainer />
      </div>
    );
  }
}
