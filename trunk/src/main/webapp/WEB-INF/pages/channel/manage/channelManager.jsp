<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../../includes/header_new.jsp"></jsp:include>


<link type="text/css" rel="stylesheet" href="/static/css/main.css" />

<script>
function deleteChannel(id){
    if(window.confirm('确认删除吗？')){
    	channel_id = "channel_"+id;
        $.get("/manage/alone/channel/delete.do", {id:id},
		    function(data){
            $('#'+channel_id).fadeOut();
        }
        );
    }
}

function editChannel(id,code,name){
	window.scrollTo(0,document.body.scrollHeight);
    $('#code').val(code);
    $('#name').val(name);

}
</script>

<div class="bd clearfix">

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
						<c:forEach items="${channels}" var="item" varStatus="stats">
							<tr class="highlight" id="channel_${item.id}">
								<td>${item.id}</td>
								<td>${item.channelCode}</td>
								<td>${item.channelName}</td>
								<td>
									<button class="btn_edit"
										onclick="editChannel('${item.id}','${item.channelCode}','${item.channelName}');">
										编辑</button>

									<button onclick="javascript:deleteChannel(${item.id});"
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
			<form method="post" action="/manage/alone/channel/save.do">
				<label>渠道码：</label>
				<input type="text" id="code" name="code">
				<br />
				<br /> 
				<label>渠道名：</label>
				<input type="text" id="name" name="name">
				<br />
				
				<!-- -------------------- -->
				<div class="selectList">
			        <select class="province">
			            <option>请选择</option>
			        </select>
			        <select class="city">
			            <option>请选择</option>
			        </select>
			        <select class="district">
			            <option>请选择</option>
			        </select>
			    </div>
			    <div class="selectList">
			        <select class="province">
			            <option>请选择</option>
			        </select>
			        <select class="city">
			            <option>请选择</option>
			        </select>
			        <select class="district">
			            <option>请选择</option>
			        </select>
			    </div>
			    <script type="text/javascript">
			    $(function(){
			        $(".selectList").each(function(){
			            var url = "area.json";
			            var areaJson;
			            var temp_html;
			            var oProvince = $(this).find(".province");
			            var oCity = $(this).find(".city");
			            var oDistrict = $(this).find(".district");
			            //初始化省
			            var province = function(){
			                $.each(areaJson,function(i,province){
			                    temp_html+="<option value='"+province.p+"'>"+province.p+"</option>";
			                });
			                oProvince.html(temp_html);
			                city();
			            };
			            //赋值市
			            var city = function(){
			                temp_html = ""; 
			                var n = oProvince.get(0).selectedIndex;
			                $.each(areaJson[n].c,function(i,city){
			                    temp_html+="<option value='"+city.ct+"'>"+city.ct+"</option>";
			                });
			                oCity.html(temp_html);
			                district();
			            };
			            //赋值县
			            var district = function(){
			                temp_html = ""; 
			                var m = oProvince.get(0).selectedIndex;
			                var n = oCity.get(0).selectedIndex;
			                if(typeof(areaJson[m].c[n].d) == "undefined"){
			                    oDistrict.css("display","none");
			                }else{
			                    oDistrict.css("display","inline");
			                    $.each(areaJson[m].c[n].d,function(i,district){
			                        temp_html+="<option value='"+district.dt+"'>"+district.dt+"</option>";
			                    });
			                    oDistrict.html(temp_html);
			                };
			            };
			            //选择省改变市
			            oProvince.change(function(){
			                city();
			            });
			            //选择市改变县
			            oCity.change(function(){
			                district();
			            });
			            //获取json数据
			            $.getJSON(url,function(data){
			                areaJson = data;
			                province();
			            });
			        });
			    });
			    </script>
				<!-- -------------------- -->
				
				<br /> <input type="submit" value="保存">
			</form>
		</div>

	</div>

</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../../includes/footer.jsp"></jsp:include>