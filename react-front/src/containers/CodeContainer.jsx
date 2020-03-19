import React from 'react'
import { connect } from 'react-redux'

class CodeContainer extends React.Component {
  render() {
    const { logged, component, ...restProps } = this.props
    let Component = component
    return (
      <div>
        {Component && logged &&
          <Component {...restProps} />}
      </div>
    );
  }
}

const mapStateToProps = ({ code, auth }) => ({
  commonCode: code.commonCode,
  authCode: code.authCode,
  logged: auth.logged,
})

const mapDispatchToProps = dispatch => ({})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CodeContainer)