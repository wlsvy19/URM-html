<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>URM - Login</title>
<style type="text/css">
body {
  background-color: #e6f7ff;
  font-size: 18px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
}

input {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  font-variant: tabular-nums;
  list-style: none;
  -webkit-font-feature-settings: 'tnum';
  font-feature-settings: 'tnum', "tnum";
  position: relative;
  height: 32px;
  padding: 4px 11px;
  color: rgba(0, 0, 0, 0.65);
  line-height: 1.5;
  background-color: #fff;
  background-image: none;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  -webkit-transition: all 0.3s;
  transition: all 0.3s;
}

.login-body {
  width: 350px;
  height: 350px;
  margin: 0 auto;
  margin-top: 15%;
  border: 1px solid #e8e8e8;
  background-color: #fff;
  box-shadow: 4px 5px 5px #b8b8b8;
}

#login-form {
  width: 300px;
  margin: 0 auto;
  margin-top: 30px;
}

#login-form label {
  display: inline-block;
  padding: 0 7px;
  width: 100%;
  height: 34px;
  line-height: 34px;
}

#login-form input {
  width: 100%;
}

#login-form button {
  line-height: 1.499;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  -webkit-box-shadow: 0 2px 0 rgba(0,0,0,.015);
  box-shadow: 0 2px 0 rgba(0,0,0,.015);
  cursor: pointer;
  -webkit-transition: all .3s cubic-bezier(.645,.045,.355,1);
  transition: all .3s cubic-bezier(.645,.045,.355,1);
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  height: 38px;
  width: 100%;
  font-size: 18px;
  border-radius: 4px;
  color: rgba(255,255,255,.65);
  background-color: #007ac3;
  border: 1px solid #d9d9d9;
  margin-top: 15px;
}

.logo {
  display: flex;
  padding: 15px 35px;
  margin-top: 25px;
}

.logo > span {
  padding: 2px 10px;
  font-weight: bold;
}
</style>
</head>
<body>
<div class="login-body">
  <div class="logo">
    <img src="../logo.gif" height="29" />
    <span>EAI 요건 관리 시스템</span>
  </div>
  <div id="login-form">
    <div>
      <label for="login-user">ID</label> <input type="text" id="login-user">
    </div>
    <div>
      <label for="login-passwd">Password</label> <input type="password" id="login-passwd" onkeydown="loginHandler(event)">
    </div>
    <div><button onclick="loginHandler()">Login</button></div>
  </div>
</div>

<script type="text/javascript">
function loginHandler (e) {
  if (e && e.keyCode !== 13) return false;

  var loginForm = {
    id: document.getElementById('login-user').value,
    password: document.getElementById('login-passwd').value,
  };

  var xhr = new XMLHttpRequest();
  xhr.open('POST', '/URM/login/process', true);
  xhr.onreadystatechange  = function () {
    if (this.readyState === 4) {
      var res = this.response
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