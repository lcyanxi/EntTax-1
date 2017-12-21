<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../includes/header_new.jsp"></jsp:include>
<style>
table{ border-collapse:0; border-spacing:0;}
table tr td{ border:1px solid #ccc;}
</style>
<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>

<div class="bd clearfix">

	<div id="mainContainer" style="margin-left: 0px;">
		<div class="contentCol">

			<input type="hidden" value="active_users" id="action_stats" />

			<div class="operations">
				<div class="bd3">
					<div class="leftCol" style="width: 390px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class= on><a
										href="/hiveadmin/userbehavior/preList.do?startDate=${startDate}&endDate=${endDate}&plat=API">返回</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							结果展示页 <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0" id="all">
							<thead>
								<tr>
									<th>plat</th>
									<th>uid/imei</th>
									<th>时间</th>
									<th>qtype</th>
									<th>name</th>
									<th>id</th>
									<th>系统</th>
									<th>应用版本</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="res">
									<tr>
										<td width="7%"><c:out value="${res.plat}" /></td>
										<td width="10%"><c:out value="${res.uid}" /></td>
										<td width="17%"><c:out value="${res.time}" /></td>
										<td width="15%"><a
											href="http://www.baidu.com/s?wd=<c:out value="${res.qtype}"/>"
											target="_blank"><c:out value="${res.qtype}" /></a></td>
										<td width=""><c:out value="${res.qtypeName}" /></td>
										<td width="10%" >
											<c:out value="${res.objId}" />
											<c:if test="${res.qtype == 'view_caipu_detail'}"> 
												<a href="http://www.douguo.com/cookbook/<c:out value="${res.objId}"/>.html" target="_blank">查看菜谱</a>	
											</c:if>
											<c:if test="${res.qtype == 'view_tuan_detail' || res.qtype == 'view_tuan_showorder_lists'}"> 
												<a href="http://m.douguo.com/tuan/share/<c:out value="${res.objId}"/>" target="_blank">查看商品</a>	
											</c:if>
											<c:if test="${res.qtype == 'view_group_postdetail' || res.qtype == 'view_group_postreplies'}"> 
												<a href="http://m.douguo.com/group/post/<c:out value="${res.objId}"/>" target="_blank">查看帖子</a>	
											</c:if>
											<c:if test="${res.qtype == 'action_tuan_order' || res.qtype == 'view_tuan_order_detail'}"> 
												<a href="http://mall.douguo.net/gorder/gorder_list?order_id=<c:out value="${res.objId}"/>" target="_blank">查看订单</a>	
											</c:if>
										</td>
										<td width="12%"><c:out value="${res.souc}" /></td>
										<td width="8%"><c:out value="${res.vers}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="mod-bottom clearfix">
						<div class="fr pagination"></div>
					</div>
				</div>
			</div>

			<div class="">
				<div id="tip_active">
					<div id="tip_activeTrend" class="tips">
						<div class="corner"></div>
						<p>每天网站和客户端搜索没有结果的关键词和数量，客户端可以根据某个标签搜索。</p>
					</div>
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>TOP100排名</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="plat" value="${plat}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>