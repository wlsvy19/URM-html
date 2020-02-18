export default {
  ajax: function (method, url, param, async, callback) {
    if (async === undefined) async = true
    var xhr = new XMLHttpRequest()
    xhr.open(method, url, async)
    console.log(url, param)
    xhr.onreadystatechange  = callback
    
    if (method === 'POST') xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(param)
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
    inOut: '9',
    dataType: '10',
    devType: '11',
    dbCrudType: '12',
    fileCrudType: '13',
  },

  getSubListByKey: (list, key, val) => {
    return list.filter(item => item[key] === val);
  },

}