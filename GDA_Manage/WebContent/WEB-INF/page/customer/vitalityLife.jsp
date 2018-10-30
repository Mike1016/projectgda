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
<form id="form">
	    <input type="hidden" name="id" id='id' value='${id}'>
		<div class="layui-form-item">
			<label class="layui-form-label">激活数</label>
			<div class="layui-input-inline">
				<input type="text" name="activationNumber" id="activationNumber"
					autocomplete="off" placeholder='请输入增加的激活数'  class="layui-input"
					   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
					   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
			</div>
		</div>

	    <div class="layui-form-item">
			<label class="layui-form-label">生命力</label>
			<div class="layui-input-inline">
				<input type="text" name="vitality" id="vitality"
					   autocomplete="off" placeholder='请输入增加的生命力（0-100）'  class="layui-input"
					   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
					   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
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
        var id = $("#id").val();
        var activationNumber = $("#activationNumber").val();
        var vitality = $("#vitality").val();
		var param = {id:id,activationNumber:activationNumber,vitality:vitality};
		var url_1 = "../customer/distribution.do";
		$.post(url_1,param, function(result) {
			debugger;
				alert(result.msg);
				parent.layer.closeAll();
				window.parent.location.reload();

		});
		return false;
	}
</script>
</html>