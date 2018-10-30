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
		<div class="layui-form-item">
			<label class="layui-form-label">用户</label>
			<div class="layui-input-inline">
				<input type="text" name="userName" id="userName"
					autocomplete="off" value='' class="layui-input">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">金额</label>
			<div class="layui-input-inline">
				<input type="text" name="account" id="account"
					autocomplete="off"  value='' class="layui-input"
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
        var userName = $("#userName").val();
        var account = $("#account").val();
        var url = "../sellout/whetherHaveUser.do";
        var param = {userName:userName};
        $.post(url,param, function(data) {
            debugger;
           if (data.status == 0){
               alert(data.msg);
               parent.layer.closeAll();
               window.parent.location.reload();
		   }
           else{
               var res = confirm("确认要提交吗?")
               if (res == false){
                   return;
               }
               var customerId = data.data.id;
               var wealth = data.data.wealth;
               var param_1 = {customerId:customerId,account:account,wealth:wealth};
               var url_1 = "../sellout/save.do";
               $.post(url_1,param_1, function(result) {
                   alert(result.msg);
                   parent.layer.closeAll();
                   window.parent.location.reload();
               });
		   }
        });
		return false;
	}
</script>
</html>