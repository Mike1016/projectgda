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
				<label class="layui-form-label">类型</label>
				<div class="layui-input-inline">
					<input type="text" name="type" id="type" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="type" placeholder='请输入类型(模糊查询)'>
				</div>
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="opetion_btn">
				<button class="layui-btn layui-btn-sm" id="save" onclick="save()"><i class="layui-icon">&#xe654;</i>新增</button>
				<button type="button" class="layui-btn" id="upload"><i class="layui-icon"></i>上传LOGO</button>
			 </span>
		</div>
	</div>
	<hr>
	<div>
<table id="demo"   lay-filter="test"></table>
	</div>
	<div id="pages"></div>
</body>

<script>
    layui.use('upload', function(){
        var $ = layui.jquery
            ,upload = layui.upload;

        //普通图片上传
        var uploadInst = upload.render({
            elem: '#upload'
            ,url: '../dictionaries/upload.do'
            ,done: function(res){
                layer.msg(res.msg);
            }
        });
    });
</script>
<script type="text/javascript">
var table;
var tableInstance;
function reload(){
	tableInstance.reload({
		  where: {
              type: $("#type").val()
		  }
		  ,page: {
		    curr: 1
		  }
		});
}

function save(){
    layer.open({
        type:2,
        title:"新增",
        area:["350px","300px"],
        maxmin:true,
        offset: '20%',
        content:"../dictionaries/toAdd.do"
    });
}

function edit(id,type){
    debugger;
    if (type == "LOGO"){
        alert("请点击<上传LOGO>按钮上传logo")
		return;
	}
    layer.open({
        type:2,
        title:"修改",
        area:["75%","70%"],
        maxmin:true,
        offset: '10%',
        content:"../dictionaries/toAdd.do?id="+id
    });
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		
  		var oprateBtn = "<div>"
            +"<button class='layui-btn layui-btn-xs'  id='user_update_customer' onclick=edit('{{d.id}}','{{d.type}}')>修改</button>";

		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../dictionaries/queryData.do' //数据接口
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
		        ,{field: 'type', title: '类型', width:"15%"}
			    ,{field: 'parameter', title: '参数', width:"15%"}
			    ,{field: 'remarks', title: '备注', width:"55%"}
		      ,{ width:"10%",title:"操作",
		    	  templet: oprateBtn
		       }
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(res){

			  }
		  });
	});
</script>
</html>