import React from 'react'
import { DatePicker, TimePicker, Button } from 'antd'
import { Form, Input } from 'antd'
import moment from 'moment'

import LogDeferredList from './list/LogDeferred-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DeferredSearch extends React.Component {
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
        startTime: fields.startTime.format('HHmm'),
        endTime: fields.endTime.format('HHmm'),
        interfaceId: fields.interfaceId,
      }
    },
    
    onKeyDown: e => {
      if (e && e.keyCode === 13) {
        this.props.search(this.method.makeParams())
      }
    }
  }

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
              {getFieldDecorator("startTime", {initialValue: moment('00:00', 'HH:mm')})(<TimePicker size="small" style={{width: 80}} allowClear={false} format="HH:mm" />)}
            </Form.Item>
            <Form.Item><span>~</span></Form.Item>
            <Form.Item>
              {getFieldDecorator("endTime", {initialValue: moment('23:59', 'HH:mm')})(<TimePicker size="small" style={{width: 80}} allowClear={false} format="HH:mm" />)}
            </Form.Item>
            <Form.Item label="인터페이스 아이디">{getFieldDecorator("interfaceId")(<Input size="small" className="search-id" onKeyDown={this.method.onKeyDown} />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

const WrappedDeferredSearch = Form.create({name:'dfd_search'})(DeferredSearch)
export default class LogDeferred extends React.Component {
  method = {
    search: (params) => {
      let type = this.refs.list.state.type
      let _this = this
      let url = '/URM/api/process/log/deferred'
      let list = 'items'
      if (type === 'error') {
        url += '/error'
        list = 'errors'
      }
      urmsc.ajax({
        type: 'GET',
        url: url,
        data: params,
        success: function(res) {
          _this.refs.list.setState({[list]: res})
        }
      })
    },
  }

  getType = () => {
    let type = this.props.match.params.path.charAt(0)
    return type === 'd' ? 1 : (type === 't' ? 2 : 3)
  }

  searchForm = null

  render() {
    return (
      <div className="urm-panel">
        <div className="urm-header">디퍼드 거래처리로그</div>
        <WrappedDeferredSearch search={this.method.search} match={this.props.match} wrappedComponentRef={ref => { if (ref) this.searchForm = ref.props.form }} />
        <LogDeferredList ref="list" devType={this.getType()} />
      </div>
    );
  }
}
