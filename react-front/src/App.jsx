import React from 'react'
import { Provider } from 'react-keep-alive'

import Router from './router/URMRouter'
import TopMenu from './component/TopMenu'

export default class App extends React.Component {
  render() {
    return (
      <div>
        <TopMenu />

        <Provider>
          <Router />
        </Provider>
      </div>
    );
  }
}
