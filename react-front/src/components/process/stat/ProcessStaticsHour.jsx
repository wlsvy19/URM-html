import React from 'react'
import { DatePicker, Button } from 'antd'
import { Form, Input, Checkbox } from 'antd'
import moment from 'moment'

import StaticsHourList from './list/StaticsHour-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class HourSearch extends React.Component {
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
        infType: infType === '1' ? 'realtime' : (infType === '2' ? 'batch' : 'deferred'),
        startDate: dates[0].format('YYYYMMDDHH'),
        endDate: dates[1].format('YYYYMMDDHH'),
        interfaceId: form.getFieldValue('interfaceId'),
      }
    },
    
    onKeyDown: e => {
      if (e && e.keyCode === 13) {
        this.props.search(this.method.makeParams())
      }
    },
    
    renderSim: () => {
      const { getFieldDecorator } = this.props.form
      let type = this.props.match.params.path.charAt(0)
      let infType = this.props.match.params.path.charAt(5)
      if (type === 'd' && infType === '1') {
        return <Form.Item label="시뮬레이션건수">{getFieldDecorator("sim", {valuePropName: "checked", initialValue: true})(<Checkbox onChange={e => { this.props.filter('sim', e) }} />)}</Form.Item>
      }
      return undefined
    },
  }

  currentDate = moment().format('YYYY-MM-DD')

  render() {
    const { RangePicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar" style={urmsc.bgColor(this.props.match)}>
        <Form colon={false}>
          <div className="row">
            <Form.Item label="처리날짜">{getFieldDecorator("processDate", {
              initialValue: [moment(this.currentDate + ' 00'), moment(this.currentDate + ' 23')]
            })(<RangePicker size="small" allowClear={false} format='YYYY-MM-DD HH시' showTime={{ format: 'HH' }} style={{width: 295}} />)}</Form.Item>
            <Form.Item label="인터페이스 아이디">{getFieldDecorator("interfaceId")(<Input size="small" className="search-id" onKeyDown={this.method.onKeyDown} />)}</Form.Item>
            <Form.Item label="총건수">{getFieldDecorator("total", {valuePropName: "checked", initialValue: true})(<Checkbox onChange={e => { this.props.filter('total', e) }} />)}</Form.Item>
            {this.method.renderSim()}
            <Form.Item label="성공건수">{getFieldDecorator("success", {valuePropName: "checked", initialValue: true})(<Checkbox onChange={e => { this.props.filter('success', e) }} />)}</Form.Item>
            <Form.Item label="실패건수">{getFieldDecorator("fail", {valuePropName: "checked", initialValue: true})(<Checkbox onChange={e => { this.props.filter('fail', e) }} />)}</Form.Item>
            <Form.Item label="평균처리시간">{getFieldDecorator("avg", {valuePropName: "checked", initialValue: true})(<Checkbox onChange={e => { this.props.filter('avg', e) }} />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

const WrappedHourSearch = Form.create({name:'shour_search'})(HourSearch)
export default class ProcessStaticsHour extends React.Component {
  method = {
    search: (params) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/process/stat/hour',
        data: params,
        success: function(res) {
          _this.refs.list.setState({items: res})
          _this.method.getList()
        }
      })
    },
    
    getList: (key, e) => {
      let form = this.searchForm
      let fields = form.getFieldsValue()
      if (key) fields[key] = e.target.checked
      let items = this.refs.list.state.items
      let tmp = []
      items.forEach((it) => {
        if (fields.total && it.countType === '총건수') {
          tmp.push(it)
        } else if (fields.success && it.countType === '성공건수') {
          tmp.push(it)
        } else if (fields.fail && it.countType === '실패건수') {
          tmp.push(it)
        } else if (fields.avg && it.countType === '평균처리시간') {
          tmp.push(it)
        } else if (fields.sim && it.countType === '시뮬레이션건수') {
          tmp.push(it)
        }
      })
      this.refs.list.setState({filters: tmp})
    }
  }

  getInfType = () => {
    let infType = ''
    let type = this.props.match.params.path.charAt(5)
    switch (type) {
    case '1' :
      infType = '온라인'
      break
    case '2' :
      infType = '배치'
      break
    case '3' :
      infType = '디퍼드'
      break
    default: break
    }
    return infType
  }

  getStyle = () => {
    let type = this.props.match.params.path.charAt(0)
    let infType = this.props.match.params.path.charAt(5)
    if (type === 'd' && infType === '1') {
      return {minWidth: 1330}
    }
    return {}
  }
  
  searchForm = null

  render() {
    return (
      <div className="urm-panel" style={this.getStyle()}>
        <div className="urm-header">시간별 {this.getInfType()} 거래처리통계</div>
        <WrappedHourSearch search={this.method.search} match={this.props.match} filter={this.method.getList}
          wrappedComponentRef={ref => { if (ref) this.searchForm = ref.props.form }} />
        <StaticsHourList ref="list" />
      </div>
    );
  }
}
