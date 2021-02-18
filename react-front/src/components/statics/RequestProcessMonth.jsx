import React from 'react'
import { Form, Select, DatePicker, Button } from 'antd'
import moment from 'moment'

import ProcessList from './list/RequestProcess-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class MonthSearch extends React.Component {
  componentDidMount() {
    //this.props.search(this.method.makeParams())
  }

  method = {
    clickSearch: e => {
      this.props.search(this.method.makeParams())
    },
    
    makeParams: () => {
      let form = this.props.form
      let dates = form.getFieldValue('processDate')
      return {
        interfaceType: form.getFieldValue('interfaceType'),
        startDate: dates[0].format('YYYYMM'),
        endDate: dates[1].format('YYYYMM'),
      }
    },
    
    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
  }

  render() {
    const { RangePicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label={locale['label.interfaceType']}>
              {getFieldDecorator("type", {initialValue: ""})(<Select size="small" className="search-id">
                <Select.Option value="">ALL</Select.Option>
                {this.method.renderOpts("infType")}
              </Select>)}
            </Form.Item>
            <Form.Item label="일자 선택">{getFieldDecorator("processDate", {
              initialValue: [moment(), moment()]
            })(<RangePicker size="small" style={{width: "220px"}} allowClear={false} format='YYYY-MM' mode={['month', 'month']} />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

const WrappedMonthSearch = Form.create({name:'pmonth_search'})(MonthSearch)
export default class RequestProcessMonth extends React.Component {
  method = {
    search: (params) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/stat/process/month',
        data: params,
        success: function(res) {
          _this.refs.list.setState({items: res})
        }
      })
    }
  }

  codeList = () => {
    let codeList = this.props.commonCode
    return codeList ? codeList : []
  }

  render() {
    return (
      <div className="urm-panel" style={{minWidth: 1345}}>
        <WrappedMonthSearch codeList={this.codeList()} search={this.method.search} />
        <ProcessList ref="list" />
      </div>
    );
  }
}
