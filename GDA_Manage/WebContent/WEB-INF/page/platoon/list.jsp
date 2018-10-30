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
					<input type="text" name="orderNo" id="orderNo" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="orderNo" placeholder='请输入订单编号(模糊查询)'>
				</div>
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="opetion_btn">
				<button class="layui-btn" onclick="openMatching()">启动匹配</button>
				<button class="layui-btn" onclick="closeMatching()">关闭匹配</button>
			 </span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			匹配控时(开始):
			<div class="layui-inline"> <!-- 注意：这一层元素并不是必须的 -->
				<input type="text" class="layui-input" id="test1">
			</div>
			匹配控时(结束):
			<div class="layui-inline"> <!-- 注意：这一层元素并不是必须的 -->
				<input type="text" class="layui-input" id="test2">
			</div>
			<button class="layui-btn" onclick="matchingTimeControl()">匹配控时</button>
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
	<span style='color:blue'>待匹配</span>

	{{# } else if(d.state== 1) { }}
	<span style='color:green'>待付款</span>

	{{# } else if(d.state== 2) { }}
	<span style='color:green'>待确认</span>

	{{# } else if(d.state== 3) { }}
	<span style='color:red'>已完成</span>

	{{# } else if(d.state== 4) { }}
	<span style='color:red'>已失效</span>

	{{# }
	}}
</script>

<script type="text/html" id="status">
	{{# if (d.status== 0) { }}
	<span style='color:blue'>未冻结</span>

	{{# } else if(d.status== 1) { }}
	<span style='color:red'>冻结</span>

	{{# } else if(d.status== 2) { }}
	<span style='color:green'>解冻</span>

	{{# }
	}}
</script>

<script>
    layui.use('laydate', function(){
        var laydate = layui.laydate;

        //执行一个laydate实例
        laydate.render({
            elem: '#test1' //指定元素
            ,type: 'time'
        });

        //执行一个laydate实例
        laydate.render({
            elem: '#test2' //指定元素
            ,type: 'time'
        });
    });
</script>

<script type="text/javascript">
var table;
var tableInstance;
function reload(){
	tableInstance.reload({
		  where: {
              orderNo: $("#orderNo").val()
		  }
		  ,page: {
		    curr: 1
		  }
		});
}

//关闭匹配
function closeMatching() {
    var res = confirm("确认要关闭匹配吗?")
    if (res == false){
        return;
    }
    $.ajax({
        url:"../platoon/closeMatching.do",
        type:"get",
        dataType:"json",
        success:function(data) {
            location.reload();
            alert(data.msg);
        }
    });
}

//开启匹配
function openMatching() {
    var res = confirm("确认要开启匹配吗?")
    if (res == false){
        return;
    }
    $.ajax({
        url:"../platoon/openMatching.do",
        type:"get",
        dataType:"json",
        success:function(data) {
            location.reload();
            alert(data.msg);
        }
    });
}

function edit(id,state) {
    $.ajax({
        url:"../platoon/toFlashBack.do",
        type:"get",
        data:"id="+id+"&state="+state,
        dataType:"json",
        success:function(data) {
            location.reload();
            alert(data.msg);
        }
    });
}

function matching(id,account){
    layer.open({
        type:2,
        title:"手动匹配",
        area:["1150px","650px"],
        maxmin:true,
        offset: '10%',
        content:"../platoon/toMatching.do?id="+id+"&account="+account
    });
}

//匹配控时
function matchingTimeControl() {
    var stat = $("#test1").val();
    var end = $("#test2").val();
    if (stat == null || end == null || stat == "" || end == ""){
        alert("开始或结束时间不能为空")
		return;
	}
    $.ajax({
        url:"../platoon/matchingTimeControl.do",
        type:"get",
        data:"stat="+stat+"&end="+end,
        dataType:"json",
        success:function(data) {
            location.reload();
            alert(data.msg);
        }
    });
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		
  		var oprateBtn = "<div>"
            +"<button class='layui-btn layui-btn-xs'  id='user_update_customer' onclick=edit('{{d.id}}','{{d.state}}')>交易回溯</button>"
            +"<button class='layui-btn layui-btn-xs'  id='matching' onclick=matching('{{d.id}}','{{d.account}}')>手动匹配</button>";

		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../platoon/queryData.do' //数据接口
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
		        ,{field: 'order_no', title: '订单编号', width:"11%"}
			    ,{field: 'userName', title: '用户', width:"7%"}
                ,{field: 'account', title: '金额', width:"8%"}
                ,{field: 'surplusAccount', title: '剩余金额', width:"8%"}
                ,{field: 'create_time', title: '创建时间', width:"10%", sort: true}
                ,{field: 'match_time', title: '匹配时间', width:"10%", sort: true}
                ,{field: 'payment_time', title: '收款时间', width:"10%", sort: true}
                ,{field: 'confirm_time', title: '成交时间', width:"10%", sort: true}
                ,{field: 'state', title: '状态', width:"5%",templet: "#state"}
                ,{field: 'status', title: '状态', width:"5%",templet: "#status"}
		      ,{ width:"11%",title:"操作",
		    	  templet: oprateBtn
		       }
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(res){
                for (var i=0;i<res.data.length;i++) {
                    if (res.data[i].state != 0 || res.data[i].status != 0) {
                        $("[data-index='"+i+"'] [data-field='11'] #matching").attr('disabled','disabled').css("background-color","cadetblue");
                    }
                }
			  }
		  });
	});
</script>

</html>