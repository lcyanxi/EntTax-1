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
<form method="post" name="functioninfo_add" id="add"
      action="/cookUserTag/insert.do" title=''
      style="width: 700px; margin: 0px;">
    <fieldset>
        <table>
            <tbody>
            <tr>
                <td>Tag名称:</td>
                <td><input type="text" name="tagName" /></td>
                <td>TagID:</td>
                <td><input type="text" name="tagId" /></td>
                <td>Flag:</td>
                <td><input type="text" name="flag" /></td>
            <tr>
                <td><input type="hidden" name="action" value="add" /></td>
                <td><button
                        class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only "
                        type="submit">
                    <span class="ui-button-text ui-corner-all">新增</span>
                </button></td>
            </tr>
            </tbody>
        </table>
    </fieldset>
</form>
<table id="tb_function",align : "center" ></table>
<div id="pager2"></div>
<script>
    $(document).ready(
            function() {
                jQuery("#tb_function").jqGrid(
                        {
                            url : '/cookUserTag/queryJson.do',
                            datatype : "json",
                            colNames : [ 'Id', 'Tag_Id', 'Tag_Name', 'Flag', '修改标签', '修改食材' ],
                            colModel : [ {
                                name : 'Id',
                                index : 'Id',
                                width : 30,
                                align : "center",
                                sortable : false
                            }, {
                                name : 'tagId',
                                index : 'tagId',
                                width : 30,
                                align : "center",
                                sortable : true
                            }, {
                                name : 'tagName',
                                index : 'tagName',
                                width : 60,
                                align : "center",
                                sortable : true
                            }, {
                                name : 'flag',
                                index : 'flag',
                                width : 60,
                                align : "center",
                                sortable : false
                            }, {
                                name : 'modify_categories',
                                index : 'modify_categories',
                                width : 80,
                                align : "center",
                                sortable : false
                            }, {
                                name : 'modify_ingredients',
                                index : 'modify_ingredients',
                                width : 80,
                                align : "center",
                                sortable : false
                            }
                            ],
                            height : 300,
                            rowNum : 500,
                            rowList : [ 500 ],
                            pager : '#pager2',
                            sortname : 'id',
                            viewrecords : true,
                            sortorder : "desc",
                            loadonce : true, //设置自动翻页为真
                            caption : "Tag Box"
                        });
                jQuery("#tb_function").jqGrid('navGrid', '#pager2', {
                    edit : false,
                    add : false,
                    del : false,
                    autowidth : true
                });

                $(window).bind(
                        'load',
                        function() {
                            $("#tb_function").setGridWidth(
                                    $(window).width() * 0.90);
                            $("#tb_function").setGridHeight(
                                    $(window).height() * 0.65);
                        });
                $(window).bind(
                        'resize',
                        function() {
                            $("#tb_function").setGridWidth(
                                    $(window).width() * 0.90);
                            $("#tb_function").setGridHeight(
                                    $(window).height() * 0.50);
                        });
            });
</script>
</body>
</html>