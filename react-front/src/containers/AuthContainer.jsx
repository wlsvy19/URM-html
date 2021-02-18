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

  componentDidUpdate(props, state) {
    if (props.logged !== this.props.logged) this.method.checkUser()
  }

  method = {
    checkUser: () => {
      let { logged, userInfo, login, logout } = this.props
      login({id: 'eai', name: 'eai', authId: '0'})
      
      urmsc.ajax({
        type: 'POST',
        url: '/URM/check/login',
        contentType: 'application/json; charset=UTF-8',
        success: function(res) {
          if (res.code === 0) {
            if (!logged) {
              login(res.obj)
            } else if (userInfo.id !== res.obj.id) {
              urmsc.ajax({
                type: 'GET',
                url: '/URM/logout',
                dataType: 'text',
                beforeSend: function () {
                  logout()
                },
              })
            }
          } else {
            console.log(res, res.message)
            logged && logout()
            if (res.obj && res.obj.endsWith('.page')) {
              window.location = res.obj
            }
          }
        }
      })
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