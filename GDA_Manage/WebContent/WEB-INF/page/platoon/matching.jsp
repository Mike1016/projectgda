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
		<div class="layui-inline" style="margin-top: 15px;">
			<label class="layui-form-label">金额</label>
			<div class="layui-input-inline">
				<input type="text" name="account" id="account" lay-verify="required|phone"
					   autocomplete="off" class="layui-input" placeholder='请输入金额'>
			</div>
		</div>
		<button class="layui-btn" style="margin-top: 5px;" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
</div>
<hr>
	<div>
		<input name="platoonId" id="platoonId" value='${result.id}' type="hidden">
		<input name="paloonAccount" id="paloonAccount" value='${result.paloonAccount}' type="hidden">
		<table id="demo"   lay-filter="test"></table>
	</div>
	<div id="pages"></div>
</body>

<script type="text/html" id="state">
	{{# if (d.state== 0) { }}
	<span style='color:blue'>待匹配</span>

	{{# } else if(d.state== 1) { }}
	<span style='color:green'>待收款</span>

	{{# } else if(d.state== 2) { }}
	<span style='color:green'>待确认</span>

	{{# } else if(d.state== 3) { }}
	<span style='color:red'>已完成</span>

	{{# }
	}}
</script>

<script type="text/html" id="type">
	{{# if (d.type== 1) { }}
	<span style='color:blue'>财富背包</span>

	{{# } else if(d.state== 2) { }}
	<span style='color:green'>奖金背包</span>

	{{# }
	}}
</script>

<script type="text/javascript">
var table;
var tableInstance;

function reload(){
    tableInstance.reload({
        where: {
            account: $("#account").val()
        }
        ,page: {
            curr: 1
        }
    });
}

function matching(id,account,orderNo) {
    var res = confirm("确认要匹配吗?")
    if (res == false){
        return;
    }
    var platoonId = $("#platoonId").val();
    var paloonAccount = $("#paloonAccount").val();
    var param = {platoonId:platoonId,id:id,orderNo:orderNo,account:account,paloonAccount:paloonAccount};
    var url = "../platoon/matching.do";
    $.post(url,param, function(result) {
        alert(result.msg);
        parent.layer.closeAll();
        window.parent.location.reload();
    });
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		
  		var oprateBtn = "<div>"
            +"<button class='layui-btn layui-btn-xs'  id='matching' onclick=matching('{{d.id}}','{{d.account}}','{{d.order_no}}')>匹配</button>";

		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../platoon/querySelloutData.do' //数据接口
		    ,response: {
		    		  statusName: 'status' //数据状态的字段名称，默认：code
		    		  ,statusCode: 200 //成功的状态码，默认：0
		    		  ,msgName: 'hint' //状态信息的字段名称，默认：msg
		    		  ,countName: 'count' //数据总数的字段名称，默认：count
		    		  ,dataName: 'data' //数据列表的字段名称，默认：data
		    		} 
		    ,page: true //开启分页
		    ,cols: [[//表头
                {field: 'id',width:"5%",type:"checkbox",fixed: 'left'}
                ,{field: 'order_no', title: '订单编号', width:"19%"}
                ,{field: 'userName', title: '用户', width:"10%"}
                ,{field: 'platoonOrderNo', title: '排单编号', width:"19%"}
                ,{field: 'type', title: '卖出类型', width:"8%",templet: "#type"}
                ,{field: 'account', title: '金额', width:"6%"}
                ,{field: 'create_time', title: '创建时间', width:"15%", sort: true}
                ,{field: 'state', title: '状态', width:"8%",templet: "#state"}
                ,{ width:"8%",title:"操作",
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