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
    
  if (props === undefined) {
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
      let res = this.response
      if (this.status === 200) {
        if (props.dataType === 'json') res = JSON.parse(this.response)
        if (props.success) props.success(res)
      } else {
        if (props.error) {
          props.error(res)
        } else {
          ajaxHandleError(this.statusText, res)
        }
      }
    } else if (this.readyState === 1) {
      if (props.beforeSend) props.beforeSend()
    }
  }

  if (props.contentType) xhr.setRequestHeader('Content-Type', props.contentType)
  xhr.send(props.data)
}

export function ajaxHandleError (statusText, error) {
  console.log('statusText [' + statusText + '] error [' + error + ']')
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
