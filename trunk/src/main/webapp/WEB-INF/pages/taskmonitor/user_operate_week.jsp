<%--
  Created by IntelliJ IDEA.
  User: lcyanxi
  Date: 2017/11/6
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<jsp:include page="../includes/header_new.jsp"></jsp:include>

<link href="/datashow/echarts3-7/css/bootstrap-reset.css" rel="stylesheet">
<link href="/datashow/echarts3-7/css/bootstrap.min.css" rel="stylesheet">
<link href="/datashow/echarts3-7/css/style-responsive.css" rel="stylesheet">
<link href="/datashow/echarts3-7/font-awesome/css/font-awesome.css" rel="stylesheet">
<!--alert提示消息-->
<link href="/datashow/echarts3-7/css/xcConfirm.css" rel="stylesheet">


<div class="bd clearfix">

    <div id="mainContainer" style="margin-left: 0px;">
        <div class="contentCol">

            <!--main content start-->
            <section id="main-content">
                <section class="wrapper">
                    <!-- page start-->
                    <div class="row">
                        <div class="col-lg-12">
                            <section class="panel">
                                <!--头部位置显示栏-->
                                <div>
                                    <button class="close" data-dismiss="modal" aria-hidden="false"></button>
                                    <h4 class="modal-title">
                                        运营部门用户字典表(周)
                                    </h4>
                                </div>
                                <!--头部位置显示栏 结束-->
                                <div>
                                    <button class="btn btn-success btn-sm" data-toggle="modal"
                                            data-target="#add_staff_myModal">
                                        <i class="icon-plus">新增用户</i>
                                    </button>
                                </div>

                                <table class="table table-striped border-top" id="sample_1">
                                    <!--头部显示所包含的内容-->
                                    <thead>
                                    <tr>
                                        <th>姓名</th>
                                        <th>用户id</th>
                                        <th>所属组</th>
                                        <th>描述</th>
                                        <th>目标菜谱</th>
                                        <th>目标作品</th>
                                        <th>目标帖子</th>
                                        <th>目标问答帖子</th>
                                        <th>目标菜单</th>
                                        <th>目标菜谱评论</th>
                                        <th>目标作品评论</th>
                                        <th>目标作品点赞</th>
                                        <th>目标帖子回复</th>
                                        <th>目标问答评论</th>
                                        <th>目标严选收入</th>
                                        <th>目标课堂收入</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <!--头部显示所包含的内容 结束-->

                                    <!--显示每个员工的详细信息-->
                                    <tbody id="tbody">
                                    <c:forEach items="${data.operator}" var="operator" varStatus="vs">
                                        <tr id="${vs.index}">
                                            <td>${operator.name}</td>
                                            <td>${operator.userid}</td>
                                            <td>${operator.group}</td>
                                            <td>${operator.workdesc}</td>
                                            <td>${operator.cooks}</td>
                                            <td>${operator.dishs}</td>
                                            <td>${operator.posts}</td>
                                            <td>${operator.questions}</td>
                                            <td>${operator.caidans}</td>
                                            <td>${operator.cook_comments}</td>
                                            <td>${operator.dish_comments}</td>
                                            <td>${operator.dish_likes}</td>
                                            <td>${operator.post_replys}</td>
                                            <td>${operator.questions_replys}</td>
                                            <td>${operator.mall}</td>
                                            <td>${operator.live}</td>
                                            <td hidden>${operator.id}</td>
                                            <td>
                                                <button class="btn btn-primary btn-xs" onclick="editInfo(${vs.index})"
                                                        data-toggle="modal" data-target="#update_staff_myModal">
                                                    <i class="icon-pencil"></i></button>
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <button class="btn btn-danger btn-xs"
                                                        onclick="delete_staff(${operator.id})">
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
                    <!-- page end-->
                </section>
            </section>
            <!--main content end-->
            </section>
            <!-- 添加员工模态框（Modal） -->
            <div class="modal fade" id="add_staff_myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <!--更新员工头部-->
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="ModalLabel">添加用户操作</h4>
                        </div>
                        <!--更新员工头部-->

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">姓名</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="name" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">用户id</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="userid" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">所属组</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="group" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">描述</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="workdesc" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标菜谱</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="cooks" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标作品</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="dishs" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标帖子</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="posts" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标问答帖子</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="questions" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标菜单</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="caidans" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标菜谱评论</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="cook_comments" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标作品评论</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="dish_comments" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标作品点赞</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="dish_likes" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标帖子回复</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="post_replys" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标问答评论</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="questions_replys" name="sphone"
                                       class="form-control" autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标严选收入</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="mall" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标课堂收入</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="live" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <!--底部按钮-->
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button id="add_staff_button" class="btn btn-primary" type="button">添加</button>
                        </div>
                        <!--底部按钮结束-->

                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div><!--添加员工 modal完-->

            <!-- 更新 员工模态框（Modal） -->
            <div class="modal fade" id="update_staff_myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <!--更新员工头部-->
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">更改用户操作</h4>
                        </div>
                        <!--更新员工头部-->

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">姓名</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_name" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">用户id</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_userid" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">所属组</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_group" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">描述</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_workdesc" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标菜谱</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_cooks" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标作品</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_dishs" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标帖子</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_posts" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标问答帖子</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_questions" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标菜单</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_caidans" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标菜谱评论</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_cook_comments" name="sphone"
                                       class="form-control" autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标作品评论</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_dish_comments" name="sphone"
                                       class="form-control" autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标作品点赞</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_dish_likes" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标帖子回复</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_post_replys" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标问答评论</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_questions_replys" name="sphone"
                                       class="form-control" autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标严选收入</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_mall" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>
                        <div class="form-group has-success">
                            <label class="col-lg-4 control-label">目标课堂收入</label>
                            <div class="col-lg-8">
                                <input type="text" placeholder="" id="up_live" name="sphone" class="form-control"
                                       autofocus>
                                <p class="help-block"></p>
                            </div>
                        </div>

                        <input hidden  id="up_id" />
                        <!--员工的角色分配 结束-->

                        <!--底部按钮-->
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-warning" onclick="update_staff()">更新</button>
                        </div>
                        <!--底部按钮结束-->

                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div><!--跟新员工 modal 完-->

        </div>
    </div>
</div>




<script src="/datashow/echarts3-7/js/bootstrap.min.js"></script>
<script src="/datashow/echarts3-7/js/xcConfirm.js"></script>
<script src="/datashow/echarts3-7/js/jquery-1.8.3.min.js"></script>



<script>
    //删除员工操作
    function delete_staff(obj) {
        var txt = "您确定要删除该员工？";
        window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.confirm, {
            onOk: function () {
                $.get("/task/deleteUserWeek.do", {id: obj}, function (data) {
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
        var id = obj+1;
        var td=document.getElementById("sample_1").rows[id];

        var name=td.cells[0].innerHTML;
        var userid=td.cells[1].innerHTML;
        var group=td.cells[2].innerHTML;
        var workdesc=td.cells[3].innerHTML;
        var cooks=td.cells[4].innerHTML;
        var dishs=td.cells[5].innerHTML;
        var posts=td.cells[6].innerHTML;
        var questions=td.cells[7].innerHTML;
        var caidans=td.cells[8].innerHTML;
        var cook_comments=td.cells[9].innerHTML;
        var dish_comments=td.cells[10].innerHTML;
        var dish_likes=td.cells[11].innerHTML;
        var post_replys=td.cells[12].innerHTML;
        var questions_replys=td.cells[13].innerHTML;
        var mall=td.cells[14].innerHTML;
        var live=td.cells[15].innerHTML;
        var id=td.cells[16].innerHTML;

        //向模态框中传值
        $('#up_name').val(name);
        $('#up_userid').val(userid);
        $('#up_group').val(group);
        $('#up_workdesc').val(workdesc);
        $('#up_cooks').val(cooks);
        $('#up_dishs').val(dishs);
        $('#up_posts').val(posts);
        $('#up_questions').val(questions);
        $('#up_caidans').val(caidans);
        $('#up_cook_comments').val(cook_comments);
        $('#up_dish_comments').val(dish_comments);
        $('#up_dish_likes').val(dish_likes);
        $('#up_post_replys').val(post_replys);
        $('#up_questions_replys').val(questions_replys);
        $('#up_mall').val(mall);
        $('#up_live').val(live);
        $('#up_id').val(id);

        $('#update_staff_myModal').modal('show');
    }


    //更新员工操作
    function update_staff() {
        //获取模态框数据
        $.ajax({
            type: "POST",
            url: "/task/updateUserWeek.do",
            data: {
                name:  $('#up_name').val(),
                userid:  $('#up_userid').val(),
                group:  $('#up_group').val(),
                workdesc: $("#up_workdesc").val(),
                cooks: $("#up_cooks").val(),
                dishs: $("#up_dishs").val(),
                posts: $("#up_posts").val(),
                questions: $("#up_questions").val(),
                caidans: $("#up_caidans").val(),
                cook_comments: $("#up_cook_comments").val(),
                dish_comments: $("#up_dish_comments").val(),
                dish_likes: $("#up_dish_likes").val(),
                post_replys: $("#up_post_replys").val(),
                questions_replys: $("#up_questions_replys").val(),
                mall: $("#up_mall").val(),
                live: $("#up_live").val(),
                id:$("#up_id").val()
            },
            dataType: 'json',
            success: function (data) {
                window.wxc.xcConfirm(data.message, window.wxc.xcConfirm.typeEnum.info, {
                    onOk: function () {
                        if(data.status=1){
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
            url: "/task/addUserWeek.do",
            type: "POST",
            async: false,
            data: {
                name: $("#name").val(),
                userid: $("#userid").val(),
                group: $("#group").val(),
                workdesc: $("#workdesc").val(),
                cooks: $("#cooks").val(),
                dishs: $("#dishs").val(),
                posts: $("#posts").val(),
                questions: $("#questions").val(),
                caidans: $("#caidans").val(),
                cook_comments: $("#cook_comments").val(),
                dish_comments: $("#dish_comments").val(),
                dish_likes: $("#dish_likes").val(),
                post_replys: $("#post_replys").val(),
                questions_replys: $("#questions_replys").val(),
                mall: $("#mall").val(),
                live: $("#live").val()
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


