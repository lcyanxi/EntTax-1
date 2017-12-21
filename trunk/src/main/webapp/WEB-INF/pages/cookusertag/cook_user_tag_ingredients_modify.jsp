<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
            /*overflow: hidden; !* Remove scroll bars on browser window *!*/
            font-size: 75%;
        }
        .liner{position: relative;}
        .liner>span{display: inline-block;height: 45px; line-height: 45px;margin-top: 10px;vertical-align: middle;width: 84px;font-size: 14px;}
        .rory .strtext{width: 640px;}
        ul{margin:0;padding: 0;}
        ul li{list-style: none;}
        .shinb{display: inline-block;margin-bottom: 20px;font-size: 14px;width: 200px;}
        .shinb select{font-size: 14px;width: 137px;}
        .hide{display: none;}
        .ultab{overflow: auto;clear: both;}
        .ultab li{width: 100px;font-size: 14px;text-align: center;float: left;border: 1px solid #f2f2f2;line-height: 40px;}
        .ultab li.active{background: #f2f2f2;border-bottom-color: #f2f2f2 }
        #tabbox {background: #f2f2f2;overflow: hidden;padding:20px 10px;}
        #tabbox .bb{background: #fff;overflow-y: scroll;padding: 5px;border-radius: 4px;height: 600px;}

        .wareSort { overflow: hidden;}
        .classifyArea{ float: left; width: 23%;  margin-right:1% ;  border: 1px solid #ddd; background-color: #fff;overflow: hidden;padding-top:10px ;padding-bottom:10px;}
        .wareSort>input[type='text']{display: block;height: 24px;line-height: 24px;margin-top: 6px ;text-align: center;border: 1px solid #ddd;width: 96%}
        .wareSort ul li{position: relative;vertical-align: middle;padding:4px !important;}
        .wareSort ul li a { display: inline-block;margin-right: 10px;line-height: 16px; overflow: hidden;text-overflow: ellipsis;color: #585858 }
        .wareSort ul li a:hover { color: #52bea6; }
        .wareSort ul li.active a {  color: #52bea6;}
        .wareSort ul li span{color: #a1161e;}
        .wareSort ul li.appid a:after{content: "ｃ⌒っ*ﾟ∀ﾟ)";color: #a1161e}
        .wareSort ul li input[type='checkbox']{display: inline-block;width: 16px;height: 16px;position: absolute;right: 4px;top: 0px;cursor: pointer;}
        .wareSort ul.classifyArea{border-top: none;}
        .wareSort div.classifyArea{border-top: none}
        .wareSort div.classifyArea a{display: block;text-align: center;}
        .selectedSort {margin-top: 4px;margin-bottom: 4px;}
        .selectedSort h3{line-height: 24px;padding: 4px;font-size: 12px;font-weight: normal}
        .selectedSort span{color: #585858;margin-right: 10px;margin-bottom:4px;line-height: 16px;}
        .selectedSort span input[type='checkbox']{display: inline-block;width: 14px;height: 14px;margin:0;}
        .wareSortBtn { padding-bottom: 50px; text-align: center; padding-top: 10px; }
        .wareSortBtn input { width: 200px; height: 36px; border: 1px solid #ed7f5a; -webkit-border-radius: 2px; -moz-border-radius: 2px; border-radius: 2px; background-color: #ed7f5a; color: #fff; }
        .wareSortBtn input:hover { border-color: #d95459; background-color: #d95459; }
        .wareSortBtn input:disabled { border-color: #ddd; background-color: #f6f6f6; color: #9a9a9a; cursor: default; }
        .box{width: 500px;overflow: hidden;color: #585858;font-size: 12px;padding-bottom:10px;}
        .chname{width: 140px;float: left;line-height: 14px;}
        .chcheck{width: 360px;float: right;padding:0 !important;}
        .chcheck li{width: 25% !important;padding:0 !important;text-align: center;float: left;height: 14px;}
        .chcheck li input{width: 14px;height: 14px;margin:0 !important;padding: 0!important;}
        #sSort{margin-top:10px;}
        .wareSort>input[type='text']{display: block;height: 24px;line-height: 24px;margin-top: 6px ;text-align: center;border: 1px solid #ddd;width: 96%;}
        .wareSort ul li span:before{content: "(";color: #a1161e}
        .wareSort ul li span:after{content: ")";color: #a1161e}
        .wareSort ul li input[type='checkbox']{display: inline-block;width: 16px;height: 16px;position: absolute;right: 4px;top: 0px;cursor: pointer;}
        .wareSort div.classifyArea a{display: block;text-align: center;}
        .selectedSort span input[type='checkbox']{display: inline-block;width: 14px;height: 14px;margin:0;}
        #cook_title{line-height: 24px;border: 1px solid #ccc}
        #rcmmdBtn{border: 1px solid #ccc;line-height: 24px;padding-left: 4px;padding-right: 4px;background: #f1f1f1;color: #333;cursor: pointer;}
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
</head>
<body>

<%--<!-- A -->--%>
<%--<form method="post" name="functioninfo_modify" id="modify" action="/cookusertag/update.do" title='' style="width:auto;margin:0px;">--%>
    <%--<div class="selectedSort">--%>
        <%--<div style="padding-top: 5px;">--%>
            <%--<input type="text" class="text-input" id="cook_title" value="aaa">--%>
            <%--<input type="button" id="rcmmdBtn" value="查询">(搜索以此为标题的菜谱的系统标签)--%>
        <%--</div>--%>
        <%--<h3>已选标签：</h3>--%>
        <%--<div id="selectedSort">--%>
            <%--<c:forEach items="${cookTagList}" var="c">--%>
            <%--<span title="aaa" data-level="aaa" data="111"><input type="checkbox" checked="true" class="ccheck" name="category_id" value="22" >aaa</span>--%>
            <%--</c:forEach>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</form>--%>

<div class="selectedSort">
    <div style="padding-top: 20px; width: auto">
        <%--<input type="text" class="text-input" id="cook_title" value="">--%>
        <%--<input type="button" id="rcmmdBtn" value="查询">(搜索以此为标题的菜谱的系统标签)--%>
        <h1>  - DC用户标签系统 -</h1>
    </div>

    <div>
    <hr/>
    </div>

    <h2> ${cookUserTag.tagName}</h2>

    <form method="post" name="functioninfo_modify" id="modify" action="/cookUserTag/update.do?tagId=${tagId}&component=${component}" title='' style="width:auto;margin:0px;">
        <div id="selectedChosen">
            <lable>已选成分：</lable>
            <c:forEach items="${cookTagChosenList}" var="cc">
                <input type="checkbox" checked="true" class="ccheck" name="category_id_chosen" value="${cc.id}" /><lable>${cc.name}</lable>
            </c:forEach>
        </div>
        <div id="selectedSort" style="margin-left: 3px">
            <lable>新选成分：</lable>
        </div>
        <div style="margin-top: 20px">
            <button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
            <span class="ui-button-text ui-corner-all">保存标签</span></button>
        </div>
    </form>

</div>
<div class="wareSort clearfix">
    <input type="text" value="" placeholder="搜索" name="first" class="sousuo"/>
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

    <ul id="sort1" class="classifyArea" ></ul>
    <ul id="sort2" class="classifyArea" style="display: none;"></ul>
    <ul id="sort3" class="classifyArea" style="display: none;"></ul>
    <ul id="sort4" class="classifyArea" style="display: none;"></ul>
</div>
<script type="text/javascript">
    var categories = ${tree};

    function Aremove(element,vue){

        element.forEach(function(value,index){
            if(value == vue){
                element.splice(index,1);
            }
        })
    }

    function in_array(stringToSearch, arrayToSearch) {
        for (s = 0; s < arrayToSearch.length; s++) {
            thisEntry = arrayToSearch[s].toString();
            if (thisEntry == stringToSearch) {
                return true;
            }
        }
        return false;
    }

    var aa = [],
            cc = [],
            bb = [],
            checkno = [1,5,11],
            checknb = [15,16,17,224,225,408,409,410];

    (function checkshow(){
        $("#sort2").show();
        $("#sort3").show();
        $("#sort4").show();
        var has =false;
        for(var i=0,ii=$("#selectedSort span").length;i<ii;i++ ){
            if($("#selectedSort span:eq("+ i+")").attr('data-level')==3){
                cc.push($("#selectedSort span:eq("+ i+")").attr('data'));
                for(var b=0;b<categories.length;b++){
                    if(categories[b]['child'] == undefined) {
                        continue;
                    }
                    for(var j=0;j<categories[b]['child'].length;j++){
                        if(categories[b]['child'][j]['child'] == undefined) {
                            continue;
                        }
                        for(var k=0,kk=categories[b]['child'][j]['child'].length;k<kk;k++){
                            if(categories[b]['child'][j]['child'][k]['child'] == undefined) {
                                continue;
                            }
                            for(var f=0,ff=categories[b]['child'][j]['child'][k]['child'].length;f<ff;f++ ){
                                var before = $("#selectedSort span:eq("+ i+")").attr('title');
                                var after =categories[b]['child'][j]['child'][k]['child'][f].id;
                                if(before==after){
                                    var bf ='<li data-level="3" data="'+ categories[b]['child'][j]['child'][k]['child'][f].id +'"><a href="javascript:void(0);">'+ categories[b]['child'][j]['child'][k]['child'][f].name +'</a><input type="checkbox" class="ccheck" checked="true" value="'+ categories[b]['child'][j]['child'][k]['child'][f].id +'" data="'+ categories[b]['child'][j]['child'][k]['child'][f].parentid +'"/></li>';
                                    $("#sort4").append(bf);
                                }
                            }
                        }
                    }
                }

            }else if($("#selectedSort span:eq("+ i+")").attr('data-level')==2){
                bb.push($("#selectedSort span:eq("+ i+")").attr('data'));
                for(var b=0;b<categories.length;b++){
                    if(categories[b]['child'] == undefined) {
                        continue;
                    }
                    for(var j=0;j<categories[b]['child'].length;j++){
                        if(categories[b]['child'][j]['child'] == undefined) {
                            continue;
                        }
                        for(var k=0,kk=categories[b]['child'][j]['child'].length;k<kk;k++){
                            var before = $("#selectedSort span:eq("+ i+")").attr('title');
                            var after =categories[b]['child'][j]['child'][k].id;
                            if(before==after){
                                var bf ='<li data-level="2" data= "'+ categories[b]['child'][j]['child'][k].id +'"><a href="javascript:void(0);" onClick="selectC(' + b + ',' + j + ',' + k +');">'+ categories[b]['child'][j]['child'][k].name +'</a><input type="checkbox" class="ccheck" checked="true" value="'+ categories[b]['child'][j]['child'][k].id +'" data="'+ categories[b]['child'][j]['child'][k].parentid +'"/></li>';
                                $("#sort3").append(bf);
                            }
                        }
                    }
                }

            }else if($("#selectedSort span:eq("+ i+")").attr('data-level')==1){
                var bf = '';
                aa.push($("#selectedSort span:eq("+ i+")").attr('data'));

                for(var b=0;b<categories.length;b++){
                    if(categories[b]['child'] == undefined) {
                        continue;
                    }
                    for(var j=0;j<categories[b]['child'].length;j++){
                        var before = $("#selectedSort span:eq("+ i+")").attr('title');
                        var after =categories[b]['child'][j].id;
                        if(before==after){
                            bf +='<li data-level="1" data="'+ categories[b]['child'][j].id +'"><a href="javascript:void(0);" onClick="selectC(' + b + ',' + j + ');">'+ categories[b]['child'][j].name +'</a><input type="checkbox" class="ccheck" checked="true" value="'+ categories[b]['child'][j].id +'" data="'+ categories[b]['child'][j].parentid +'"';
                            if(in_array(categories[b]['child'][j].id, checknb)){
                                bf += ' disabled ';
                            }
                            bf += '/></li>';
                            $("#sort2").append(bf);
                        }
                    }

                }
            }
        }

        for(var i = 0;i < $("#sort2 li").length; i++){
            if(in_array($("#sort2 li:eq("+ i +")").attr('data'),bb)){
                $("#sort2 li:eq("+ i +")").addClass('appid');
                $("#sort2 li:eq("+ i +") input").attr('disabled',true);
            }
        }

        for(var j = 0;j < $("#sort3 li").length; j++){
            if(in_array($("#sort3 li:eq("+ j +")").attr('data'),cc)){
                $("#sort3 li:eq("+ j +")").addClass('appid');
                $("#sort3 li:eq("+ j +") input").attr('disabled',true);
            }
        }
    })();

    (function() {
        areaCont = "";
        for (var i=0; i<categories.length; i++) {
            areaCont +='<li'
            if(in_array(categories[i].id, aa)){
                areaCont += ' class="appid" ';
            }
            areaCont += '  data =' + categories[i].id+'><a onClick="selectP(' + i + '); " href="javascript:void(0)">' + categories[i].name + '</a></li>';
        }
        $("#sort1").html(areaCont);
    })();


    function isEmptyObject(e) {
        var t;
        for (t in e)
            return !1;
        return !0
    }


    /*选择一级目录*/
    function selectP(p) {
        $("#sort1 li").eq(p).addClass("active").siblings("li").removeClass("active");
        areaCont = "";
        $("#sort3").hide();
        $("#sort4").hide();
        if(isEmptyObject(categories[p]['child'])) {
            $("#sort2").hide();
            return;
        }else{
            for (var j=0; j<categories[p].child.length; j++) {
                areaCont += '<li';
                if(in_array(categories[p]['child'][j].id, bb)){
                    areaCont += ' class="appid" ';
                }
                areaCont += ' data =' +  categories[p]['child'][j].id+ ' ><a  onClick="selectC(' + p + ',' + j + '); " href="javascript:void(0)">' + categories[p]['child'][j].name + '</a><input type="checkbox" ';

                if ($("span[title=" + categories[p]['child'][j].id + "]").length > 0) {
                    areaCont += " checked ";
                }
                if(in_array(categories[p].id, checkno)){
                    areaCont += ' disabled ';
                }
                if(in_array(categories[p]['child'][j].id, bb)){
                    areaCont += ' disabled ';
                }
                areaCont += ' onChange="selectC(' + p + ',' + j + ')" class="ccheck" value="'+ categories[p]['child'][j].id +'" data="'+ categories[p]['child'][j].parentid +'" data-level="1" ></li>';
            }
            $("#sort2").html(areaCont).show();
        }
    }
    /*选择二级目录*/
    function selectC(p,c) {
        $("#sort2 li").eq(c).addClass("active").siblings("li").removeClass("active");
        $("#sort4").hide();
        areaCont = "";
        if(isEmptyObject(categories[p]['child'][c]['child'])){
            $("#sort3").hide();
            return;
        }else{
            for (var k=0; k < categories[p]['child'][c]['child'].length; k++) {
                areaCont += '<li  ';
                if(in_array(categories[p]['child'][c]['child'][k].id, cc)){
                    areaCont += ' class="appid" ';
                }
                areaCont += '  data =' +categories[p]['child'][c]['child'][k].id + ' ><a onClick="selectD(' + p + ',' + c + ',' + k + ');" href="javascript:void(0)">' + categories[p]['child'][c]['child'][k].name + '</a><input type="checkbox" ';
                if ($("span[title=" + categories[p]['child'][c]['child'][k].id + "]").length > 0) {
                    areaCont += " checked ";
                }
                if(in_array(categories[p]['child'][c]['child'][k].id, cc)){
                    areaCont += ' disabled ';
                }
                areaCont += ' onChange="selectD(' + p + ',' + c + ',' + k + ');" class="ccheck" value="'+ categories[p]['child'][c]['child'][k].id +'" data="'+ categories[p]['child'][c]['child'][k].parentid +'" data-level="2"></li>';
            }
            $("#sort3").html(areaCont).show();
        }
    }
    /*选择三级目录*/
    function selectD(p,c,d) {
        $("#sort3 li").eq(d).addClass("active").siblings("li").removeClass("active");
        areaCont = "";
        expressC = "";
        expressD = "";
        if(isEmptyObject(categories[p]['child'][c]['child'][d]['child'])){
            $("#sort4").hide();
            return;
        } else{
            for (var k=0; k<categories[p]['child'][c]['child'][d]['child'].length; k++) {
                areaCont += '<li   data =' + categories[p]['child'][c]['child'][d]['child'][k].id + '" ><a onClick="selectF(' + p + ',' + c + ',' + d + ',' + k + ');" href="javascript:void(0)">' + categories[p]['child'][c]['child'][d]['child'][k].name + '</a><input type="checkbox" ';
                if ($("span[title=" + categories[p]['child'][c]['child'][d]['child'][k].id + "]").length > 0) {
                    areaCont += " checked ";
                }
                areaCont += ' class="ccheck" onChange="selectF(' + p + ',' + c + ',' + d + ',' + k + ');" value="'+ categories[p]['child'][c]['child'][d]['child'][k].id +'" data-level="3" data="'+ categories[p]['child'][c]['child'][d]['child'][k].parentid +'"></li>';
            }
            $("#sort4").html(areaCont).show();
        }
    }
    // 选择四级目录
    function selectF(p,c,d,f) {
        $("#sort4 li").eq(f).addClass("active").siblings("li").removeClass("active");
    }

    // 菜谱选择标签
    $(".ccheck").live('click',function(){
        var _true = $(this).attr('checked'),
                xid = $(this).val(),
                pid = $(this).attr('data'),
                xlevel =$(this).attr('data-level'),
                _index = $(this).parents('ul').attr('id');
        if(_true){
            var areaCont = '';
            areaCont ='<span title="'+ xid +'" data-level = "'+ xlevel +'" data="'+ $(this).attr('data') +'"><input type="checkbox" checked="true" readonly="readonly" name="category_id" value="'+ xid +'" />'+$(this).siblings('a').text() +'</span>';
            $("#selectedSort").append(areaCont);

            if(_index == 'sort3'){
                var has2 = false;

                for(k=0,kk=$("#sort2 li").length;k<kk;k++){
                    if($("#sort2 li:eq("+k+")").attr('data')==pid){
                        $("#sort2 li:eq("+k+")").addClass('appid');
                        $("#sort2 li:eq("+k+") input").attr('checked',true);
                        $("#sort2 li:eq("+k+") input").attr('disabled',true);
                        var aid = $("#sort2 li:eq("+k+") input").val();
                        var apid = $("#sort2 li:eq("+k+") input").attr('data');
                        var aname = $("#sort2 li:eq("+k+") a").text();
                        for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                            if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                has2 = true;
                                return has2;
                                break;
                            }
                        }
                        break;
                    }
                }

                if($("#sort2 li").length == 0){
                    for (i=0;i<categories.length;i++ ){
                        if(categories[i]['child'] == undefined) {   continue;  }
                        for(j=0;j<categories[i]['child'].length;j++){
                            if(categories[i]['child'][j].id == pid){
                                var aid = categories[i]['child'][j].id;
                                var apid = categories[i]['child'][j].parentid;
                                var aname = categories[i]['child'][j].name;
                                for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                    if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                        has2 = true;
                                        return has2;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

                if(has2 == false && typeof aid != 'undefined'){
                    _xlevel = xlevel -1;
                    areaCont ='<span title="'+ aid +'" data-level="'+ _xlevel +'" data="'+ apid +'"><input type="checkbox" checked="true" readonly="readonly" name="category_id" value="'+ aid +'" />'+ aname +'</span>';
                    $("#selectedSort").append(areaCont);
                }
            }else if(_index == 'sort4'){

                var has3 = false;
                var has2 = false;
                for(k=0,kk=$("#sort3 li").length;k<kk;k++){
                    if($("#sort3 li:eq("+k+")").attr('data')==pid){
                        $("#sort3 li:eq("+k+")").addClass('appid');
                        $("#sort3 li:eq("+k+") input").attr('checked',true);
                        $("#sort3 li:eq("+k+") input").attr('disabled',true);
                        var bid = $("#sort3 li:eq("+k+") input").val();
                        var bpid = $("#sort3 li:eq("+k+") input").attr('data');
                        var bname = $("#sort3 li:eq("+k+") a").text();
                        for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                            if($("#selectedSort span:eq("+ t +")").attr('title') == bid){
                                has3 = true;
                                return has3;
                                break;
                            }
                        }
                    }
                }

                if($("#sort3 li").length == 0 ){
                    for (i=0;i<categories.length;i++ ){
                        if(categories[i]['child'] == undefined) {   continue;  }
                        for(j=0;j<categories[i]['child'].length;j++){
                            if(categories[i]['child'][j]['child'] == undefined) { continue;  }
                            for(k=0;k<categories[i]['child'][j]['child'].length;k++){
                                if(categories[i]['child'][j]['child'][k].id == pid){
                                    var bid = categories[i]['child'][j]['child'][k].id;
                                    var bpid = categories[i]['child'][j]['child'][k].parentid;
                                    var bname = categories[i]['child'][j]['child'][k].name;
                                    for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                        if($("#selectedSort span:eq("+ t +")").attr('title') == bid){
                                            has3 = true;
                                            return has3;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                if(has3 == false && typeof bid != 'undefined'){
                    _xlevel = xlevel - 1;
                    areaCont ='<span title="'+ bid +'" data-level="'+ _xlevel +'" data="'+ bpid +'"><input type="checkbox" checked="true" readonly="readonly" name="category_id" value="'+ bid +'" />'+ bname +'</span>';
                    $("#selectedSort").append(areaCont);
                }



                for(k=0,kk=$("#sort2 li").length;k<kk;k++){
                    if($("#sort2 li:eq("+k+")").attr('data')==bpid){
                        $("#sort2 li:eq("+k+")").addClass('appid');
                        $("#sort2 li:eq("+k+") input").attr('checked',true);
                        var aid = $("#sort2 li:eq("+k+") input").val();
                        var apid = $("#sort2 li:eq("+k+") input").attr('data');
                        var aname = $("#sort2 li:eq("+k+") a").text();
                        for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                            if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                has2 = true;
                                return has2;
                                break;
                            }
                        }
                        break;
                    }
                }

                if($("#sort2 li").length == 0){
                    for (i=0;i<categories.length;i++ ){
                        // 2
                        if(categories[i]['child'] == undefined) {   continue;  }
                        for(j=0;j<categories[i]['child'].length;j++){
                            if(categories[i]['child'][j].id == bpid){
                                var aid = categories[i]['child'][j].id;
                                var apid = categories[i]['child'][j].parentid;
                                var aname = categories[i]['child'][j].name;
                                for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                    if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                        has2 = true;
                                        return has2;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }


                if(has2 == false && typeof aid != 'undefined'){
                    _xlevel = xlevel - 2;
                    areaCont ='<span title="'+ aid +'" data-level="'+ _xlevel +'" data="'+ apid +'"><input type="checkbox" checked="true" readonly="readonly" name="category_id" value="'+ aid +'" />'+ aname +'</span>';
                    $("#selectedSort").append(areaCont);
                }
            }
        }else{
            // checked = false;
            for(var i= 0,ii =$("#selectedSort span").length;i<ii;i++ ){

                if($("#selectedSort span:eq("+i+")").attr('title') == xid){
                    $("#selectedSort span:eq("+i+")").remove();
                }

                for(var j=0,jj= $(".wareSort .ccheck").length;j<jj;j++ ){
                    if($(".wareSort .ccheck:eq("+j+")").val()==xid){
                        $(".wareSort .ccheck:eq("+j+")").attr('checked',false);
                    }
                }
            }

            if(_index == 'sort2'){
                Aremove(aa,pid);
            }


            if(_index == 'sort3'){
                Aremove(bb,pid);
                var has2 = false;
                for(var s = 0, ss = $("#selectedSort span").length;s < ss ;s++ ){
                    if($("#selectedSort span:eq("+s+")").attr('data') == pid){
                        has2 = true ;
                    }
                }

                if(has2 == false){

                    for(k=0,kk=$("#sort2 li").length;k<kk;k++){
                        if($("#sort2 li:eq("+k+")").attr('data')==pid){
                            $("#sort2 li:eq("+k+")").removeClass('appid');
                            $("#sort2 li:eq("+k+") input").attr('checked',false);
                            if(!in_array(pid, checknb)){
                                $("#sort2 li:eq("+k+") input").attr('disabled',false);
                            }
                            var aid = $("#sort2 li:eq("+k+") input").val();
                            var apid = $("#sort2 li:eq("+k+") input").attr('data');
                            var aname = $("#sort2 li:eq("+k+") a").text();
                            for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                    $("#selectedSort span:eq("+ t +")").remove();
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    if($("#sort2 li").length == 0){
                        for (i=0;i<categories.length;i++ ){
                            // 2
                            if(categories[i]['child'] == undefined) {   continue;  }
                            for(j=0;j<categories[i]['child'].length;j++){
                                if(categories[i]['child'][j].id == pid){
                                    var aid = categories[i]['child'][j].id;
                                    var apid = categories[i]['child'][j].parentid;
                                    var aname = categories[i]['child'][j].name;
                                    for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                        if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                            $("#selectedSort span:eq("+ t +")").remove();
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    Aremove(aa,apid);
                }

            }else if(_index == 'sort4'){
                Aremove(cc,pid);
                var has4 = false;
                var has3 = false;
                for(var s = 0, ss = $("#selectedSort span").length;s < ss ;s++ ){
                    if($("#selectedSort span:eq("+s+")").attr('data') == pid){
                        has4 = true ;
                    }
                }


                if(has4 == false){

                    for(k=0,kk=$("#sort3 li").length;k<kk;k++){

                        if($("#sort3 li:eq("+k+")").attr('data') == pid){
                            $("#sort3 li:eq("+k+")").removeClass('appid');
                            $("#sort3 li:eq("+k+") input").attr('checked',false);
                            $("#sort3 li:eq("+k+") input").attr('disabled',false);
                            var bid = $("#sort3 li:eq("+k+") input").val(),
                                    bpid = $("#sort3 li:eq("+k+") input").attr('data'),
                                    bname = $("#sort3 li:eq("+k+") a").text();

                            for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                if($("#selectedSort span:eq("+ t +")").attr('title') == bid){
                                    $("#selectedSort span:eq("+ t +")").remove();
                                    break;
                                }
                            }
                        }
                    }
                    //no 显示父级3
                    if($("#sort3 li").length == 0 ){
                        for (i=0;i<categories.length;i++ ){
                            if(categories[i]['child'] == undefined) {   continue;  }
                            for(j=0;j<categories[i]['child'].length;j++){
                                if(categories[i]['child'][j]['child'] == undefined) { continue;  }
                                for(k=0;k<categories[i]['child'][j]['child'].length;k++){
                                    if(categories[i]['child'][j]['child'][k].id == pid){
                                        // 到哪里啦 郁闷
                                        var bid = categories[i]['child'][j]['child'][k].id,
                                                bpid = categories[i]['child'][j]['child'][k].parentid;

                                        for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                            if($("#selectedSort span:eq("+ t +")").attr('title') == bid){
                                                $("#selectedSort span:eq("+ t +")").remove();
                                                break;
                                            }
                                        }

                                    }
                                }

                            }
                        }
                    }

                    Aremove(bb,bpid);

                    for(var s = 0, ss = $("#selectedSort span").length;s < ss ;s++ ){
                        if($("#selectedSort span:eq("+s+")").attr('data') == bpid){
                            has3 = true ;
                        }
                    }

                    if(has3 == false){

                        for(k=0,kk=$("#sort2 li").length;k<kk;k++){
                            if($("#sort2 li:eq("+k+")").attr('data')==bpid){
                                $("#sort2 li:eq("+k+")").removeClass('appid');
                                $("#sort2 li:eq("+k+") input").attr('checked',false);
                                if(!in_array(bpid, checknb)){
                                    $("#sort2 li:eq("+k+") input").attr('disabled',false);
                                }
                                var aid = $("#sort2 li:eq("+k+") input").val();

                                for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                    if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                        $("#selectedSort span:eq("+ t +")").remove();
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        //no 显示2级
                        if($("#sort2 li").length == 0){

                            for (i=0;i<categories.length;i++ ){

                                if(categories[i]['child'] == undefined) {   continue;  }
                                for(j=0;j<categories[i]['child'].length;j++){

                                    if(categories[i]['child'][j].id == bpid){
                                        var aid = categories[i]['child'][j].id,
                                                apid = categories[i]['child'][j].parentid;
                                        for(var t = 0,tt = $("#selectedSort span").length; t < tt; t++ ){

                                            if($("#selectedSort span:eq("+ t +")").attr('title') == aid){
                                                $("#selectedSort span:eq("+ t +")").remove();
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        Aremove(aa,apid);
                    }

                }

            }
        }
    })

    // 搜索
    $(".sousuo").bind('keypress',function(e){
        var has = false;
        var bf = cf = df = '' ;
        if (e.keyCode == 13) {
            var _this = $(this).val();
            var _name =$(this).attr('name');
            if(_this == ''){
                alert("请输入需要搜索的标签");
            }else if(_this != ''){
                for (i=0;i<categories.length;i++ ){
                    // 1
                    if(categories[i].name.indexOf(_this) != -1){
                        has = true;
                        $("#sort1 li:eq("+ i +")").addClass("active");
                    }

                    // 2
                    if(categories[i]['child'] == undefined) {   continue;  }
                    for(j=0;j<categories[i]['child'].length;j++){
                        if(categories[i]['child'][j].name.indexOf(_this) != -1){
                            has = true;
                            bf +='<li class="active" data="'+categories[i]['child'][j].id +'" data-level="1"><a href="javascript:void(0);" onClick="selectC(' + i + ',' + j + ');">'+ categories[i]['child'][j].name +'</a><input type="checkbox" class="ccheck" value="'+ categories[i]['child'][j].id +'" data="'+ categories[i]['child'][j].parentid +'"';
                            if(in_array(categories[i]['child'][j].id, checknb)){
                                bf += ' disabled ';
                            }
                            bf += '/></li>';
                        }

                        // 3
                        if(categories[i]['child'][j]['child'] == undefined) { continue;  }
                        for(k=0;k<categories[i]['child'][j]['child'].length;k++){
                            if(categories[i]['child'][j]['child'][k].name.indexOf(_this) != -1){
                                has = true;
                                cf +='<li class="active" data="'+ categories[i]['child'][j]['child'][k].id +'" data-level="2"><a href="javascript:void(0);" onClick="selectC(' + i + ',' + j +',' + k + ');">'+ categories[i]['child'][j]['child'][k].name +'</a><input type="checkbox" class="ccheck" value="'+ categories[i]['child'][j]['child'][k].id +'" data="'+ categories[i]['child'][j]['child'][k].parentid +'"/></li>';
                            }
                            // 4
                            if(categories[i]['child'][j]['child'][k]['child'] == undefined) {  continue;  }
                            for(f=0;f<categories[i]['child'][j]['child'][k]['child'].length;f++){
                                if(categories[i]['child'][j]['child'][k]['child'][f].name.indexOf(_this) != -1){
                                    has = true;
                                    df +='<li class="active" data="'+ categories[i]['child'][j]['child'][k]['child'][f].id +'" data-level="3"><a href="javascript:void(0);" >'+ categories[i]['child'][j]['child'][k]['child'][f].name +'</a><input type="checkbox" class="ccheck" value="'+ categories[i]['child'][j]['child'][k]['child'][f].id +'" data="'+ categories[i]['child'][j]['child'][k]['child'][f].parentid +'"/></li>';
                                }
                            }
                        }
                    }
                }
                if(has ==true){
                    $("#sort2").html(bf).show();
                    $("#sort3").html(cf).show();
                    $("#sort4").html(df).show();
                    // debugger;
                    for(var i=0,ilen = $(".selectedSort span").length;i<ilen;i++){
                        for(var j=0,jlen = $('#sort2 li').length;j<jlen;j++){

                            if($(".selectedSort span:eq("+ i+")").attr('title') == $('#sort2 li:eq('+ j+') input').val()){
                                $('#sort2 li:eq('+ j+') input').attr('checked',true);
                            }
                        }
                        for(var k=0,klen = $('#sort3 li').length;k<klen;k++){
                            if($(".selectedSort span:eq("+ i+")").attr('title') == $('#sort3 li:eq('+ k+') input').val()){
                                $('#sort3 li:eq('+ k+') input').attr('checked',true);
                            }
                        }
                        for(var c=0,clen = $('#sort4 li').length;c<clen;c++){
                            if($(".selectedSort span:eq("+ i+")").attr('title') == $('#sort4 li:eq('+ c+') input').val()){
                                $('#sort4 li:eq('+ c+') input').attr('checked',true);
                            }
                        }
                    }
                }else{
                    alert("没有您要搜索标签");
                }

            }
        }

    })

    $('#rcmmdBtn').bind('click', function () {
        var cookname = $('#cook_title').val();
        var modular  = $(this).attr("modular");
        if(cookname != "") {
            $.ajax(
                    {
                        url: '/recipenew/ajaxrecommendtag?title='+cookname+"&type="+modular,
                        type: "GET",
                        dataType: 'json',
                        async: false,
                        success: function (msg) {
                            console.log(msg);
                            if (msg.status == 'OK') {
                                $.each(msg.data, function(i, element){
                                    if ($("span[title=" + element.id + "]").length <= 0) {
                                        var lohtml = '<span title="' + element.id +
                                                '" data-level="' + element.level +
                                                '"><input type="checkbox" checked="true" readonly="readonly" class="ccheck" name="category_id" value="' + element.id +
                                                '" data="' + element.parentid +
                                                '">' + element.name +
                                                '</span>';
                                        $('#selectedSort').append(lohtml);
                                    }
                                });
                            }
                        }
                    });
        }
    });


</script>
</body>
</html>