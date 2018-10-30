<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<base href="<%=basePath%>">
<!-- js -->
<script type="text/javascript"></script>
<script src="static/js/jquery-1.11.3.min.js"></script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/jquery.tips.js"></script>
<script src="static/js/area.js"></script>
<script src="static/js/zebra_dialog.js"></script>
<script src="static/js/grid.locale-cn.js"></script>
<script src="static/js/jquery.jqGrid.min.js"></script>
<script src="static/js/bootstrap-datetimepicker.min.js"></script>
<script src="static/js/comet4j.js"></script>

<!-- css -->
<link rel="stylesheet" href="static/css/bootstrap.min.css">
<link rel="stylesheet" href="static/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="static/css/jquery-ui-1.7.1.custom.css">
<link rel="stylesheet" href="static/css/ui.jqgrid.css">
</head>
<body>
<script type="text/javascript">

function startChannel(){
    var kbDom = document.getElementById('kb');
    JS.Engine.on({
    	
    	//频道监测
    	apply_notice:function(kb){
    		if(kb){
    		}
    	},
    		2 : function(kb){//侦听一个channel
    			if(kb){
    				alert(kb);
            	}
    		}
    });
    JS.Engine.start('conn');
    JS.Engine.on('start',function(cId,channelList,engine){
//     	debugger;
//     	alert("engine"+engine+"cId"+cId+"channelList"+channelList)
    	$.post("<%=basePath%>/recordUserConnID.do?connID="+cId,function(){
    	})
    });
}
</script>
</body>
</html>
