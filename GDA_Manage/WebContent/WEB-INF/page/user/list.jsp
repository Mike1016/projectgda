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
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-inline">
					<input type="text" name="username" id="username" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="username" placeholder='请输入用户名(模糊查询)'>
				</div>
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="opetion_btn">
				 <button class="layui-btn layui-btn-sm" id="user_add" onclick="add('{{d.id}}')"><i class="layui-icon">&#xe654;</i>新增</button>
<!-- 			 <button class="layui-btn layui-btn-sm" id="user_delete" onclick="del()"><i class="layui-icon">&#xe640;</i>删除</button> -->
<!-- 			 <button class="layui-btn layui-btn-sm" id="user_freeze" onclick="freeze()"><i class="layui-icon">&#xe6b1;</i>锁定</button> -->
<!-- 			 <button class="layui-btn layui-btn-sm" id="user_active" onclick="active()"><i class="layui-icon">&#xe672;</i>激活</button> -->
<!-- 			 <button class="layui-btn layui-btn-sm" id="user_reset" onclick="reset()"><i class="layui-icon">&#xe672;</i>重置密码</button> -->
			 </span>
		</div>
	</div>
	<hr>
	<div>
<table id="demo"   lay-filter="test"></table>
	</div>
	<div id="pages"></div>
</body>

<script type="text/html" id="state">
	{{# if (d.state== 0) { }}
	<span style='color:blue'>可用</span>

	{{# } else if(d.state== 1) { }}
	<span style='color:red'>不可用</span>


	{{# }
	}}
</script><script type="text/html" id="type">
	{{# if (d.type== 0) { }}
	<span style='color:blue'>管理员</span>

	{{# } else if(d.type== 1) { }}
	<span style='color:green'>审核员</span>


	{{# }
	}}
</script>

<script type="text/javascript">
var table;
var tableInstance;
function reload(){
	tableInstance.reload({
		  where: { 
		    username: $("#username").val()
		  }
		  ,page: {
		    curr: 1
		  }
		});
}

function add(){
	layer.open({
		type:2,
		title:"新增",
		area:["450px","580px"],
		maxmin:true,
		offset: '20%',
		content:"../user/add.do"  
	});
}
function edit(id,url){
	var url = "../user/toReview.do";
	layer.open({
		type:2,
		title:"修改",
		area:["350px","350px"],
		maxmin:true,
		offset: '20%',
		content:url+"?id="+id
	});
}

function del(id) {
	$.ajax({
		url:"../user/delete.do",
		type:"get",
		data:"id="+id,
		dataType:"json",
		success:function(data) {
			location.reload();
		}
	});
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		
  		var oprateBtn = "<div><button class='layui-btn layui-btn-xs' id='user_update' onclick=edit('{{d.id}}')>修改</button>"
    			+"<button class='layui-btn layui-btn-xs'  id='user_delete' onclick=del('{{d.id}}')>删除</button>";
		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../user/queryData.do' //数据接口
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
		      ,{field: 'username', title: '账号', width:"15%"}
		      ,{field: 'nick_name', title: '姓名', width:"15%"}
		      ,{field: 'password', title: '密码', width:"15%"}
		      ,{field: 'create_time', title: '创建时间', width:"15%", sort: true}
		      ,{field: 'state', title: '状态', width:"15%",templet: "#state"}
		      ,{field: 'type', title: '类型', width:"10%",templet:"#type"}
		      ,{ width:"10%",title:"操作",
		    	  templet: oprateBtn
		       }
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(){
			    }
		  });
	});
</script>
</html>