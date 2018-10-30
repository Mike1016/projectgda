<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../static/layui/css/layui.css" />
<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<title></title>
</head>

<body style="">
<form id="form" class="layui-form" action="">
	    <input type="hidden" name="id" id='id' value='${result.id}'>
		<div class="layui-form-item">
			<label class="layui-form-label">用户名</label>
			<div class="layui-input-inline">
				<input type="text" name="userName" id="userName"
					autocomplete="off" lay-verify="required|username" placeholder='请输入用户名' value='${result.userName}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">城市</label>
			<div class="layui-input-inline">
				<input type="text" name="city" id="city"
					autocomplete="off" placeholder='请输入城市' value='${result.city}' class="layui-input">
			</div>
		</div>

	    <div class="layui-form-item">
			<label class="layui-form-label">微信</label>
			<div class="layui-input-inline">
				<input type="text" name="weChat" id="weChat"
					   autocomplete="off" placeholder='请输入微信' value='${result.weChat}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">支付宝</label>
			<div class="layui-input-inline">
				<input type="text" name="alipay" id="alipay"
					   autocomplete="off" placeholder='请输入支付宝' value='${result.alipay}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">身份证号</label>
			<div class="layui-input-inline">
				<input type="text" name="identityCard" id="identityCard"
					   autocomplete="off" placeholder='请输入身份证号' lay-verify="required|identity" value='${result.identityCard}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">手机号</label>
			<div class="layui-input-inline">
				<input type="tel" name="phone" id="phone"
					   autocomplete="off" placeholder='请输入手机号' lay-verify="required|phone|number" value='${result.phone}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">密码</label>
			<div class="layui-input-inline">
				<input type="text" name="password" id="password" MAXLENGTH="18"
					   autocomplete="off" placeholder='请输入密码(6-18位)' lay-verify="" value='' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">安全码</label>
			<div class="layui-input-inline">
				<input type="text" name="securityPassword" id="securityPassword" MAXLENGTH="6"
					   autocomplete="off" placeholder='请输入安全密码(4-6位)' lay-verify="" value='' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
				<label class="layui-form-label">激活数</label>
				<div class="layui-input-inline">
					<input type="text" name="activationNumber" id="activationNumber"
						   autocomplete="off" placeholder='请输入激活数' value='${result.activationNumber}' class="layui-input">
				</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">财富值</label>
			<div class="layui-input-inline">
				<input type="text" name="wealth" id="wealth"
					   autocomplete="off" placeholder='请输入财富值' value='${result.wealth}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">奖金</label>
			<div class="layui-input-inline">
				<input type="text" name="bonus" id="bonus"
					   autocomplete="off" placeholder='请输入奖金' value='${result.bonus}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">活动值</label>
			<div class="layui-input-inline">
				<input type="text" name="activity" id="activity"
					   autocomplete="off" placeholder='请输入活动值' value='${result.activity}' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">收货地址</label>
			<div class="layui-input-inline">
				<input type="text" name="address" id="address"
					   autocomplete="off" placeholder='请输入收货地址' value='${result.address}' class="layui-input">
		    </div>
		</div>
		<hr />
		<%--<input type="button" onclick="add_edit_task()" value="提交"--%>
			   <%--lay-submit="" class="layui-btn layui-btn-sm" style="float: right;">--%>
	<div class="layui-form-item">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">立即提交</button>
		</div>
	</div>
	</form>
</body>
<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>

<script type="text/javascript">
    $(document).ready(function(){
        if($("#id").val()!=''){
            $("#userName").attr('disabled','disabled').css("background-color","#E8E8E8");
        }
    })

    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form
            ,layer = layui.layer

        form.verify({
            username: function(value, item){ //value：表单的值、item：表单的DOM对象
                if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
                    return '用户名不能有特殊字符';
                }
                if(/(^\_)|(\__)|(\_+$)/.test(value)){
                    return '用户名首尾不能出现下划线\'_\'';
                }
                if(/^\d+\d+\d$/.test(value)){
                    return '用户名不能全为数字';
                }
            }
        });

        //监听提交
        form.on('submit(demo1)', function(data){
            add_edit_task()
            parent.layer.closeAll();
            window.parent.location.reload();
        });
    });

	function add_edit_task() {
        var id = $("#id").val();
        var btnType = (id == '' ? 'add' : 'edit');
        var userName = $("#userName").val();
        var city = $("#city").val();
        var weChat = $("#weChat").val();
        var alipay = $("#alipay").val();
        var identityCard = $("#identityCard").val();
        var phone = $("#phone").val();
        var password = $("#password").val();
        var activationNumber = $("#activationNumber").val();
        var securityPassword = $("#securityPassword").val();
        var wealth = $("#wealth").val();
        var bonus = $("#bonus").val();
        var activity = $("#activity").val();
        var address = $("#address").val();
		var param = {id:id,btnType:btnType,userName:userName,city:city,weChat:weChat,alipay:alipay
			,identityCard:identityCard,phone:phone,password:password,activationNumber:activationNumber
			,securityPassword:securityPassword,wealth:wealth,bonus:bonus,activity:activity,address:address};
		var url_1 = "../customer/toAddEdit.do";
		$.post(url_1,param, function(result) {
			alert(result.msg);
			parent.layer.closeAll();
			window.parent.location.reload();
		});
		return false;
	}
</script>
</html>