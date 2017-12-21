<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>jqGrid Demos</title>
    <link rel="stylesheet" type="text/css" media="screen"
          href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
    <link rel="stylesheet" type="text/css" media="screen"
          href="/jqgrid/themes/ui.jqgrid.css" />
    <link rel="stylesheet" type="text/css" media="screen"
          href="/jqgrid/themes/ui.multiselect.css" />
    <style>
        html,body {
            margin: 0; /* Remove body margin/padding */
            padding: 0;
            overflow: hidden; /* Remove scroll bars on browser window */
            font-size: 75%;
        }
    </style>
    <script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
    <script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js"
            type="text/javascript"></script>
    <script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
    <script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>

    <script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
    <script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
    <script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
    <script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript" src="/scripts/My97DatePicker/WdatePicker.js"></script>
</head>
<body>

<div class="selectedSort">
    Tag_Name：<?php echo $cookinfo['name']; ?></a>
    <div id="sSort">
        <div class="box">
            <div class="chname"></div>
            <ul class="chcheck">
                <li>主要主料</li>
                <li>次要主料</li>
                <li>辅料</li>
                <li>调味料</li>
            </ul>
        </div>
        <?php foreach($ingredients as $c) { ?>
        <div class="box" data-level="<?php echo $c['level']; ?>">
            <div class="chname" title="<?php echo $c['id']; ?>" data="<?php echo $c['parentid'] ?>" ><?php echo $c['name'] ?></div>
            <ul class="chcheck" title="<?php echo $c['type'] ?>">
                <li ><input type="radio" name="<?php echo $c['id']; ?>" value="<?php echo $c['id']; ?>_1" <?php echo $c['type']==1?"checked":"" ?> /></li>
                <li ><input type="radio" name="<?php echo $c['id']; ?>" value="<?php echo $c['id']; ?>_2" <?php echo $c['type']==2?"checked":"" ?> /></li>
                <li ><input type="radio" name="<?php echo $c['id']; ?>" value="<?php echo $c['id']; ?>_3" <?php echo $c['type']==3?"checked":"" ?> /></li>
                <li ><input type="radio" name="<?php echo $c['id']; ?>" value="<?php echo $c['id']; ?>_4" <?php echo $c['type']==4?"checked":"" ?> /></li>
            </ul>
        </div>
        <?php } ?>
    </div>
</div>
<!-- 不可编辑字段 -->
<div class="chbkbj">
    <?php foreach($nonstd as $c) { ?>
    <p style="padding: 0;"><?php echo $c; ?></p>
    <?php }?>
</div>
<div class="wareSort clearfix">
    <input type="text" value="" placeholder="搜索食材标签" name="first" class="searchsc">
    <div class="classifyArea">
        <a href="javascript:void:(0)">一级</a>
    </div>
    <div class="classifyArea">
        <a href="javascript:void:(0)">二级</a>
    </div>
    <div class="classifyArea">
        <a href="javascript:void:(0)">三级</a>
    </div>
    <div class="classifyArea">
        <a href="javascript:void:(0)">四级</a>
    </div>

    <ul id="iort1" class="classifyArea" ></ul>
    <ul id="iort2" class="classifyArea" style="display: none;"></ul>
    <ul id="iort3" class="classifyArea" style="display: none;"></ul>
    <ul id="iort4" class="classifyArea" style="display: none;"></ul>
</div>

<script type="text/javascript">
    var fstlevel = 1;
    var seclevel = 1;
    var trdlevel = 1;
    var forlevel = 1;
    var cateIngre = 1;

    function in_array(stringToSearch, arrayToSearch) {
        for (s = 0; s < arrayToSearch.length; s++) {
            thisEntry = arrayToSearch[s].toString();
            if (thisEntry == stringToSearch) {
                return true;
            }
        }
        return false;
    }
    /*初始化一级目录*/
    function intProvince() {
        areaCont = "";
        for (var i = 0; i < cateIngre.length; i++) {
            areaCont += '<li';
            if (in_array(cateIngre[i].id, fstlevel)) {
                areaCont += ' class="appid" ';
            }
            areaCont += ' data =' + cateIngre[i].id + '  ><a  onClick="selectsP(' + i + '); " href="javascript:void(0)">' + cateIngre[i].name + '</a></li>';
        }
        $("#iort1").html(areaCont);
    }
    intProvince();
    function isEmptyObject(e) {
        var t;
        for (t in e)
            return !1;
        return !0
    }
    /*选择一级目录*/
    function selectsP(p) {
        $("#iort1 li").eq(p).addClass("active").siblings("li").removeClass("active");
        areaCont = "";
        $("#iort3").hide();
        $("#iort4").hide();
        if (isEmptyObject(cateIngre[p]['child'])) {
            $("#iort2").hide();
            return;
        } else {
            for (var j = 0; j < cateIngre[p].child.length; j++) {
                areaCont += '<li';
                if (in_array(cateIngre[p]['child'][j].id, seclevel)) {
                    areaCont += ' class="appid" ';
                }
                areaCont += ' data =' + cateIngre[p]['child'][j].id + '  ><a  onClick="selectsC(' + p + ',' + j + '); " href="javascript:void(0)">' + cateIngre[p]['child'][j].name + '</a><input type="checkbox" class="scheck" ';
                if ($("div[title=" + cateIngre[p]['child'][j].id + "]").length > 0) {
                    areaCont += " checked ";
                }
                areaCont += 'value="' + cateIngre[p]['child'][j].id + '" data="' + cateIngre[p]['child'][j].parentid + '" onChange="selectsC(' + p + ',' + j + '); "></li>';
            }
            $("#iort2").html(areaCont).show();
        }
    }
    /*选择二级目录*/
    function selectsC(p, c) {
        $("#iort2 li").eq(c).addClass("active").siblings("li").removeClass("active");
        $("#iort4").hide();
        areaCont = "";
        if (isEmptyObject(cateIngre[p]['child'][c]['child'])) {
            $("#iort3").hide();
            return;
        } else {
            for (var k = 0; k < cateIngre[p]['child'][c]['child'].length; k++) {
                areaCont += '<li';
                if (in_array(cateIngre[p]['child'][c]['child'][k].id, trdlevel)) {
                    areaCont += ' class="appid" ';
                }
                areaCont += ' data =' + cateIngre[p]['child'][c]['child'][k].id + '  ><a  onClick="selectsD(' + p + ',' + c +','+ k +'); " href="javascript:void(0)">' + cateIngre[p]['child'][c]['child'][k].name + '</a><input type="checkbox" class="scheck" ';
                if ($("div[title=" + cateIngre[p]['child'][c]['child'][k].id + "]").length > 0) {
                    areaCont += " checked ";
                }
                areaCont += 'value="' + cateIngre[p]['child'][c]['child'][k].id + '" data="' + cateIngre[p]['child'][c]['child'][k].parentid + '" onChange="selectsD(' + p + ',' + c + ','+ k +'); "></li>';
            }
            $("#iort3").html(areaCont).show();
        }
    }
    /*选择三级目录*/
    function selectsD(p, c, d) {
        $("#iort3 li").eq(d).addClass("active").siblings("li").removeClass("active");
        areaCont = "";
        expressC = "";
        expressD = "";
        if (isEmptyObject(cateIngre[p]['child'][c]['child'][d]['child'])) {
            $("#iort4").hide();
            return;
        } else {
            for (var k = 0; k < cateIngre[p]['child'][c]['child'][d]['child'].length; k++) {
                areaCont += '<li';
                console.log(in_array(cateIngre[p]['child'][c]['child'][d]['child'][k].id, forlevel));
                if (in_array(cateIngre[p]['child'][c]['child'][d]['child'][k].id, forlevel)) {
                    areaCont += ' class="appid" ';
                }
                areaCont += ' data =' + cateIngre[p]['child'][c]['child'][d]['child'][k].id + '" ><a onClick="selectsF(' + p + ',' + c + ',' + d + ',' + k + ');" href="javascript:void(0)">' + cateIngre[p]['child'][c]['child'][d]['child'][k].name + '</a><input type="checkbox" ';
                if ($("div[title=" + cateIngre[p]['child'][c]['child'][d]['child'][k].id + "]").length > 0) {
                    areaCont += " checked ";
                }
                areaCont += 'class="scheck" value="' + cateIngre[p]['child'][c]['child'][d]['child'][k].id + '"  data="' + cateIngre[p]['child'][c]['child'][d]['child'][k].parentid + '" onChange="selectsF(' + p + ',' + c + ',' + d + ',' + k + ');"></li>';
            }
            $("#iort4").html(areaCont).show();
        }
    }
    // 选择四级目录
    function selectsF(p, c, d, f) {
        $("#iort4 li").eq(f).addClass("active").siblings("li").removeClass("active");
    }

    // 菜谱选择标签
    $(".scheck").live('click', function () {
        var _true = $(this).attr('checked');
        var xid = $(this).val();
        var pid = $(this).attr('data');
        var fnum = false;
        if (_true) {
            var areaCont = '';
            areaCont = '<div class="box"><div class="chname" title="' + xid + '" data="' + pid + '">' + $(this).siblings('a').text() + '</div><ul class="chcheck"><li><input type="radio" name="' + xid + '" value="' + xid + '_1" /></li><li><input type="radio" name="' + xid + '" value="' + xid + '_2"/></li><li><input type="radio" name="' + xid + '" value="' + xid + '_3" /></li><li><input type="radio" name="' + xid + '" value="' + xid + '_4" /></li></div>';
            $("#sSort").append(areaCont);
        } else {
            for (var i = 0, ii = $("#sSort .box").length; i < ii; i++) {

                if ($("#sSort .box:eq(" + i + ") .chname").attr('title') == xid) {
                    $("#sSort .box:eq(" + i + ")").remove();
                }

                for (var j = 0, jj = $(".wareSort .scheck").length; j < jj; j++) {
                    if ($(".wareSort .scheck:eq(" + j + ")").val() == xid) {
                        $(".wareSort .scheck:eq(" + j + ")").attr('checked', false);
                    }

                }
            }
        }
    });
    // 标签选择主辅料-type
    $(".chcheck li input").live('click', function () {
        var _in = $(this).parent('li').index();
        if (typeof($(this).parents('ul').attr('title')) != "undefined") {
            $(this).parents('ul').attr('title', _in)
        } else {
        }

    })

    // 搜索
    $(".searchsc").bind('keypress', function (e) {
        var has = false;
        var bf = cf = df ='';
        if (e.keyCode == 13) {
            var _this = $(this).val();
            var _name = $(this).attr('name');
            if (_this == '') {
                alert("请输入需要搜索的标签");
            } else if (_this != '') {
                for (i = 0; i < cateIngre.length; i++) {
                    if (cateIngre[i].name.indexOf(_this) != -1) {
                        has = true;
                        $("#sort1 li:eq(" + i + ")").addClass("active");

                    }else{
                        for(var j=0;j<cateIngre[i]['synonum'].length;j++){
                            if (cateIngre[i]['synonum'][j].indexOf(_this) != -1) {
                                has = true;
                                $("#sort1 li:eq(" + i + ")").addClass("active");
                            }
                        }
                    }

                    // 2
                    if (cateIngre[i]['child'] == undefined) {  continue; }
                    for (j = 0; j < cateIngre[i]['child'].length; j++) {
                        if (cateIngre[i]['child'][j].name.indexOf(_this) != -1) {
                            has = true;
                            bf += '<li class="active"><a href="javascript:void(0);" onClick="selectsC(' + i + ',' + j + ');">' + cateIngre[i]['child'][j].name + '</a><input type="checkbox" class="scheck" value="' + cateIngre[i]['child'][j].id + '" data="' + cateIngre[i]['child'][j].parentid + '" onChange="selectsC(' + i + ',' + j + ');"/></li>';
                        }else{
                            for(var jj=0;jj<cateIngre[i]['child'][j]['synonum'].length;jj++){
                                if (cateIngre[i]['child'][j]['synonum'][jj].indexOf(_this) != -1) {
                                    has = true;
                                    bf += '<li class="active"><a href="javascript:void(0);" onClick="selectsC(' + i + ',' + j + ');">' + cateIngre[i]['child'][j].name + '</a><input type="checkbox" class="scheck" value="' + cateIngre[i]['child'][j].id + '" data="' + cateIngre[i]['child'][j].parentid + '" onChange="selectsC(' + i + ',' + j + ');"/></li>';
                                }
                            }
                        }

                        // 3

                        if (cateIngre[i]['child'][j]['child'] == undefined) { continue;  }
                        for (k = 0; k < cateIngre[i]['child'][j]['child'].length; k++) {
                            if (cateIngre[i]['child'][j]['child'][k].name.indexOf(_this) != -1) {
                                has = true;
                                df += '<li class="active"><a href="javascript:void(0);" onClick="selectsC(' + i + ',' + j + ',' + k + ');">' + cateIngre[i]['child'][j]['child'][k].name + '</a><input type="checkbox" class="scheck" value="' + cateIngre[i]['child'][j]['child'][k].id + '" data="' + cateIngre[i]['child'][j]['child'][k].parentid + '" onChange="selectsC(' + i + ',' + j + ',' + k + ');"/></li>';
                            }

                            // 4
                            if (cateIngre[i]['child'][j]['child'][k]['child'] == undefined) { continue;  }
                            for (f = 0; f < cateIngre[i]['child'][j]['child'][k]['child'].length; f++) {
                                if (_this == cateIngre[i]['child'][j]['child'][k]['child'][f].name) {
                                    has = true;
                                    df += '<li class="active"><a href="javascript:void(0);" >' + cateIngre[i]['child'][j]['child'][k]['child'][f].name + '</a><input type="checkbox" class="scheck" value="' + cateIngre[i]['child'][j]['child'][k]['child'][f].id + '"/></li>';
                                }
                            }
                        }
                    }
                }

                if (has == true) {
                    $("#iort2").html(bf).show();
                    $("#iort3").html(cf).show();
                    $("#iort4").html(df).show();
                } else {
                    alert("没有您要搜索的标签");
                }

            }
        }
    })

</script>


</body>
</html>