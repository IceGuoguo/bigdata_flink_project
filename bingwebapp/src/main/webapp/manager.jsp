<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>欢迎${user.name}主页</title>
    <!--引入样式主题-->
    <link rel="stylesheet" href="statics/easyui/themes/default/easyui.css">
    <!--引入图标样式-->
    <link rel="stylesheet" href="statics/easyui/themes/icon.css">
    <!--引入jQuery EasyUI 插件 -->
    <script src="statics/jquery-1.8.3.min.js"></script>
    <script src="statics/easyui/jquery.easyui.min.js"></script>
    <script src="statics/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script src="statics/highcharts.js"></script>

    <script src="https://img.hcharts.cn/highmaps/modules/map.js"></script>
    <script src="statics/china.js"></script>
</head>
<body class="easyui-layout">
        <div data-options="region:'north',height:60">
            <h2>
               欢迎${user.name}登陆,<a href="/logout" class="easyui-linkbutton" >退出登录</a>
            </h2>
        </div>
        <div data-options="region:'south',height:30"></div>
        <div id="left" data-options="region:'west',width:120,title:'操作栏',hideCollapsedContent:false,href:'${pageContext.request.contextPath}/commons/left.html'">

        </div>
        <div data-options="region:'center',width:120,border:false">
            <div id="center_content_tabs" class="easyui-tabs" data-options="border:false,fit:true" style="background-color: #F4F4F4"></div>
        </div>
</body>