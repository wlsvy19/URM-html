import React from 'react'
import { Form, DatePicker, Input, Button } from 'antd'
import moment from 'moment'

import StaticsDayList from './list/StaticsDay-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DaySearch extends React.Component {
  method = {
    clickSearch: e => {
      this.props.search(this.method.makeParams())
    },
    
    makeParams: () => {
      let form = this.props.form
      let dates = form.getFieldValue('processDate')
      let type = this.props.match.params.path.charAt(0)
      let infType = this.props.match.params.path.charAt(5)
      return {
        type: type === 'd' ? 1 : (type === 't' ? 2 : 3),
        infType: infType === '4' ? 'realtime' : (infType === '5' ? 'batch' : 'deferred'),
        startDate: dates[0].format('YYYYMMDD'),
        endDate: dates[1].format('YYYYMMDD'),
        interfaceId: form.getFieldValue('interfaceId'),
      }
    },
    
    onKeyDown: e => {
      if (e && e.keyCode === 13) {
        this.props.search(this.method.makeParams())
      }
    }
  }

  render() {
    const { RangePicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar psday" style={urmsc.bgColor(this.props.match)}>
        <Form colon={false}>
          <div className="row">
            <Form.Item label="처리날짜">{getFieldDecorator("processDate", {
              initialValue: [moment(), moment()]
            })(<RangePicker size="small" allowClear={false} style={{width: 260}} />)}</Form.Item>
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

const WrappedDaySearch = Form.create({name:'sday_search'})(DaySearch)
export default class ProcessStaticsDay extends React.Component {
  method = {
    search: (params) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/stat/day',
        data: params,
        success: function(res) {
          _this.refs.list.setState({items: res})
        }
      })
    }
  }

  getInfType = () => {
    let infType = ''
    let type = this.props.match.params.path.charAt(5)
    switch (type) {
    case '4' :
      infType = '온라인'
      break
    case '5' :
      infType = '배치'
      break
    case '6' :
      infType = '디퍼드'
      break
    default: break
    }
    return infType
  }

  render() {
    return (
      <div className="urm-panel">
        <div className="urm-header">일자별 {this.getInfType()} 거래처리통계</div>
        <WrappedDaySearch search={this.method.search} match={this.props.match} />
        <StaticsDayList ref="list" match={this.props.match} />
      </div>
    );
  }
}
