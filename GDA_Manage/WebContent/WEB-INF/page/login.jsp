<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登录</title>
<link rel="stylesheet" href="../static/layui/css/layui.css" />
<link rel="stylesheet" href="../static/css/login.css" />
</head>
<body>
<div  class="cont">
	<div>
	    <div class="layui-form">
	        <div class="layui-form-item">
	            <h1 style="text-align: center;">后台管理系统</h1>
	        </div>
	        <hr />
	        <div class="layui-form-item">
	            <label class="layui-form-label">用户姓名:</label>
	            <div class="layui-input-block">
	                <input type="text" name="username" id="username" class="layui-input"  autocomplete="off"/>
	            </div>
	        </div>
	        <div class="layui-form-item">
	            <label class="layui-form-label">登录密码:</label>
	            <div class="layui-input-block">
	                <input type="password" name="password" id="password" class="layui-input"/>
	            </div>
	        </div>
	        <div class="layui-form-item">
	            <label class="layui-form-label">验证码:</label>
	            <div class="layui-input-inline">
	                <input type="text" name="verifycode" id="input" class="layui-input">
	            </div>
	           
  				<input type="button" id="code" onclick="createCode()"  value='获取验证码' /> 
	            
	        </div>
	        <div class="layui-form-item">
	            <div class="layui-input-block">
	                <input class="layui-input layui-input-primary login_btn" type="button" onclick="validate()" value="登录" />
	            </div>
	        </div>
	    </div>
    </div>
</div>

<div class="footer">© 2018-2020西安大咖实业有限公司 陕ICP备18009393号-1</div>
</body>
<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>
 <script type="text/javascript">
function login() {
		var param = {
			username : $("#username").val(),
			password : $("#password").val()
		};

		$
				.post(
						"/GDA_Manage/user/login.do",
						param,
						function(data) {
							if (data == 200) {
								window.location.href = "/GDA_Manage/daka/toIndex.do";
							} else {
								alert("用户名密码错误")
							}
						})
	}

layui.use(['layer'], function(){
    $ = layui.jquery;
    layer = layui.layer;
});


var code ; //在全局定义验证码  
       
function createCode(){ 
   code = "";  
   var codeLength = 4;
   var checkCode = document.getElementById("code");  
   var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',  
   'S','T','U','V','W','X','Y','Z');//随机数  
   for(var i = 0; i < codeLength; i++) {//循环操作  
    var index = Math.floor(Math.random()*36);//取得随机数的索引（0~35）  
    code += random[index];//根据索引取得随机数加到code上  
  }  
  checkCode.value = code;//把code值赋给验证码  
} 
//校验验证码  
function validate(){  
  var inputCode = document.getElementById("input").value.toUpperCase(); //取得输入的验证码并转化为大写     
  if(inputCode.length <= 0) { //若输入的验证码长度为0  
    alert("请输入验证码！"); 
  }else if(inputCode != code ) { //若输入的验证码与产生的验证码不一致时  
    alert("验证码输入错误！"); 
    createCode();//刷新验证码  
    document.getElementById("input").value = "";//清空文本框  
  }else {  
	  login();
  } 
} 
</script>
</html>