import { message } from 'antd'

export const locale = require('msgProperties')

export const CODEKEY = {
  procStat: '1',
  chgStat: '2',
  infType: '3',
  trType: '4',
  syncType: '5',
  sysType: '6',
  period: '7',
  dbType: '8',
  inOutGbn: '9',
  dataType: '10',
  devType: '11',
  dbCrudType: '12',
  fileCrudType: '13',
}

export const LIST_STATE = {
  UPDATE: 'U',
  DELETE: 'D',
}

export function ajax (props) {
  let defaultProps = {
    method: 'GET',
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    async: true,
    dataType: 'json',
  }
    
  if (!props) {
    props = defaultProps
  } else {
    if (props.method === undefined)  props.method = props.type ? props.type : defaultProps.method
    if (props.contentType === undefined) props.contentType = defaultProps.contentType
    if (props.async === undefined) props.async = defaultProps.async
    if (props.dataType === undefined) props.dataType = defaultProps.dataType
  }

  if (props.method === 'GET') {
    console.log(props.url, props.data)
    let param = props.data
    if (typeof param === 'string') param = JSON.parse(props.data)
    if (typeof param === 'object') {
      let paramStr = ''
      for (let key in param) {
        let value = param[key]
        if (value !== undefined && value !== null) {
          paramStr += '&' + key + '=' + encodeURIComponent(value)
        }
      }
      props.url = props.url + '?' + paramStr.substring(1)
    } else if (param) {
      console.log('Wrong Request Parameter!!')
      props.data = null
    }
  }
  
  let xhr = new XMLHttpRequest()
  xhr.open(props.method, props.url, props.async)
  xhr.onreadystatechange  = function () {
    if (this.readyState === 4) {
      if (this.status === 200) {
        let location = this.getResponseHeader('Location')
        console.log(location)
        if (location && location.endsWith('.page')) {
        	console.log('in', location)
            //window.location.href = location
        } else if (this.responseURL && this.responseURL.endsWith('.page')) {
          window.location.href = this.responseURL
        } else {
          let res = this.response
          if (props.dataType === 'json') {
            try {
              res = JSON.parse(this.response)
            } catch (e) {
              // session expired
              if (!this.responseURL) {
                message.warning('Session Expired!!! - Please refresh this page. ')
              }
            }
          }
          if (props.success) props.success(res)
        }
      } else {
        if (props.error) {
          props.error(this)
        } else {
          ajaxHandleError(this)
        }
      }
    } else if (this.readyState === 1) {
      if (props.beforeSend) props.beforeSend()
    }
  }

  if (props.contentType) xhr.setRequestHeader('Content-Type', props.contentType)
  xhr.send(props.data)
}

export function ajaxHandleError (xhr) {
  let res = xhr.statusText
  let txt = xhr.responseText
  if (txt) txt = txt.split('<HR size="1" noshade="noshade">').join('')
  let xmlDoc = null
  if (window.DOMParser) {
    let parser = new DOMParser();
    xmlDoc = parser.parseFromString(txt, 'text/xml');
  } else if (window.ActiveXObject) { // Internet Explorer
    xmlDoc = new window.ActiveXObject('Microsoft.XMLDOM');
    xmlDoc.async = false;
    xmlDoc.loadXML(txt);
  }
  //console.log([xmlDoc], xmlDoc.getElementsByTagName('h1')[0].childNodes[0].nodeValue)
  message.warning(res)
}

export function convertYN (obj) {
  if (typeof obj === 'boolean') {
    return obj ? 'Y' : 'N'
  } else if (typeof obj === 'string') {
    return obj === 'Y' ? true : false
  }
}

export function getSubListByKey (list, key, val) {
  return list.filter(item => item[key] === val);
}

const urmProps = require('urmProperties')
export function isSelectAuth (type, value, auth) {
  let values = urmProps['auth.' + auth + '.' + type]
  if (!values) {
    return true
  }
  values = values.split(',')
  for(let i = 0; i < values.length; i++) {
    if(values[i] === value) {
      return true
    }
  }
  return false
}

export function isPageEdit (page, type, auth) {
  if (page === 'datamap') page = 'request'
  let values = urmProps['page.' + page + '.' + type]
  if (!values) {
    return false
  }
  values = values.split(',')
  for(let i = 0; i < values.length; i++) {
    if(values[i] === auth) {
      return true
    }
  }
  return false
}

export function bgColor (match) {
  let color = '#e6f7ff'
  let type = match ? match.params.path.charAt(0) : ''
  switch (type) {
  case 'd' :
    color = '#8888ff'
    break
  case 't' :
    color = '#88ff88'
    break
  case 'p' :
    color = '#ff8888'
    break
  default: break
  }
  return {backgroundColor: color}
}