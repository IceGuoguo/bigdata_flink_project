<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>系统入口</title>
    <!--引入样式主题-->
    <link rel="stylesheet" href="statics/easyui/themes/bootstrap/easyui.css">
    <!--引入图标样式-->
    <link rel="stylesheet" href="statics/easyui/themes/icon.css">
    <!--引入jQuery EasyUI 插件 -->
    <script src="statics/jquery.min.js"></script>
    <script src="statics/easyui/jquery.easyui.min.js"></script>
    <script src="statics/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script src="statics/jquery.extd.js"></script>
    <script src="statics/jquery.cookie.js"></script>
    <script>
        $(function () {
            //用户登陆页面
            $("#user_login_dialog").dialog({
                title:"用户登陆",
                iconCls:"icon-logo",
                top:120,
                width:300,
                modal:true,
                draggable:false,
                closable:false,
                closed:false,
                cache:false,
                href:"commons/login/loginForm.html",
                buttons:[{
                    text:"登陆",
                    iconCls:"icon-ok",
                    plain:true,
                    handler:function(){
                        $("#user_login_form").form("submit",{
                            url:"bingApp/userLogin",
                            onSubmit: function(){
                                if(!$("#user_login_form").form("validate")){
                                    $.messager.alert("表单提示","请检查您的表单输入")
                                    return false
                                }
                                $.messager.progress();
                                //开始生成cookies inputVector
                                var vectors = $("#user_login_form").data("inputVector");
                                $.cookie("inputVector",vectors.join(","))
                                return true
                            },
                            success:function(data){
                                $.messager.progress('close');	// 如果提交成功则隐藏进度条

                                if(data){
                                    alert(data)
                                    var error = eval('(' + data + ')');
                                    $.messager.alert("登陆出错！","错误信息："+error.erroCode+",提示:"+error.message)
                                }else{
                                    window.location.href="manager.jsp"
                                }
                            }
                        })
                    }
                },{
                    text:'重置',
                    iconCls:"icon-redo",
                    plain:true,
                    handler:function(){
                        $("#user_login_form").form("reset")
                    }
                }],
                toolbar:[{
                    text:'用户注册',
                    plain:true,
                    handler:function(){
                        $("#user_login_dialog").dialog("close")
                        $("#user_register_dialog").dialog("open")
                    }
                }],
                onLoad:function () {
                    $("#user_login_form").actions({
                        vectorName:"inputVector"
                    })
                }
            })

            //用户注册页面
            $("#user_register_dialog").dialog({
                title:"用户注册",
                iconCls:"icon-logo",
                top:120,
                width:300,
                modal:true,
                draggable:false,
                closable:false,
                closed:true,
                cache:false,
                href:"commons/login/registerForm.html",
                buttons:[{
                    text:"注册",
                    iconCls:"icon-ok",
                    plain:true,
                    handler:function(){

                        $("#user_rigister_form").form("submit",{
                            url:"bingApp/register",
                            onSubmit: function(){
                                if(!$("#user_rigister_form").form("validate")){
                                    $.messager.alert("表单提示","请检查您的表单输入")
                                    return false
                                }
                                $.messager.progress();
                                return true
                            },
                            success:function(data){
                                $.messager.progress('close');	// 如果提交成功则隐藏进度条
                                if(data){
                                    var error = eval('(' + data + ')');
                                    console.log(error)
                                    $.messager.alert("注册出错！","错误信息："+error.erroCode+",提示:"+error.message)
                                }else{
                                    window.location.href="manager.jsp"
                                }
                            }
                        })
                    }
                },{
                    text:'重置',
                    iconCls:"icon-redo",
                    plain:true,
                    handler:function(){
                        $("#user_rigister_form").form("reset")
                    }
                }],
                toolbar:[{
                    text:'用户登陆',
                    plain:true,
                    handler:function(){
                        $("#user_register_dialog").dialog("close")
                        $("#user_login_dialog").dialog("open")
                    }
                }]
            })

        })
    </script>
</head>
<body style="background-color: #3f3f3f">
    <!--登陆Dialog-->
    <div id="user_login_dialog">
    </div>
    <div id="user_register_dialog">
    </div>
</body>