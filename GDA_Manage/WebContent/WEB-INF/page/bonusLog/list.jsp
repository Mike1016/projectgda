<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
<link rel="stylesheet" href="../static/layui/css/layui.css" />
<link rel="stylesheet" href="../static/css/common.css" />
<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>
</head>
<body>
	<!--条件查询-->
	<div class="condition">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">用户账号</label>
				<div class="layui-input-inline">
					<input type="text" name="userName" id="userName" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="userName" placeholder='请输入用户只账号(模糊查询)'>
				</div>
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="opetion_btn">

			 </span>
		</div>
	</div>
	<hr>
	<div>
<table id="demo"   lay-filter="test"></table>
	</div>
	<div id="pages"></div>
</body>

<script type="text/javascript">
var table;
var tableInstance;
function reload(){
	tableInstance.reload({
		  where: {
              userName: $("#userName").val()
		  }
		  ,page: {
		    curr: 1
		  }
});
}

function del(id) {
    var res = confirm("确认要删除吗?删除之后数据不可找回")
    if (res == false){
        return;
    }
    $.ajax({
        url:"../bonusLog/deleteData.do",
        type:"get",
        data:"id="+id,
        dataType:"json",
        success:function(data) {
            alert(data.msg)
            location.reload();
        }
    });
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;

        tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../bonusLog/queryData.do' //数据接口
		    ,response: {
		    		  statusName: 'status' //数据状态的字段名称，默认：code
		    		  ,statusCode: 200 //成功的状态码，默认：0
		    		  ,msgName: 'hint' //状态信息的字段名称，默认：msg
		    		  ,countName: 'count' //数据总数的字段名称，默认：count
		    		  ,dataName: 'data' //数据列表的字段名称，默认：data
		    		} 
		    ,page: true //开启分页
		    ,cols: [[//表头
		        {field: 'id',type:"checkbox", width:"5%", sort: true, fixed: 'left'}
		        ,{field: 'userName', title: '用户账号', width:"18%"}
			    ,{field: 'origin_account', title: '原始金额', width:"18%"}
			    ,{field: 'account', title: '金额', width:"18%"}
                ,{field: 'interest', title: '利率', width:"18%"}
                ,{field: 'create_time', title: '创建时间', width:"22%", sort: true}
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(res){

			  }
		  });
	});
</script>
</html>