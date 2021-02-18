import React from 'react'
import { DatePicker, TimePicker, Button } from 'antd'
import { Form, Input, Checkbox } from 'antd'
import moment from 'moment'

import LogRealtimeList from './list/LogRealtime-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class RealtimeSearch extends React.Component {
  method = {
    clickSearch: e => {
      this.props.search(this.method.makeParams())
    },
    
    makeParams: () => {
      let form = this.props.form
      let fields = form.getFieldsValue()
      let type = this.props.match.params.path.charAt(0)
      return {
        type: type === 'd' ? 1 : (type === 't' ? 2 : 3),
        processDate: fields.processDate.format('YYYYMMDD'),
        startTime: fields.startTime.format('HHmmss'),
        endTime: fields.endTime.format('HHmmss'),
        interfaceId: fields.interfaceId,
        globalId: fields.globalId,
        error: fields.error ? 'Y' : 'N',
      }
    },
    
    onKeyDown: e => {
      if (e && e.keyCode === 13) {
        this.props.search(this.method.makeParams())
      }
    }
  }

  currentTime = moment().format('HH')

  render() {
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar" style={urmsc.bgColor(this.props.match)}>
        <Form colon={false}>
          <div className="row">
            <Form.Item label="처리날짜">
              {getFieldDecorator("processDate", {initialValue: moment()})(<DatePicker size="small" style={{width: 125}} allowClear={false} />)}
            </Form.Item>
            <Form.Item>
              {getFieldDecorator("startTime", {initialValue: moment(this.currentTime + ':00:00', 'HH:mm:ss')})(<TimePicker size="small" style={{width: 100}} allowClear={false} />)}
            </Form.Item>
            <Form.Item><span>~</span></Form.Item>
            <Form.Item>
              {getFieldDecorator("endTime", {initialValue: moment(this.currentTime + ':59:59', 'HH:mm:ss')})(<TimePicker size="small" style={{width: 100}} allowClear={false} />)}
            </Form.Item>
            <Form.Item label="인터페이스 아이디">{getFieldDecorator("interfaceId")(<Input size="small" className="search-id" onKeyDown={this.method.onKeyDown} />)}</Form.Item>
            <Form.Item label="GlobalID">{getFieldDecorator("globalId")(<Input size="small" className="search-id" onKeyDown={this.method.onKeyDown} />)}</Form.Item>
            <Form.Item label="에러만">{getFieldDecorator("error", {valuePropName: "checked", initialValue: false})(<Checkbox className="search-id" />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

const WrappedRealtimeSearch = Form.create({name:'onl_search'})(RealtimeSearch)
export default class LogRealtime extends React.Component {
  method = {
    search: (params) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/log/online',
        data: params,
        success: function(res) {
          _this.refs.list.setState({items: res})
        }
      })
    }
  }

  getType = () => {
    let type = this.props.match.params.path.charAt(0)
    return type === 'd' ? 1 : (type === 't' ? 2 : 3)
  }

  render() {
    return (
      <div className="urm-panel">
        <div className="urm-header">온라인 거래처리로그</div>
        <WrappedRealtimeSearch search={this.method.search} match={this.props.match} />
        <LogRealtimeList ref="list" devType={this.getType()} />
      </div>
    );
  }
}
