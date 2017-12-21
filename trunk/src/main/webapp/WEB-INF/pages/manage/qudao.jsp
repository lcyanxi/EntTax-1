<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../includes/header.jsp"></jsp:include>


<link type="text/css" rel="stylesheet" href="/static/css/main.css" />

<script>
function deleteQudao(id){
    if(window.confirm('确认删除吗？')){
    	qudao_id = "qudao_"+id;
        $.get("/manage/alone/qudao/delete.do", {id:id},
		    function(data){
            $('#'+qudao_id).fadeOut();
        }
        );
    }
}

function editQudao(id,code,name){
	window.scrollTo(0,document.body.scrollHeight);
    $('#code').val(code);
    $('#name').val(name);

}
</script>

<div class="bd clearfix">

	<jsp:include page="../includes/left_menu.jsp">
		<jsp:param name="menu1" value="manage" />
		<jsp:param name="menu2" value="Qudao" />
	</jsp:include>

	<div class="right_field">

		<div id="data_view_field">

			<div class="grid_field" id="grid_field">
				<table width="100%" border="0" cellspacing="0" cellpadding="">
					<tbody>
						<tr>
							<th style="border-left: 0;">渠道id</th>
							<th>渠道码</th>
							<th>渠道名</th>
							<th>操作</th>
						</tr>
						<c:forEach items="${qudaos}" var="item" varStatus="stats">
							<tr class="highlight" id="qudao_${item.id}">
								<td>${item.id}</td>
								<td>${item.qudaoCode}</td>
								<td>${item.qudaoName}</td>
								<td>
									<button class="btn_edit"
										onclick="editQudao('${item.id}','${item.qudaoCode}','${item.qudaoName}');">
										编辑</button>

									<button onclick="javascript:deleteQudao(${item.id});"
										class="btn_del">删除</button>

								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>


		<div id="new_field"
			style="margin-top: 10px; border: 1px solid #EEE; padding: 10px;">
			<form method="post" action="/manage/alone/qudao/save.do">
				<label>渠道码：</label><input type="text" id="code" name="code"><br />
				<br /> <label>渠道名：</label><input type="text" id="name" name="name"><br />
				<br /> <input type="submit" onclick="return ck();" value="保存">
			</form>
		</div>

	</div>

</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>