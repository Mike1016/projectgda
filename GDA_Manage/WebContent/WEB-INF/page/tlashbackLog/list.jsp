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
			<label class="layui-form-label">排单编号</label>
			<div class="layui-input-inline">
				<input type="text" name="platoonOrderNo" id="platoonOrderNo" lay-verify="required|phone"
					   autocomplete="off" class="layui-input" placeholder='请输入排单编号(模糊查询)'>
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

<script type="text/html" id="sellout_state">
	{{# if (d.sellout_state== 0) { }}
	<span style='color:blue'>待匹配</span>

	{{# } else if(d.sellout_state== 1) { }}
	<span style='color:green'>待收款</span>

	{{# } else if(d.sellout_state== 2) { }}
	<span style='color:green'>待确认</span>

	{{# } else if(d.sellout_state== 3) { }}
	<span style='color:green'>已确认</span>

	{{# } else if(d.sellout_state== 4) { }}
	<span style='color:green'>已完成</span>

	{{# }
	}}
</script>

<script type="text/html" id="platoon_state">
	{{# if (d.platoon_state== 0) { }}
	<span style='color:blue'>待匹配</span>

	{{# } else if(d.platoon_state== 1) { }}
	<span style='color:green'>待付款</span>

	{{# } else if(d.platoon_state== 2) { }}
	<span style='color:green'>待确认</span>

	{{# } else if(d.platoon_state== 3) { }}
	<span style='color:green'>已完成</span>

	{{# } else if(d.platoon_state== 4) { }}
	<span style='color:red'>已失效</span>

	{{# }
	}}
</script>
<script type="text/javascript">
var table;
var tableInstance;
function reload(){
	tableInstance.reload({
		  where: {
              platoonOrderNo: $("#platoonOrderNo").val()
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
        url:"../tlashbackLog/deleteData.do",
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
		    ,url: '../tlashbackLog/queryData.do' //数据接口
		    ,response: {
		    		  statusName: 'status' //数据状态的字段名称，默认：code
		    		  ,statusCode: 200 //成功的状态码，默认：0
		    		  ,msgName: 'hint' //状态信息的字段名称，默认：msg
		    		  ,countName: 'count' //数据总数的字段名称，默认：count
		    		  ,dataName: 'data' //数据列表的字段名称，默认：data
		    		} 
		    ,page: true //开启分页
		    ,cols: [[//表头
		        {field: 'id',type:"checkbox", width:"5%", fixed: 'left'}
			    ,{field: 'ptOrderNo', title: '排单编号', width:"20%"}
                ,{field: 'stOrderNo', title: '卖出编号', width:"20%"}
                ,{field: 'platoon_state', title: '排单状态', width:"10%",templet: "#platoon_state"}
                ,{field: 'sellout_state', title: '卖出状态', width:"10%",templet: "#sellout_state"}
                ,{field: 'platoon_account', title: '排单金额', width:"10%"}
                ,{field: 'sellout_account', title: '卖出金额', width:"10%"}
                ,{field: 'create_time', title: '创建时间', width:"14%", sort: true}
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(res){

			  }
		  });
	});
</script>
</html>