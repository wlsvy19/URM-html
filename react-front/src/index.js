import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'
import { Provider } from 'react-keep-alive'
//import { createStore } from 'redux'
//import { Provider } from 'react-redux'

import './index.css'
import 'antd/dist/antd.css'

import App from './App'
import * as serviceWorker from './serviceWorker'

//const store = createStore(reducer);

const Root = () => (
    <BrowserRouter>
      <Provider>
        <App/>
      </Provider>
    </BrowserRouter>
)

ReactDOM.render(<Root />, document.getElementById('root'))

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister()
