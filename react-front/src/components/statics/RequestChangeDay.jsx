import React from 'react'
import { Form, DatePicker, Button } from 'antd'
import moment from 'moment'

import ChangeList from './list/RequestChange-list'
import * as urmsc from '@/urm-utils'

const locale = urmsc.locale

class DaySearch extends React.Component {
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
        startDate: dates[0].format('YYYYMMDD'),
        endDate: dates[1].format('YYYYMMDD'),
      }
    },
  }

  render() {
    const { RangePicker } = DatePicker
    const { getFieldDecorator } = this.props.form

    return (
      <div className="search-bar">
        <Form colon={false}>
          <div className="row">
            <Form.Item label="일자 선택">{getFieldDecorator("processDate", {
              initialValue: [moment(), moment()]
            })(<RangePicker size="small" style={{width: "220px"}} allowClear={false} />)}</Form.Item>
            <Form.Item className="search-buttons">
              <Button size="small" onClick={this.method.clickSearch}>{locale['label.search']}</Button>
            </Form.Item>
          </div>
        </Form>
      </div>
    );
  }
}

const WrappedDaySearch = Form.create({name:'cday_search'})(DaySearch)
export default class RequestChangeDay extends React.Component {
  method = {
    search: (params) => {
      let _this = this
      urmsc.ajax({
        type: 'GET',
        url: '/URM/api/stat/change/day',
        data: params,
        success: function(res) {
          _this.refs.list.setState({items: res})
        }
      })
    }
  }

  render() {
    return (
      <div className="urm-panel">
        <WrappedDaySearch search={this.method.search} />
        <ChangeList ref="list" />
      </div>
    );
  }
}
