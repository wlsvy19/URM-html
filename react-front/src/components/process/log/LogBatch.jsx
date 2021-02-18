import React from 'react'
import { DatePicker, TimePicker, Button } from 'antd'
import { Form, Input, Radio } from 'antd'
import moment from 'moment'

import LogBatchList from './list/LogBatch-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class BatchSearch extends React.Component {
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
        batchId: fields.batchId,
        result: fields.result,
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
            <Form.Item label="배치 아이디">{getFieldDecorator("batchId")(<Input size="small" className="search-id" onKeyDown={this.method.onKeyDown} />)}</Form.Item>
            <Form.Item label={locale['label.trType']}>
              {getFieldDecorator("result", {initialValue: "T"})(<Radio.Group className="size-id">
                <Radio value="T">전체</Radio>
                <Radio value="E">실패</Radio>
              </Radio.Group>)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

const WrappedBatchSearch = Form.create({name:'bat_search'})(BatchSearch)
export default class LogBatch extends React.Component {
  method = {
    search: (params) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/log/batch',
        data: params,
        success: function(res) {
          _this.refs.list.setState({items: res, details: []})
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
        <div className="urm-header">배치 거래처리로그</div>
        <WrappedBatchSearch search={this.method.search} match={this.props.match} />
        <LogBatchList ref="list" devType={this.getType()} />
      </div>
    );
  }
}
