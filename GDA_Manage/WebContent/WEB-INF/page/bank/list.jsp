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
				<label class="layui-form-label">持卡人</label>
				<div class="layui-input-inline">
					<input type="text" name="cardHolder" id="cardHolder" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="cardHolder" placeholder='请输入持卡人(模糊查询)'>
				</div>
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="opetion_btn">
				 <button class="layui-btn layui-btn-sm" id="user_add" onclick="add()"><i class="layui-icon">&#xe654;</i>新增</button>
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
              cardHolder: $("#cardHolder").val()
		  }
		  ,page: {
		    curr: 1
		  }
		});
}

function add(){
    layer.open({
        type:2,
        title:"修改",
        area:["350px","450px"],
        maxmin:true,
        offset: '20%',
        content:"../bank/toAdd.do"
    });
}

function del(id) {
    var res = confirm("确认要删除吗?删除之后数据不可找回")
    if (res == false){
        return;
    }
	$.ajax({
		url:"../bank/deleteData.do",
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
		
  		var oprateBtn ="<div>"
            +"<button class='layui-btn layui-btn-xs'  id='user_delete' onclick=del('{{d.id}}')>删除</button>";

  		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../bank/queryData.do' //数据接口
		    ,response: {
		    		  statusName: 'status' //数据状态的字段名称，默认：code
		    		  ,statusCode: 200 //成功的状态码，默认：0
		    		  ,msgName: 'hint' //状态信息的字段名称，默认：msg
		    		  ,countName: 'count' //数据总数的字段名称，默认：count
		    		  ,dataName: 'data' //数据列表的字段名称，默认：data
		    		} 
		    ,page: true //开启分页
		    ,cols: [[//表头
		        {field: 'id',type:"checkbox", width:"2%", sort: true, fixed: 'left'}
		        ,{field: 'userName', title: '帐号', width:"10%"}
			    ,{field: 'bankName', title: '银行名称', width:"30%"}
			    ,{field: 'cardHolder', title: '持卡人', width:"10%"}
			    ,{field: 'cardNumber', title: '银行卡卡号', width:"30%"}
		      ,{ width:"18%",title:"操作",
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