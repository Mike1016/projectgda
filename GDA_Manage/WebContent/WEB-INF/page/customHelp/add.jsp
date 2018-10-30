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
	    <input type="hidden" name="id" id='id' value='${result.id}'>
	    <input type="hidden" name="pathImg" id='pathImg' value='${result.imgPath}'>
		<div class="layui-upload">
			<div class="layui-upload-list">
				<img class="layui-upload-img" id="demo1" width="350px" height="240px">
				<p id="demoText"></p>
			</div>
			<input type="hidden" name="imgPath" id='imgPath' value=''>
			<button type="button" class="layui-btn" id="test1">上传图片</button>
		</div>
	<hr />
            <input type="button" onclick="add_edit_task()" value="提交"
        class="layui-btn layui-btn-sm" style="float: right;">
                </form>
                </body>
                <script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>

<script>
    layui.use('upload', function(){
        var $ = layui.jquery
            ,upload = layui.upload;

        //普通图片上传
        var uploadInst = upload.render({
            elem: '#test1'
            ,url: '../customHelp/upLoad.do'
            ,before: function(obj){
                //预读本地文件示例，不支持ie8
                obj.preview(function(index, file, result){
                    $('#demo1').attr('src', result);
                });
            }
            ,done:function(res){
                debugger;
                $('#imgPath').val(res.data);
            }
        });
    });
</script>

<script type="text/javascript">
    $(document).ready(function(result){
		if ($("#id").val() != null && $("#id").val() != "") {
            $('#demo1').attr('src', "<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>"+$("#pathImg").val());
		}
    })

        function add_edit_task() {
        var res = confirm("确认要提交数据吗?")
        if (res == false){
            return;
        }
        var id = $("#id").val();
        var imgPath = $("#imgPath").val();
		if (id == null || id == ""){
            var param = {imgPath:imgPath};
            var url_1 = "../customHelp/save.do";
            $.post(url_1,param, function(result) {
                alert(result.msg);
                parent.layer.closeAll();
                window.parent.location.reload();
            });
		}else{
			var param = {id:id,imgPath:imgPath};
			var url_1 = "../customHelp/edit.do";
			$.post(url_1,param, function(result) {
					alert(result.msg);
					parent.layer.closeAll();
					window.parent.location.reload();
			});
        }
		return false;
	}
</script>
</html>