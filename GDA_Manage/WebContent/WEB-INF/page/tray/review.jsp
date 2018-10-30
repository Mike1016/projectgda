<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.daka.entry.SysUserVO" %>
<%@page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../static/layui/css/layui.css"/>
    <script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
    <title></title>
</head>

<body style="">
<form id="form">
    <input type="hidden" name="id" id="id" value="${result.data.id}">
    <div class="layui-form-item">
        <label class="layui-form-label">奖品编号</label>
        <div class="layui-input-inline">
            <input type="text" name="trayNo" id="tray_no"
                   autocomplete="off" value="${result.data.trayNo}" class="layui-input"
                   disabled="disabled">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">转盘等级</label>
        <div class="layui-input-inline">
            <input type="text" name="trayGrade" id="tray_grade"
                   autocomplete="off" value="${result.data.trayGrade}" class="layui-input"
                   disabled="disabled">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">奖品类型</label>
        <div class="layui-input-block">
            <select id="type" lay-verify="required" class="layui-input" style="width: 190px">
                <option value="1" <c:if test="${1==result.data.type}">selected="selected"</c:if>>财值富</option>
                <option value="2" <c:if test="${2==result.data.type}">selected="selected"</c:if>>生命力</option>
                <option value="3" <c:if test="${3==result.data.type}">selected="selected"</c:if>>其他实物</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">奖品数量</label>
        <div class="layui-input-inline">
            <input type="text" name="account" id="account"
                   autocomplete="off" value="${result.data.account}" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">转盘内容</label>
        <div class="layui-input-inline">
            <input type="text" name="contextImg" id="context_img"
                   autocomplete="off" value="${result.data.contextImg}" class="layui-input"
                   disabled="disabled">
        </div>
    </div>
    <div class="layui-form-item">
        <img src="${result.data.contextImg}" width="190px" hspace="110px">
        <div align="right">
            <div style="padding-right:30px">
                <button type="button" class="layui-btn" id="file">
                    <i class="layui-icon">&#xe67c;</i>上传图片
                </button>
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">奖品说明</label>
        <div class="layui-input-inline">
            <input type="text" name="prizes" id="prizes"
                   autocomplete="off" value="${result.data.prizes}" class="layui-input"><span style="color: red">例如：5000财富值，100生命力，一台电脑。</span>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">获奖比率</label>
        <div class="layui-input-inline">
            <input type="text" name="probability" id="probability"
                   autocomplete="off" value="${result.data.probability}" maxlength="7" class="layui-input"
                   onblur="checkProbability();">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">维护时间</label>
        <div class="layui-input-inline">
            <input type="text" name="createTime" id="create_time"
                   autocomplete="off" placeholder="${result.data.createTime}" class="layui-input"
                   disabled="disabled">
        </div>
    </div>

    <hr/>
    <div style="padding-right:30px">
        <input type="button" onclick="modifyPrize()" value="提交"
               class="layui-btn layui-btn-sm" style="float: right;">
        <input type="button" onclick="reset()" value="重置"
               class="layui-btn layui-btn-sm" style="float: right;">
    </div>
</form>
</body>

<script type="text/javascript" src="../static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../static/layui/layui.js"></script>
<script>
    layui.use('upload', function () {
        var upload = layui.upload;

        //执行实例
        var uploadInst = upload.render({
            elem: '#file' //绑定元素
            , url: '../tray/upload.do' //上传接口
            , done: function (data) {
                //上传完毕回调
                var v1 = data.src;
                $("#context_img").val(v1);
            }
            , error: function () {
                //请求异常回调
                alert("上传失败");
            }
        });
    });
</script>
<script type="text/javascript">
    function checkProbability() {
        var probability = $("#probability").val();
        if (probability < 0 || probability > 1) {
            alert("请输入0到1之间的数");
        }
    }

    function modifyPrize() {
        debugger;
        var id = $("#id").val();
        var trayNo = $("#tray_no").val();
        var trayGrade = $("#tray_grade").val();
        var type = $("#type option:selected").val();
        var account = $("#account").val();
        var contextImg = $("#context_img").val();
        var prizes = $("#prizes").val();
        var probability = $("#probability").val();
        var createTime = $("#create_time").val();
        var param = {
            id: id,
            trayNo: trayNo,
            trayGrade: trayGrade,
            type: type,
            account: account,
            contextImg: contextImg,
            prizes: prizes,
            probability: probability,
            createTime: createTime
        };
        var url = "${pageContext.request.contextPath }/tray/modifyTray.do";
        $.post(url, param, function (result) {
            debugger;
            if (result.status == 2) {
                alert(result.msg);
            }else{
                alert(result.msg);
                window.parent.location.reload();
                parent.layer.closeAll();
            }

        });
        return false;
    }
</script>
<
<script type="text/javascript">
    function reset() {
        $("#type").val(3);
        $("#account").val("0");
        $("#prizes").val("谢谢惠顾");
        $("#probability").val("0.000000");
    }
</script>
</html>