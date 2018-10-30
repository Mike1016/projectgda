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
	<input type="hidden" name="id" id='id' value='${result.id}'>
		<div class="layui-form-item">
			<label class="layui-form-label">标题</label>
			<div class="layui-input-inline">
				<input type="text" name="title" id="title"
					autocomplete="off" placeholder='${result.title}' class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">内容</label>
			<div class="layui-input-inline">
				<textarea  name="content" id="content" style="display: none;">${result.content}</textarea>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">创建时间</label>
			<div class="layui-input-inline">
				<input type="text" name="create_time" id="create_time"
					   autocomplete="off" placeholder='请输入创建时间(2018-08-08)' class="layui-input">
			</div>
		</div>

		<hr />
		<input type="button" onclick="add_edit_task()" value="修改"
			class="layui-btn layui-btn-sm" style="float: right;">
	</form>
</body>
<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        layui.use('layedit', function(){
            layedit = layui.layedit;
            layedit.build('content',{
                tool:[
                    'strong' //加粗
                    ,'italic' //斜体
                    ,'underline' //下划线
                    ,'del' //删除线
                    ,'|' //分割线
                    ,'left' //左对齐
                    ,'center' //居中对齐
                    ,'right' //右对齐
                ]
            }); //建立编辑器
        });
    })
</script>
<script type="text/javascript">
	function add_edit_task() {
        var res = confirm("确认要提交修改数据吗?")
        if (res == false){
            return;
        }
		debugger;
		var id = $("#id").val();
		var title=$("#title").val();
		var content=layedit.getContent(1);
        var createTime=$("#create_time").val();
		var param={id:id,title:title,content:content,create_time:createTime};
		$.post("../notice/updateNotice.do?id="+id+"&title="+title+"&content="+content+"&create_time="+createTime,function(data) {
			parent.layer.closeAll();
			window.parent.location.reload();
		})
		return false;
	}
</script>
</html>