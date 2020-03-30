import React from 'react'
import { connect } from 'react-redux'

import { login, logout } from '@/store/modules/auth'
import * as urmsc from '@/urm-utils'

class AuthContainer extends React.Component {
  componentDidMount() {
    this.method.checkUser()
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.props.logged !== nextProps.logged) {
      return true
    }
    return false
  }

  method = {
    checkUser: () => {
      let { userInfo, login, logout } = this.props
      let user = JSON.parse(sessionStorage.getItem('URMUser'))
      let userId = !user ? '' : user.id
      
      if (userId && userId.length > 0) {
        if (userInfo.id.trim().length > 0 && userId !== userInfo.id) {
          logout()
        } else {
          login(user)
          return false
        }
      } else if (userInfo.id.trim().length > 0) {
        userId = userInfo.id
      }
      login({id: 'eai', name: 'eai', authId: '0'})
      /*urmsc.ajax({
        type: 'POST',
        url: '/URM/login/process',
        data: JSON.stringify({id: userId}),
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          if (res.code === 0) {
            sessionStorage.setItem('URMUser', JSON.stringify(res.obj));
            login(res.obj)
          } else {
            console.log(res.message)
            window.location = '/URM/' + res.obj
          }
        }
      })*/
    }
  }

  render() {
    return (
      <div></div>
    );
  }
}

const mapStateToProps = ({ auth }) => ({
  logged: auth.logged,
  userInfo: auth.userInfo,
})

const mapDispatchToProps = dispatch => ({
  login: (user) => dispatch(login(user)),
  logout: () => dispatch(logout()),
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AuthContainer)