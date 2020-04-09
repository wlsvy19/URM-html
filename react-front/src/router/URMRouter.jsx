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
        <Route path="/manage/biz" render={props => (
          <KeepAlive key="biz"><Router.CodeContainer component={Router.BizCode} /></KeepAlive>
        )} />
        
        {/* statics */}
        <Route path="/stat/process1" render={props => (
          <KeepAlive key="process1"><Router.CodeContainer component={Router.RequestProcessDay} /></KeepAlive>
        )} />
        <Route path="/stat/process2" render={props => (
          <KeepAlive key="process2"><Router.CodeContainer component={Router.RequestProcessMonth} /></KeepAlive>
        )} />
        <Route path="/stat/change1" render={props => (
          <KeepAlive key="change1"><Router.CodeContainer component={Router.RequestChangeDay} /></KeepAlive>
        )} />
        <Route path="/stat/change2" render={props => (
          <KeepAlive key="change2"><Router.CodeContainer component={Router.RequestChangeMonth} /></KeepAlive>
        )} />
        
        <Router.AuthContainer />
      </div>
    );
  }
}
