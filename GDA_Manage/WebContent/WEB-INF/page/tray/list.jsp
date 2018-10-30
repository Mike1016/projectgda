<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>主页</title>
    <link rel="stylesheet" href="../static/layui/css/layui.css"/>
    <link rel="stylesheet" href="../static/css/common.css"/>
    <script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="../static/layui/layui.js"></script>
</head>
<body>
<!--条件查询-->
<div class="condition">
    <div class="layui-form-item">
        <div class="layui-inline">
            <div class="layui-form-item">
                <label class="layui-form-label">转盘等级</label>
                <div class=" layui-input-inline">
                    <select id="Grade" lay-verify="required" class="layui-input" style="width: 170px">
                        <option value="">请选择等级</option>
                        <option value="1">3级</option>
                        <option value="2">6级</option>
                        <option value="3">9级</option>
                    </select>
                </div>
                <button class="layui-btn" onclick="reload()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

            </div>
        </div>
    </div>
</div>
<hr>
<div>
    <table id="demo" lay-filter="test"></table>
</div>
<div id="pages"></div>
</body>


<script type="text/html" id="trayGrade">
    {{# if (d.trayGrade==1) { }}
    <span>小转盘</span>

    {{# } else if(d.trayGrade== 2) { }}
    <span>中转盘</span>

    {{# } else if(d.trayGrade== 3) { }}
    <span>大转盘</span>
    {{# }
    }}

</script>
<script type="text/html" id="type">
    {{# if (d.type==1) { }}
    <span>财富值</span>

    {{# } else if(d.type== 2) { }}
    <span>生命力</span>

    {{# } else if(d.type== 3) { }}
    <span>其他实物</span>
    {{# }
    }}

</script>


<script type="text/javascript">
    var table;
    var tableInstance;

    function reload() {
        tableInstance.reload({
            where: {
                trayGrade: $("#Grade option:selected").val()
            }
            , page: {
                curr: 1
            }
        });
    }


    function edit(id, url) {
        var url = "${pageContext.request.contextPath }/tray/queryDataById.do";
        layer.open({
            type: 2,
            title: "修改",
            area: ["600px", "720px"],
            maxmin: true,
            offset: '20%',
            content: url + "?id=" + id
        });
    }

    layui.use(['table', 'layer'], function () {
        table = layui.table;
        layer = layui.layer;

        var oprateBtn = "<div><button class='layui-btn layui-btn-xs' id='tray_update' onclick=edit('{{d.id}}')>修改</button>";
        tableInstance = table.render({
            limits: [10, 20, 50],
            elem: '#demo'
            , height: $(window).height() * 0.7
            , url: '${pageContext.request.contextPath }/tray/querydata.do' //数据接口
            , response: {
                statusName: 'status' //数据状态的字段名称，默认：code
                , statusCode: 200 //成功的状态码，默认：0
                , msgName: 'hint' //状态信息的字段名称，默认：msg
                , countName: 'count' //数据总数的字段名称，默认：count
                , dataName: 'data' //数据列表的字段名称，默认：data
            }
            , page: true //开启分页
            , cols: [[//表头
                {field: 'id', type: "checkbox", width: "5%", sort: true, fixed: 'left'}
                , {field: 'trayNo', title: '奖品编号', width: "11%"}
                , {field: 'trayGrade', title: '转盘等级', width: "12%", templet: "#trayGrade"}
                , {field: 'type', title: '奖品类型 ', width: "12%",templet: "#type"}
                , {field: 'account', title: '奖品数量', width: "12%"}
                , {field: 'prizes', title: '奖品说明', width: "12%"}
                , {field: 'probability', title: '获奖比率', width: "12%"}
                , {field: 'createTime', title: '维护时间', width: "12%"}
                , {
                    width: "12%", title: "操作",
                    templet: oprateBtn
                }
            ]]
            , even: false //开启隔行背景
            , done: function () {
            }
        });
    });
</script>
</html>