<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <title>orange</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="/syalert.min.css" />
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/syalert.min.js"></script>

    <style type="text/css">
        *{ margin:0px; padding:0px; box-sizing:border-box; }
        *:focus{ outline:none;}
        .tsm{ background:#333; font-size:13px; color:#fff; margin:20px; margin-top:0px; padding:12px; line-height:25px;}
        .tsm .p2{ margin-top:12px;}
        .btns{ padding:20px;}
        .btns div{ display:block; text-align:center; cursor:pointer; padding:10px; border-radius:5px; background:#0CC; color:#fff; margin-bottom:12px; width:100%;}
    </style>

    <script type="text/JavaScript">
        var websocket = null;

        if('WebSocket' in window){
            websocket = new WebSocket("ws://localhost:8765/websocket");
        }else{
            alert('老哥，你的浏览器不支持websocket!');
        }

        websocket.onerror = function(){
            setMessageInnerHTML("连接服务器异常");
        };

        websocket.onopen = function(event){
            setMessageInnerHTML("连接服务器成功");
            getList();
        };

        websocket.onmessage = function(){
            var message = event.data;
            message = clear(message);
            setMessageInnerHTML(message);
        };

        websocket.onclose = function(){
            setMessageInnerHTML("退出连接");
        };

        window.onbeforeunload = function(){
            websocket.close();
        };

        function setMessageInnerHTML(message){
            if("" != message) {
                document.getElementById('textarea-out').innerHTML += message + "\r\n";
            }
        }

        function closeWebSocket(){
            websocket.close();
        }

        function send(param){
            var message = document.getElementById('textarea-entry').value;
            if(message.length > 8000) {
                alert("老哥，你的输入太长了，搞小一点!");
            }else {
                var pix = "@#$&&send!@#!!!";
                /*websocket.send(pix + param + message);*/
                websocket.send(pix + message);
            }
        }

        function stop(){
            var pix = "@#$&&stop!@#!!!";
            var message = pix;
            websocket.send(message);
        }

        function clear(message){
            if("!!!@@@##clear##@@@!!!" == message) {
                document.getElementById('textarea-out').innerHTML = "";
                message ="";
            }
            return message;
        }
        
        function delDBInfo(dbId) {
            $.post("/del_db",
                    {
                        dbId:dbId
                    },
                    function(data){
                        getList();
                    }
            );
        }

        function addDB() {
            var dbName = document.getElementById('db_name').value;
            var dbUrl = document.getElementById('db_url').value;
            var dbUsername = document.getElementById('db_username').value;
            var dbPassword = document.getElementById('db_password').value;
            var driverClassName = document.getElementById('driver_class_name').value;

            $.post("/add_db",
                    {
                        dbName:dbName,
                        dbUrl:dbUrl,
                        dbUsername:dbUsername,
                        dbPassword:dbPassword,
                        driverClassName:driverClassName
                    },
                    function(data){
                        var result = data;
                        if(result == true) {
                            document.getElementById('add_db_return_message').innerHTML = "<p style=\"color:green\">添加成功</p>";
                            syalert.syhide('addDB_div');
                        } else{
                            document.getElementById('add_db_return_message').innerHTML = "<p style=\"color:red\">添加失败</p>";
                        }
                        getList();
                    }
            );
        }

        function getList() {
            $.post("/db_list",
                    {},
                    function(data){
                        document.getElementById('db_list').innerHTML = "";
                        var dbList=eval(data);
                        for(var i=0; i<dbList.length; i++){
                            var dbInfo = dbList[i];
                            var status = dbInfo.status;
                            var color;
                            if(status == 1) {
                                color = "green";
                            } else {
                                color = "red";
                            }
                            document.getElementById('db_list').innerHTML +=
                                    "<li class=\"list-group-item\" style=\"color: "+color+"\"><span style=\"float: right;\"><a href='javascript:alert(\"编辑功能还没实现\")'>编辑</a> | <a href='javascript:delDBInfo(" + dbInfo.dbId + ");'>删除</a></span>" + dbInfo.dbName + "</li>";
                        }
                    }
            );
        }

        $(document).ready(function(){
            $("#dbRefresh").click(function(){
                getList();
            });
        });
    </script>

    <style>
        .fakeimg {
            height: 200px;
            background: #aaa;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-sm-4">
                <div class="well well-lg">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-6">
                                    <span>
                                        <button type="button" class="btn btn-success" id="addDB" id="submitBTN" onclick="syalert.syopen('addDB_div')">添加数据源</button>
                                    </span>
                                </div>
                                <div class="col-xs-6 text-right">
                                    <span>
                                        <button type="button" class="btn btn-success" id="dbRefresh"><span class="glyphicon glyphicon-send"></span> 刷新</button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <ul id="db_list" class="list-group">
                                <#--<li class="list-group-item"><span style="float: right;"><a>编辑</a> | <a>删除</a></span>TEST-DATABASE-1</li>-->
                            </ul>
                            <hr class="hidden-sm hidden-md hidden-lg">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-8">
                <dev>
                    <div class="well well-lg">
                        <form role="form" id="sql-form">
                            <div class="form-group">
                                <textarea class="form-control" id="textarea-entry" rows="15" maxlength="8000" placeholder="请输入执行语句"></textarea>
                            </div>
                            <button type="button" id="run" class="btn btn-info" onclick="send('${params.session_id}')">运行</button>
                            <button type="button" id="stop" class="btn btn-danger" onclick="alert('停止功能还没实现')">停止</button>
                        </form>
                    </div>
                    <div class="panel panel-default">
                        <div id="run-result" class="panel-body">
                            <textarea class="form-control" id="textarea-out" rows="15" disabled="true"></textarea>
                        </div>
                    </div>
                </dev>
            </div>
        </div>
    </div>

    <div class="sy-alert sy-alert-model animated" sy-enter="zoomIn" sy-leave="zoomOut" sy-type="confirm" sy-mask="true" id="addDB_div">
        <div class="sy-title">添加数据源</div>
        <div class="sy-content">
            <div class="form">
                <p class="input-item"><input id="db_name" type="text" placeholder="DB_NAME" /></p>
                <p class="input-item"><input id="db_url" type="text" placeholder="URL" /></p>
                <p class="input-item"><input id="driver_class_name" type="text" placeholder="DRIVER_CLASS_NAME" /></p>
                <p class="input-item"><input id="db_username" type="text" placeholder="USERNAME" /></p>
                <p class="input-item"><input id="db_password" type="text" placeholder="PASSWORD" /></p>
                <p id="add_db_return_message"></p>
            </div>
        </div>
        <div class="sy-btn">
            <button onclick="syalert.syhide('addDB_div')">取消</button>
            <button onclick="addDB()">确定</button>
        </div>
    </div>
</body>
</html>