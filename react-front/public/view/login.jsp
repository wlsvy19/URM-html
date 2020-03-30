<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>URM - Login</title>
<style type="text/css">
input {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  font-variant: tabular-nums;
  list-style: none;
  -webkit-font-feature-settings: 'tnum';
  font-feature-settings: 'tnum', "tnum";
  position: relative;
  height: 24px;
  padding: 4px 11px;
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  line-height: 1.5;
  background-color: #fff;
  background-image: none;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  -webkit-transition: all 0.3s;
  transition: all 0.3s;
}

.login-header {
  height: 53px;
  display: flex;
  border-bottom: 1px solid #e8e8e8;
}

.login-body {
  width: 350px;
  height: 150px;
  margin: 0 auto;
  margin-top: 20%;
  border: 1px solid #e8e8e8;
}

#login-form {
  width: 300px;
  margin: 0 auto;
  margin-top: 40px;
}

#login-form > label {
  display: inline-block;
  text-align: right;
  width: 100px;
}

.logo {
  display: flex;
  padding: 7px;
}

.logo > span {
  padding: 5px;
}
</style>
</head>
<body>
<div class="login-header">
  <div class="logo">
    <img src="../logo.gif" height="29" />
    <span>EAI 요건 관리 시스템</span>
  </div>
</div>

<div class="login-body">
  <div id="login-form">
    <label for="login-user">ID</label> <input type="text" id="login-user">
    <br/>
    <label for="login-passwd">Password</label> <input type="password" id="login-passwd" onkeydown="loginHandler(event)">
    <br/>
    <div style="text-align: right;"><button onclick="loginHandler()">Login</button></div>
  </div>
</div>

<script type="text/javascript">
function loginHandler (e) {
  if (e && e.keyCode !== 13) return false;

  let loginForm = {
    id: document.getElementById('login-user').value,
    password: document.getElementById('login-passwd').value,
  };

  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/URM/login/process', true);
  xhr.onreadystatechange  = function () {
    if (this.readyState === 4) {
      let res = this.response
      if (this.status === 200) {
        res = JSON.parse(this.response);
        if (res.code === 0) {
          sessionStorage.setItem('URMUser', JSON.stringify(res.obj));
          self.location = '/URM/';
        } else {
          console.log(res.message)
        }
      } else {
        console.log('statusText [' + this.statusText + '] error [' + res + ']');
      }
    } else if (this.readyState === 1) {
      xhr.setRequestHeader('AjaxCall', 'true');
    }
  }

  xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
  xhr.send(JSON.stringify(loginForm));
}

</script>
</body>
</html>