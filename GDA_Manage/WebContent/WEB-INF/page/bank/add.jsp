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
				<input type="text" name="userName" id="userName"
					autocomplete="off" placeholder='请输入用户名' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">持卡人</label>
			<div class="layui-input-inline">
				<input type="text" name="cardHolder" id="cardHolder"
					   autocomplete="off" placeholder='请输入持卡人' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">银行卡卡号</label>
			<div class="layui-input-inline">
				<input type="text" name="cardNumber" id="cardNumber"
					   autocomplete="off" placeholder='请输入银行卡卡号' class="layui-input">
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
		var userName=$("#userName").val();
        var cardHolder=$("#cardHolder").val();
        var cardNumber=$("#cardNumber").val();
		var param={userName:userName,cardHolder:cardHolder,cardNumber:cardNumber};
		var url = "../bank/save.do";
		$.post(url,param,function(data) {
		    alert(data.msg);
			parent.layer.closeAll();
			window.parent.location.reload();
		})
		return false;
	}
</script>
</html>