import React from 'react'
import { Form, DatePicker, Button } from 'antd'
import moment from 'moment'

import ChangeList from './list/RequestChange-list'
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
        startDate: form.getFieldValue('startDate').format('YYYYMM'),
        endDate: form.getFieldValue('endDate').format('YYYYMM'),
      }
      
      this.props.search(params)
    },
  }

  render() {
    const { MonthPicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
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

const WrappedMonthSearch = Form.create({name:'cmonth_search'})(MonthSearch)
export default class RequestChangeDay extends React.Component {
  method = {
    search: (params) => {
      let $this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/stat/change/day',
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
      <div className="urm-panel">
        <WrappedMonthSearch search={this.method.search} />
        <ChangeList ref="list" />
      </div>
    );
  }
}
