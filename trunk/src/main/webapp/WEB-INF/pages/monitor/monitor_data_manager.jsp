<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../includes/header_new.jsp"></jsp:include>

<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>

<div class="bd clearfix">

	<div id="mainContainer" style="margin-left: 0px;">
		<div class="contentCol">

			<input type="hidden" value="active_users" id="action_stats" />

			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							数据量监测 <a class="icon help poptips"
									 action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0">
							<thead>
							<tr>
								<th>序号</th>
								<th>数据类型</th>
								<th>数据来源</th>
                                <th>索引创建时间</th>
								<th>数据状态</th>
								<th>最后更新时间</th>
								<th>最后更新数量</th>
								<th>累计更新数量</th>
							</tr>
							</thead>
							<tbody id="data-list">
							<c:forEach items="${mapList}" var="datam" varStatus="st">
                                <tr>
                                    <c:choose>
                                        <c:when test="${datam.isNormal=='0'}">
                                            <td style="color: green;"><c:out value="${st.count}"/></td>
                                            <td style="color: green;"><c:out value="${datam.type}" /></td>
                                            <td style="color: green;"><c:out value="${datam.index}" /></td>
                                            <td style="color: green;"><c:out value="${datam.indexCreatedDate}" /></td>
											<td style="color: green;"><c:out value="数据正常" /></td>
											<td style="color: green;"><c:out value="${datam.lastUpdateDate}" /></td>
											<td style="color: green;"><c:out value="${datam.lastUpdateDocs}" /></td>
                                            <td style="color: green;"><a style="color: green" title="${datam.historyDateCounts}"><c:out value="${datam.clusterStatus}" /></a></td>
                                        </c:when>
                                        <c:when test="${datam.isNormal=='1'}">
                                            <td style="color: white; background-color: salmon"><c:out value="${st.count}"/></td>
                                            <td style="color: white; background-color: salmon"><c:out value="${datam.type}" /></td>
                                            <td style="color: white; background-color: salmon"><c:out value="${datam.index}" /></td>
                                            <td style="color: white; background-color: salmon"><c:out value="${datam.indexCreatedDate}" /></td>
                                            <td style="color: white; background-color: salmon"><c:out value="数据异常" /></td>
                                            <td style="color: white; background-color: salmon"><c:out value="${datam.lastUpdateDate}" /></td>
                                            <td style="color: white; background-color: salmon"><c:out value="${datam.lastUpdateDocs}" /></td>
                                            <td style="color: white; background-color: salmon"><a style="color: white" title="${datam.historyDateCounts}"><c:out value="${datam.clusterStatus}" /></a></td>
                                        </c:when>
                                        <c:when test="${datam.isNormal=='2'}">
                                            <td style="color: black; background-color: #ffb570"><c:out value="${st.count}"/></td>
                                            <td style="color: black; background-color: #ffb570"><c:out value="${datam.type}" /></td>
                                            <td style="color: black; background-color: #ffb570"><c:out value="${datam.index}" /></td>
                                            <td style="color: black; background-color: #ffb570"><c:out value="${datam.indexCreatedDate}" /></td>
                                            <td style="color: black; background-color: #ffb570"><c:out value="数据异常" /></td>
                                            <td style="color: black; background-color: #ffb570"><c:out value="${datam.lastUpdateDate}" /></td>
                                            <td style="color: black; background-color: #ffb570"><c:out value="${datam.lastUpdateDocs}" /></td>
                                            <td style="color: black; background-color: #ffb570"><a style="color: white" title="${datam.historyDateCounts}"><c:out value="${datam.clusterStatus}" /></a></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="color: green"><c:out value="${st.count}"/></td>
                                            <td style="color: green"><c:out value="${datam.type}" /></td>
                                            <td style="color: green"><c:out value="${datam.index}" /></td>
                                            <td style="color: green"><c:out value="${datam.indexCreatedDate}" /></td>
											<td style="color: green"><c:out value="数据正常" /></td>
											<td style="color: green"><c:out value="${datam.lastUpdateDate}" /></td>
											<td style="color: green"><c:out value="${datam.lastUpdateDocs}" /></td>
                                            <td style="color: green"><a style="color: green" title="${datam.historyDateCounts}"><c:out value="${datam.clusterStatus}" /></a></td>
                                        </c:otherwise>
                                    </c:choose>
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
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="app" value="${app}" />
<input type="hidden" id="qtype" value="${qtype}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
		type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(
			function() {

				global.renderPage = function() {
					app = $("#app").val();
					qtype = $("#qtype").val();
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/user/behavior/list.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&qtype=" + qtype;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>