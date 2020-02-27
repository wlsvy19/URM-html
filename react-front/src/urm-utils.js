export default {
  ajax: function (props) {
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
    
    let xhr = new XMLHttpRequest()
    xhr.open(props.method, props.url, props.async)
    console.log(props.url, props.data)
    xhr.onreadystatechange  = function () {
      if (this.readyState === 4) {
        let res = this.response
        if (props.dataType === 'json') res = JSON.parse(this.response) 
        if (this.status === 200) {
          if (props.success) props.success(res)
        } else {
          if (props.error) props.error(res)
        }
      } else if (this.readyState === 1) {
        if (props.beforeSend) props.beforeSend()
      }
    }
    
    xhr.setRequestHeader('Content-Type', props.contentType);
    xhr.send(props.data)
  },
  
  codeKey: {
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
  },

  getSubListByKey: (list, key, val) => {
    return list.filter(item => item[key] === val);
  },
  
  convertYN: (obj) => {
    if (typeof obj === 'boolean') {
      return obj ? 'Y' : 'N'
    } else if (typeof obj === 'string') {
      return obj === 'Y' ? true : false
    }
  },

  RULE: {
    codeKey: {
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
    },

    getSubListByKey: (list, key, val) => {
      return list.filter(item => item[key] === val);
    },

    getCodeByKey: (list, key) => {
      return this.getSubListByKey(list, 'kind', this.codeKey[key])
    },

    renderOptsForCode: (codeList, key, renderFunc) => {console.log(this)
      return this.getCodeByKey(codeList, key).map(renderFunc)
    },
  }
}