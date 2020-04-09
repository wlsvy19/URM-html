import React from 'react'
import { Form, Select, DatePicker, Button } from 'antd'
import moment from 'moment'

import ProcessList from './list/RequestProcess-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class MonthSearch extends React.Component {
  componentDidMount() {
    this.props.search(this.props.form.getFieldsValue())
  }

  method = {
    clickSearch: e => {
      let form = this.props.form
      let params = {
        interfaceType: form.getFieldValue('interfaceType'),
        startDate: form.getFieldValue('startDate').format('YYYYMM'),
        endDate: form.getFieldValue('endDate').format('YYYYMM'),
      }
      
      this.props.search(params)
    },
    
    renderOpts: (key) => {
      return urmsc.getSubListByKey(this.props.codeList, 'kind', urmsc.CODEKEY[key])
              .map((it) => <Select.Option key={it.code} value={it.code}>{it.name}</Select.Option>)
    },
  }

  render() {
    const { MonthPicker } = DatePicker
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
            <Form.Item label="일자 선택">
              {getFieldDecorator("startDate", {initialValue: moment()})(<MonthPicker size="small" className="search-id" allowClear={false} />)}
            </Form.Item>
            <Form.Item>
              {getFieldDecorator("endDate", {initialValue: moment()})(<MonthPicker size="small" className="search-id" allowClear={false} />)}
            </Form.Item>
            <Form.Item className="search-buttons">
              <Button icon="search" onClick={this.method.clickSearch} title={locale['label.search']} />
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
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/stat/process/month',
        data: params,
        success: function(res) {
          $this.refs.list.setState({items: res})
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
