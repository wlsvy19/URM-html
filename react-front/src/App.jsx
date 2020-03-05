import React from 'react'
import { Route } from 'react-router-dom'
import { KeepAlive } from 'react-keep-alive'

import * as Comp from './router'
import TopMenu from './component/TopMenu'

class App extends React.Component {
  render() {
    const RouteComp = (props) => {
      let componentKey = () => {
        let name = props.component.name
        if (!name) name = props.component.WrappedComponent.name
        return name
      }
      
      return (
        <KeepAlive name={componentKey()}>
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
        <Route path="/URM/user" render={props => <RouteComp component={Comp.User} />} />
      </div>
    );
  }
}

export default App