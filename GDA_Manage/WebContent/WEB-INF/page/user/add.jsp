<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.daka.entry.SysUserVO"%>
<%@page import="java.util.List"%>
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
<form id="form">
		<div class="layui-form-item">
			<label class="layui-form-label">用户名</label>
			<div class="layui-input-inline">
				<input type="text" name="username" id="username"
					autocomplete="off" placeholder='请输入用户名' class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">密码</label>
			<div class="layui-input-inline">
				<input type="text" name="password" id="password"
					autocomplete="off" placeholder='请输入密码' class="layui-input">
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">昵称</label>
			<div class="layui-input-inline">
				<input type="text" name="nick_name" id="nick_name"
					autocomplete="off" placeholder='请输入昵称' class="layui-input">
			</div>
		</div>

	<div class="layui-form-item">
		<label class="layui-form-label">类型</label>
		<div class="layui-input-inline" style="line-height: 50px">
			<select lay-verify="required" lay-search="" style="height: 38px" id="type" name="type">
				<option value="0" <c:if test="${0 == result.type }">selected="selected"</c:if>>管理员</option>
				<option value="1" <c:if test="${1 == result.type }">selected="selected"</c:if>>审核员</option>
			</select>
		</div>
	</div>

		<hr />
		<input type="button" onclick="add_edit_task()" value="提交"
			class="layui-btn layui-btn-sm" style="float: right;">
	</form>
</body>
<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>
<script type="text/javascript">
	function add_edit_task() {
		debugger;
		var username=$("#username").val();
		var password=$("#password").val();
		var nickName=$("#nick_name").val();
		var type=$("#type").val();
		var param={username:username,password:password,nickName:nickName,type:type};
		$.post("../user/insertUser.do?username="+username+"&password="+password+"&nickName="+nickName+"&type="+type,function(data) {
			parent.layer.closeAll();
			window.parent.location.reload();
		})
		return false;
	}
</script>
</html>