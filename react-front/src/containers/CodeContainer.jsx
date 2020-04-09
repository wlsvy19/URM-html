import React from 'react'
import { connect } from 'react-redux'

const urmProps = require('urmProperties')

class CodeContainer extends React.Component {
  isView = () => {
    let pathname = window.location.pathname
    let pathArr = pathname.split('/')
    let page = pathArr[2]
    
    let viewAuth = urmProps['page.' + page + '.view']
    let authId = this.props.userInfo.auth
    return this.props.logged && viewAuth.split(',').indexOf(authId) > -1
  }

  render() {
    const { logged, component, ...restProps } = this.props
    let Component = component
    return (
      <div>
        {Component && this.isView() &&
          <Component {...restProps} />}
      </div>
    );
  }
}

const mapStateToProps = ({ code, auth }) => ({
  commonCode: code.commonCode,
  authCode: code.authCode,
  logged: auth.logged,
  userInfo: auth.userInfo,
})

const mapDispatchToProps = dispatch => ({})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CodeContainer)