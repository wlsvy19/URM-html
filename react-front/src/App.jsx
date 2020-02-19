import React from 'react'
import { Route } from 'react-router-dom'
import { KeepAlive } from 'react-keep-alive'

import * as Comp from './router'
import TopMenu from './component/TopMenu'

class App extends React.Component {
  render() {
    const RouteComp = (props) => {
      return (
        <KeepAlive name={props.component.name}>
          <props.component />
        </KeepAlive>
      );
    }
    
    return (
      <div>
        <TopMenu />
        <Route exact path="/URM" render={props => <RouteComp component={Comp.Main} />} />
        <Route path="/URM/request" render={props => <RouteComp component={Comp.Request} />} />
        <Route path="/URM/data" render={props => <RouteComp component={Comp.Data} />} />
        <Route path="/URM/system" render={props => <RouteComp component={Comp.System} />} />
      </div>
    );
  }
}

export default App