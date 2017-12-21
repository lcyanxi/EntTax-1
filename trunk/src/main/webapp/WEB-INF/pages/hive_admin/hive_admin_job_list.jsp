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

			<div class="operations">
				<div class="bd3">
					<div class="leftCol" style="width: 390px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${plat == 'JOB'}"> on</c:if>><a
										href="/hiveadmin/preList.do?startDate=${startDate}&endDate=${endDate}&plat=JOB">JOB管理</a></li>
									<li class=<c:if test="${plat == 'FOCUS'}"> on</c:if>><a
										href="/hiveadmin/preFocusList.do?startDate=${startDate}&endDate=${endDate}&plat=FOCUS">焦点图数据统计</a></li>
									<li class=<c:if test="${plat == 'AD'}"> on</c:if>><a
										href="/hiveadmin/preAdList.do?startDate=${startDate}&endDate=${endDate}&plat=AD">广告数据统计</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="wrap-table">
				<div class="parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2></h2>
						<iframe src="/hiveadmin/preListFrame.do" name="adminFrame" id="adminFrame" marginwidth="0" marginheight="0" frameBorder="0" width="100%" height="573px"></iframe>
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