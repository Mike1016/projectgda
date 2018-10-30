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
				<label class="layui-form-label">用户电话</label>
				<div class="layui-input-inline">
					<input type="text" name="phone" id="phone" lay-verify="required|phone"
						autocomplete="off" class="layui-input" id="phone" placeholder='请输入用户电话(模糊查询)'>
				</div>
			</div>
			<button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="opetion_btn">
				 <button class="layui-btn layui-btn-sm" id="user_add" onclick="add('{{d.id}}')"><i class="layui-icon">&#xe654;</i>新增</button>
				 <button class="layui-btn layui-btn-sm" id="distributions_add" onclick="distributions()"><i class="layui-icon">&#xe654;</i>全部配送</button>
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
	<span style='color:blue'>未激活</span>

	{{# } else if(d.state== 1) { }}
	<span style='color:green'>激活</span>

	{{# } else if(d.state== 2) { }}
	<span style='color:red'>封号</span>

	{{# }
	}}
</script>

<script type="text/html" id="userType">
	{{# if (d.user_type== 0) { }}
	<span style='color:blue'>注册用户</span>

	{{# } else if(d.user_type== 1) { }}
	<span style='color:green'>系统用户</span>

	{{# }
	}}
</script>

<script type="text/javascript">
var table;
var tableInstance;
function reload(){
	tableInstance.reload({
		  where: {
              phone: $("#phone").val()
		  }
		  ,page: {
		    curr: 1
		  }
		});
}

function add(btnType) {
	layer.open({
        type:2,
        title:"新增",
        area:["450px","580px"],
        maxmin:true,
        offset: '20%',
		content: "../customer/toAdd.do?btnType=add"
	});
}

function edit(id){
    layer.open({
        type:2,
        title:"修改",
        area:["350px","450px"],
        maxmin:true,
        offset: '20%',
        content:"../customer/toAdd.do?id="+id
    });
}

function del(id) {
    var res = confirm("确认要删除吗?删除之后数据不可找回")
    if (res == false){
        return;
    }
	$.ajax({
		url:"../customer/deleteData.do",
		type:"get",
		data:"id="+id,
		dataType:"json",
		success:function(data) {
			location.reload();
		}
	});
}

function prohibition(id) {
	var res = confirm("确认要封号吗?")
	if (res == false){
	    return;
	}
    $.ajax({
        url:"../customer/toAddEdit.do",
        type:"get",
        data:"id="+id+"&btnType=title",
        dataType:"json",
        success:function(data) {
            location.reload();
        }
    });
}

function relieve(id) {
    var res = confirm("确认要解封吗?")
    if (res == false){
        return;
    }
    $.ajax({
        url:"../customer/toAddEdit.do",
        type:"get",
        data:"id="+id+"&btnType=unseal",
        dataType:"json",
        success:function(data) {
            location.reload();
        }
    });
}

function activation(id) {
    var res = confirm("确认要激活吗?")
    if (res == false){
        return;
    }
    $.ajax({
        url:"../customer/toAddEdit.do",
        type:"get",
        data:"id="+id+"&btnType=activation",
        dataType:"json",
        success:function(data) {
            location.reload();
        }
    });
}

function distributions(){
    layer.open({
        type:2,
        title:"所有会员配送激活数/生命力",
        area:["350px","240px"],
        maxmin:true,
        offset: '25%',
        content:"../customer/toActivatingLife.do"
    });
}

function distribution(id){
    layer.open({
        type:2,
        title:"单个会员配送激活数/生命力",
        area:["350px","240px"],
        maxmin:true,
        offset: '25%',
        content:"../customer/toActivatingLife.do?id="+id
    });
}

	layui.use([ 'table','layer'], function() {
		table = layui.table;
		layer = layui.layer;		
		
  		var oprateBtn = "<div><button class='layui-btn layui-btn-xs' id='activation' onclick=activation('{{d.id}}')>激活</button>"
				+"<button class='layui-btn layui-btn-xs' id='prohibition' onclick=prohibition('{{d.id}}')>封号</button>"
			+"<button class='layui-btn layui-btn-xs'  id='relieve' onclick=relieve('{{d.id}}')>解封</button>"
            +"<button class='layui-btn layui-btn-xs'  id='user_update_customer' onclick=edit('{{d.id}}')>修改</button>"
            +"<button class='layui-btn layui-btn-xs'  id='user_distribution' onclick=distribution('{{d.id}}')>配送</button>"
            +"<button class='layui-btn layui-btn-xs'  id='user_delete' onclick=del('{{d.id}}')>删除</button>";
		tableInstance = table.render({
			limits:[10,20,50],
		    elem: '#demo'
		    ,height: $(window).height()*0.8
		    ,url: '../customer/queryData.do' //数据接口
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
		        ,{field: 'user_name', title: '姓名', width:"6%"}
			    ,{field: 'level', title: '等级', width:"4%"}
                ,{field: 'extension_code', title: '推荐码', width:"6%"}
			    ,{field: 'city', title: '城市', width:"4%"}
			    ,{field: 'we_chat', title: '微信', width:"5%"}
			    ,{field: 'alipay', title: '支付宝', width:"5%"}
			    ,{field: 'identity_card', title: '身份证号', width:"6%"}
			    ,{field: 'phone', title: '电话', width:"6%"}
                ,{field: 'activation_number', title: '激活数', width:"4.5%"}
                ,{field: 'wealth', title: '财富值', width:"5%"}
                ,{field: 'bonus', title: '奖金', width:"5%"}
                ,{field: 'vitality', title: '生命值', width:"4.5%"}
                ,{field: 'activity', title: '活动值', width:"4.5%"}
                ,{field: 'address', title: '收货地址', width:"5.5%"}
                ,{field: 'state', title: '状态', width:"4.5%",templet: "#state"}
                ,{field: 'regester_time', title: '注册时间', width:"7%", sort: true}
                ,{field: 'user_type', title: '用户类型', width:"7%",templet: "#userType"}
		      ,{ width:"17%",title:"操作",
		    	  templet: oprateBtn
		       }
		      ]]
			 ,even: false //开启隔行背景
			 ,done:function(res){
                for (var i=0;i<res.data.length;i++) {
                    if (res.data[i].state==1) {
                        $("[data-index='"+i+"'] [data-field='18'] #relieve").attr('disabled','disabled').css("background-color","cadetblue");
                        $("[data-index='"+i+"'] [data-field='18'] #activation").attr('disabled','disabled').css("background-color","cadetblue");
                    }else if(res.data[i].state==2){
                        $("[data-index='"+i+"'] [data-field='18'] #activation").attr('disabled','disabled').css("background-color","cadetblue");
                        $("[data-index='"+i+"'] [data-field='18'] #user_distribution").attr('disabled','disabled').css("background-color","cadetblue");
                        $("[data-index='"+i+"'] [data-field='18'] #prohibition").attr('disabled','disabled').css("background-color","cadetblue");
                    }else{
                        $("[data-index='"+i+"'] [data-field='18'] #relieve").attr('disabled','disabled').css("background-color","cadetblue");
                        $("[data-index='"+i+"'] [data-field='18'] #user_distribution").attr('disabled','disabled').css("background-color","cadetblue");
                    }
                }
			  }
		  });
	});
</script>
</html>