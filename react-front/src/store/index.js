import { combineReducers } from 'redux'
import code from './modules/code'
import auth from './modules/auth'

export default combineReducers({
  code,
  auth,
});