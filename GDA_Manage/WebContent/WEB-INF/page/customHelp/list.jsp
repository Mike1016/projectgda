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

	<span id="opetion_btn">
		<button class="layui-btn layui-btn-sm" id="save" onclick="save()"><i class="layui-icon">&#xe654;</i>新增</button>
	</span>
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
        area:["350px","400px"],
        maxmin:true,
        offset: '20%',
        content:"../customHelp/toAdd.do"
    });
}

function edit(id,img_path){
    layer.open({
        type:2,
        title:"修改",
        area:["350px","400px"],
        maxmin:true,
        offset: '20%',
        content:"../customHelp/toAdd.do?id="+id+"&imgPath="+img_path
    });
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		
  		var oprateBtn = "<div>"
            +"<button class='layui-btn layui-btn-xs'  id='user_update_customer' onclick=edit('{{d.id}}','{{d.img_path}}')>修改</button>";

		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../customHelp/queryData.do' //数据接口
		    ,response: {
		    		  statusName: 'status' //数据状态的字段名称，默认：code
		    		  ,statusCode: 200 //成功的状态码，默认：0
		    		  ,msgName: 'hint' //状态信息的字段名称，默认：msg
		    		  ,countName: 'count' //数据总数的字段名称，默认：count
		    		  ,dataName: 'data' //数据列表的字段名称，默认：data
		    		} 
		    ,page: true //开启分页
		    ,cols: [[//表头
		        {field: 'id',type:"checkbox", width:"10%", sort: true, fixed: 'left'}
		        ,{field: 'img_path', title: '图片路径', width:"40%",align:'center'}
			    ,{field: 'create_time', title: '创建时间', width:"40%", sort: true,align:'center'}
		      ,{ width:"10%",title:"操作",
		    	  templet: oprateBtn,align:'center'
		       }
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(res){
                for (var i=0;i<res.data.length;i++) {
                    debugger;
                    $("[data-index='"+i+"'] [data-field='img_path'] div").remove();
                    $("[data-index='"+i+"'] td").css("height","80px");
                    $("[data-index='"+i+"'] [data-field='img_path']").append("<div class='layui-table-cell laytable-cell-1-img_path' align='center' style='height:60px;'><img src='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>"+res.data[i].img_path+"' /></div>");
                }
			  }
		  });
	});
</script>
</html>