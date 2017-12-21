<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/12/19
  Time: 10:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header_new.jsp"></jsp:include>
<html>
<head>
    <link href="/datashow/echarts3-7/css/bootstrap-reset.css" rel="stylesheet">
    <link href="/datashow/echarts3-7/css/bootstrap.min.css" rel="stylesheet">
    <link href="/datashow/echarts3-7/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="/datashow/echarts3-7/css/style.css" rel="stylesheet">
    <!--alert提示消息-->
    <link href="/datashow/echarts3-7/css/xcConfirm.css" rel="stylesheet">
</head>
<body>
<section id="container" class="">
    <!--main content start-->
    <!-- page start-->
    <div class="row">
        <div class="col-lg-12">
            <section class="panel">
                <!--头部位置显示栏-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="false"></button>
                    <h4 class="modal-title">
                        srctype词典管理
                    </h4>
                </div>
                <!--头部位置显示栏 结束-->


                <table class="table table-striped border-top" id="sample_1">
                    <!--头部显示所包含的内容-->
                    <thead>
                    <tr>
                        <th style="width: 10px">
                            <button class="btn btn-primary btn-sm" data-toggle="modal"
                                    data-target="#add_staff_myModal">
                                <i class="icon-plus"></i>
                            </button>
                        </th>
                        <th hidden></th>
                        <th>srctype</th>
                        <th>名称</th>
                        <th>业务</th>
                        <th>创建时间</th>
                        <th>更新时间</th>
                        <th>描述</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <!--头部显示所包含的内容 结束-->

                    <!--显示srctype的详细信息-->
                    <tbody id="tbody">
                    <c:forEach items="${data}" var="serverLogSrctypeDict" varStatus="vs">
                        <tr>
                            <td></td>
                            <td hidden>${serverLogSrctypeDict.id}</td>
                            <td>${serverLogSrctypeDict.srctype}</td>
                            <td>${serverLogSrctypeDict.srctypeName}</td>
                            <td>${serverLogSrctypeDict.service}</td>
                            <td>${serverLogSrctypeDict.createTime}</td>
                            <td>${serverLogSrctypeDict.updateTime}</td>
                            <td>${serverLogSrctypeDict.sdesc}</td>
                            <td>
                                <button class="btn btn-primary btn-xs" id="${vs.index}" onclick="editInfo(this)"
                                        data-toggle="modal" data-target="#update_myModal">
                                    <i class="icon-pencil"></i></button>

                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <button class="btn btn-danger btn-xs"
                                        onclick="delete_staff(${serverLogSrctypeDict.id})">
                                    <i class="icon-trash "></i></button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <!--显示每个员工的详细信息 结束-->

                </table>
            </section>
        </div>
    </div>
    <!--main content end-->
</section>
<!-- 添加srctype模态框（Modal） -->
<div class="modal fade" id="add_staff_myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!--添加员工和悬浮显示的内容-->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title">
                    添加srctype操作
                </h4>
            </div>
            <!--添加员工和悬浮显示的内容 结束-->

            <!-- page start 添加的主要部分-->
            <div class="row">
                <div class="col-lg-12">

                    <section class="panel">
                        <div class="panel-body">
                            <form role="form" id="add_staff" class="form-horizontal tasi-form">
                                <div class="form-group has-success">
                                    <label class="col-lg-4 control-label">srctype</label>
                                    <div class="col-lg-8">
                                        <input type="text" placeholder="" id="srctype" class="form-control"
                                               autofocus>
                                        <p class="help-block"></p>
                                    </div>
                                </div>
                                <div class="form-group has-success">
                                    <label class="col-lg-4 control-label">name</label>
                                    <div class="col-lg-8">
                                        <input type="text" placeholder="" id="name" class="form-control"
                                               autofocus>
                                        <p class="help-block"></p>
                                    </div>
                                </div>
                                <div class="form-group has-success">
                                    <label class="control-label col-lg-4">业务</label>
                                    <div class="col-lg-8">
                                        <select class="form-control m-bot15" id="service">
                                            <option value="caipu">caipu</option>
                                            <option value="mall">mall</option>
                                            <option value="comment">评论</option>
                                            <option value="post">作品</option>
                                            <option value="user">用户</option>
                                            <option value="group">圈圈</option>
                                            <option value="live_class">课堂</option>
                                            <option value="other">其他</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group has-success">
                                    <label class="col-lg-4 control-label">描述</label>
                                    <div class="col-lg-8">
                                        <input type="text" placeholder="" id="sdesc" class="form-control"
                                               autofocus>
                                        <p class="help-block"></p>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-xs-6 col-lg-3">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                                        </button>
                                    </div>
                                    <div class="col-xs-6 col-lg-6">
                                        <button id="add_staff_button" class="btn btn-primary" type="button">添加</button>
                                        <h4 id="message" class="help-block text-danger text-center"></h4>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </section>
                </div>
            </div>
            <!-- page end 添加的主要部分 结束-->

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div><!--添加srctype modal完-->

<!-- 更新srctype模态框（Modal） -->
<div class="modal fade" id="update_myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!--更新srctype头部-->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    更改srctype操作
                </h4>
            </div>
            <!--更新srctype头部-->

            <div class="form-group has-success">
                <label class="col-lg-4 control-label">srctype</label>
                <div class="col-lg-8">
                    <input type="text" placeholder="" id="update_srctype" class="form-control"
                           autofocus>
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="form-group has-success">
                <label class="col-lg-4 control-label">名称</label>
                <div class="col-lg-8">
                    <input type="text" placeholder="" id="update_name" class="form-control"
                           autofocus>
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="form-group has-success">
                <label class="control-label col-lg-4">业务</label>
                <div class="col-lg-8">
                    <select class="form-control m-bot15" id="update_service">
                        <option value="caipu">caipu</option>
                        <option value="mall">mall</option>
                        <option value="comment">评论</option>
                        <option value="post">作品</option>
                        <option value="user">用户</option>
                        <option value="group">圈圈</option>
                        <option value="live_class">课堂</option>
                        <option value="other">其他</option>
                    </select>
                </div>
            </div>


            <div class="form-group has-success">
                <label class="col-lg-4 control-label">描述</label>
                <div class="col-lg-8">
                    <input type="text" placeholder="" id="update_desc" class="form-control" autofocus>
                    <p class="help-block"></p>
                </div>
            </div>

            <input hidden  id="update_id" >
            <!--底部按钮-->
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-warning" onclick="update_staff()">确定</button>
            </div>
            <!--底部按钮结束-->

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div><!--更新srctype modal 完-->

<script src="/datashow/echarts3-7/js/jquery-1.8.3.min.js"></script>
<script src="/datashow/echarts3-7/js/bootstrap.min.js"></script>
<script src="/datashow/echarts3-7/js/jquery.dataTables.min.js"></script>
<script src="/datashow/echarts3-7/js/DT_bootstrap.js"></script>
<script type="text/javascript">
    //删除员工操作
    function delete_staff(obj) {
        var txt = "您确定要删除该条数据？";
        window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.confirm, {
            onOk: function () {
                $.get("/slog/srctype/deleteSrctype.do", {id: obj}, function (data) {
                    window.wxc.xcConfirm(data.message, window.wxc.xcConfirm.typeEnum.info, {
                        onOk: function () {
                            if (data.status == 1) {
                                location.reload();
                            }
                        }
                    });
                });
            }
        });

    }


    //触发更新staff模态框的同时调用此方法  -----用于模态框传值
    function editInfo(obj) {
        //var id =obj;
        var id = $(obj).attr("id");
        // alert(id);
        //获取表格中的一行数据

        var table = $("#sample_1").DataTable();

        var srctype = table.row(id).data()[2];
        var name = table.row(id).data()[3];
        var service = table.row(id).data()[4];
        var desc = table.row(id).data()[7];
        var id = table.row(id).data()[1];

        //向模态框中传值
        $('#update_id').val(id);
        $('#update_srctype').val(srctype);
        $('#update_name').val(name);
        $('#update_service').val(service);
        $('#update_desc').val(desc);
        $('#update_myModal').modal('show');
    }


    //更新srctype操作
    function update_staff() {
        //获取模态框数据
        $.ajax({
            type: "POST",
            url: "/slog/srctype/updateSrctype.do",
            data: {
                id: $('#update_id').val(),
                srctype: $('#update_srctype').val(),
                name: $('#update_name').val(),
                service: $('#update_service').val(),
                sdesc: $("#update_desc").val()
            },
            dataType: 'json',
            success: function (data) {
                window.wxc.xcConfirm(data.message, window.wxc.xcConfirm.typeEnum.info, {
                    onOk: function () {
                        if (data.status = 1) {
                            location.reload();
                        }

                    }
                });
            }
        });
    }

    //添加员工
    $('#add_staff_button').click(function () {
        $.ajax({
            url: "/slog/srctype/addSrctype.do",
            type: "POST",
            async: false,

            data: {
                srctype: $("#srctype").val(),
                name: $("#name").val(),
                service: $("#service").val(),
                sdesc: $("#sdesc").val()
            },
            timeout: 30000,       //超时时间
            dataType: "json",     //返回的数据类型
            success: function (data) {
                window.wxc.xcConfirm(data.message, window.wxc.xcConfirm.typeEnum.info, {
                    onOk: function () {
                        if (data.status == 1) {
                            location.reload();
                        }
                    }
                });
            },
            complete: function (XMLHttpRequest, status) {
                //发送失败
                if (status == "timeout") {
                    $("#message").text("发送超时请重发");
                }
            }
        });
    });

</script>
<script src="/datashow/echarts3-7/js/dynamic-table.js"></script>
<script src="/datashow/echarts3-7/js/xcConfirm.js"></script>
</body>
</html>
