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
			
				<label class="layui-form-label">标题</label>
				<div class="layui-input-inline">
					<input type="text" name="title" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="title" placeholder='请输入标题(模糊查询)'>
				</div>
				
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 			 <button class="layui-btn layui-btn-sm" id="role_add" onclick="add()"><i class="layui-icon">&#xe654;</i>新增</button>
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
var curr_menu_id = window.parent.document.getElementById("curr_menu_id").value;

function reload(){
	tableInstance.reload({
		  where: { 
			  title: $("#title").val()
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
		area:["450px","550px"],
		maxmin:true,
		offset: '20%',
		content:"../notice/insert.do?" 
	});
}

 function edit(id){
	layer.open({
		type:2,
		title:"修改", 
		area:["450px","550px"],
		maxmin:true,
		offset: '20%',
		content:"../notice/to_review.do?id="+id  
	});
} 
	function del(id) {
        var res = confirm("确认要删除吗?删除之后数据不可找回")
        if (res == false){
            return;
        }
		$.ajax({
			url:"../notice/delete.do",
			type:"get",
			data:"id="+id,
			dataType:"json",
			success:function(data) {
				location.reload();
			}
		});
	}

  var oprateBtn = "<div><button class='layui-btn layui-btn-xs' id='user_update' onclick=edit('{{d.id}}')>修改</button>"
    			 +"<button class='layui-btn layui-btn-xs'  id='user_delete' onclick=del('{{d.id}}')>删除</button>" ;
	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../notice/queryData.do' //数据接口
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
				      ,{field: 'title', title: '标题', width:"15%"}
				      ,{field: 'content', title: '内容', width:"55%"}
				      ,{field: 'create_time', title: '时间', width:"15%", sort: true}
				      ,{ width:"10%",title:"操作",
				    	  templet: oprateBtn
				      } 
		      ]]
			 ,even: true //开启隔行背景
			 ,done:function(){
			    }
		  });
	});
</script>
</html>