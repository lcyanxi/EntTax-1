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
										href="/hiveadmin/preList.do?startDate=${startDate}&endDate=${endDate}&plat=API">返回</a></li>
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
									<c:forEach items="${colTitleList}" var="colTitle">
                                        <th><c:out value="${colTitle}"/></th>
                                    </c:forEach>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="res">
									<tr>
									    <c:if test="${res.size() >= 1 }">
									        <td width="7%"><c:out value="${res.col0}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 2 }">
									        <td width="7%"><c:out value="${res.col1}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 3 }">
									        <td width="7%"><c:out value="${res.col2}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 4 }">
									        <td width="7%"><c:out value="${res.col3}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 5 }">
									        <td width="7%"><c:out value="${res.col4}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 6 }">
									        <td width="7%"><c:out value="${res.col5}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 7 }">
									        <td width="7%"><c:out value="${res.col6}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 8 }">
									        <td width="7%"><c:out value="${res.col7}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 9 }">
									        <td width="7%"><c:out value="${res.col8}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 10 }">
									        <td width="7%"><c:out value="${res.col9}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 11 }">
									        <td width="7%"><c:out value="${res.col11}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 12 }">
									        <td width="7%"><c:out value="${res.col12}" /></td>
									    </c:if>
                                        <c:if test="${res.size() >= 13 }">
									        <td width="7%"><c:out value="${res.col13}" /></td>
									    </c:if>
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