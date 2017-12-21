<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../includes/header.jsp"></jsp:include>
<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>
<div class="bd clearfix">
	<div id="mainContainer" style="margin-left: 0px;">
		<div class="contentCol">
			<input type="hidden" value="installations" id="action_stats" />
			<div class="operations">
				<div class="bd3">
					<div class="leftCol">
						<div class="mod-select">
                        		<div class="tabpanel">
                        		<ul id="toptab" class="borders">
                        		<!--
                        		<li class=<c:if test="${tab == 'NEW' || tab != ''}"> on</c:if>><a
                        			href="/trends/newuser.do?startDate=${startDate}&endDate=${endDate}&app=3&tab=DAU">新增用户</a></li>

                        		<li class=<c:if test="${tab == 'CHANNEL'}"> on</c:if>><a
                        			href="/appdau/daudetaillist.do?startDate=${startDate}&endDate=${endDate}&app=4&tab=CHANNEL">渠道新增用户</a></li>
                        		-->
                        		</ul>
                        	</div>
                        </div>
					</div>
					<div class="contentCol" style="width: 720px;">
						<div class="filterPanel">
							<ul class="filteritems borders clearfix">
								<li class="items itemsfirst  "><span class="ell" id="filter-version_install">版本 </span></li>
								<li class="items  "><span class="ell" id="filter-channel_install">渠道</span></li>
								<li class="items itemslast " style="display:none;"><span class="ell" id="filter-segment_install"><b class="newV fr">new</b>用户群</span></li>
							</ul>
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="date-select">
									<span class="start">${start_date}</span> - <span class="end">${end_date}</span>
									<b class="icon pulldown"></b>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>
						新增用户趋势<a class="icon help poptips" action-frame="tip_newTrend"
							title=""></a>
					</h2>
					<div class="option">
						<div class="particle">
							<ul id="installation_period"></ul>
						</div>
					</div>
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="chartpanel">
							<div id="chartcontainer"
								style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
						<div class="contrastpanel">
							<a href="#" class="constr borders" id="constr-date">对比时段</a>
						</div>
					</div>
				</div>
			</div>
			<div class="mod mod1 parent-table">
				<div class="mod-header radius">
					<h2>
						新增用户明细<a class="icon help poptips" action-frame="tip_newDetail"
							title=""></a>
					</h2>
					<div class="option" style="display:none;">
						<span class="icon export exportCsv" title="导出"></span>
					</div>
				</div>
				<div class="mod-body " id="data-load">
					<table class="data-load " width="100%" border="0" cellspacing="0">
						<thead>
							<tr>
								<th>日期</th>
								<th>新增用户/活跃用户</th>
								<th>新增用户占比</th>
							</tr>
						</thead>
						<tbody id="data-list">
						</tbody>
					</table>
					<div class="wait-load">
						<img src='/static/img/ajax-loader.gif' />
					</div>
				</div>
				<div class="mod-bottom clearfix">
					<div class="fr pagination"></div>
				</div>


			</div>
			<!-- <a class="icon help poptips" action-frame="tipdemo" title="poptip"></a> -->
			<div class="">
				<div id="tip_installation">
					<div id="tip_newTrend" class="tips">
						<div class="corner"></div>
						<p>按日、周或月查看数据可进行版本、渠道和分群的交叉筛选，小时数据最多展示7天并暂时不支持筛选，当日数据无筛选数据。按周（按月）显示新增用户时，界面上用每周的周一（每个月的第一日）来代表该周（该月）。</p>
						<p>
							<span class="highlight">新增用户：</span>第一次启动应用的用户（以设备为判断标准）
						</p>
					</div>
					<div id="tip_newDetail" class="tips">
						<div class="corner"></div>
						<p>
							<span class="highlight">新增用户占比：</span>时段内新增用户/时段内活跃用户
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="hiddenft"></div>

</div>
<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	// page ajax render
	global.renderPage = function() {
		var params = {
			app_id : global.appid,
			start_date : global.pickedStartDay,
			end_date : global.pickedEndDay,
			channels : [ global.filter.channel ],
			versions : [ global.filter.version ],
			segments : [ global.filter.segment ],
			time_unit : $('#installation_period').renderTab('get_status'),
			stats : global.action_stats
		};
		var url = '/trends/';
		render_chart('chartcontainer', '', url + 'load_chart_data.do', params,
				false, {
					'xAxis' : {
						labels : {
							align : 'center'
						}
					}
				});
		$('.parent-table').renderTable({
			url : url + 'load_table_data.do',
			params : params,
			temp : '<tr><td>\${date}</td><td><a href="/trends/newuserdetail.do">\${data}</a></td><td>\${rate}</td></tr>'
		});
		_track_components_usage();
	}
	$(document)
			.ready(
					function() {
						//init time_unit tab
						var time_unit_comp = $('#installation_period');
						time_unit_comp.renderTab({
									data : [
											{
												name : I18n.t('components.filters.time_unit.daily'),
												particle : "daily",
												flag : "true"
											}],
									default_type : "daily",
									callback : function(tar, attr, index, txt) {
										// for hourly data no filter response, disabled 'hourly' channel/version pannel
										global.time_unit = attr;
										if (global.pickedDays != '') {
											_track_userEvent('新功能监测_时间颗粒度', attr, global.pickedDays);
										}
										if (attr == "hourly")
											$('.filteritems').hide();
										else
											$('.filteritems').show();
										if (typeof window.global.renderPage === "function") {
											window.global.renderPage();
										}
									}
								});
						// init filter
						// in daily/weekly/monthly, when selecting filter, disabled hourly, vice-versa.
						$('#filter-version_install')
								.Filter(
										{
											panelid : 'filt-version',
											url : '/trends/load_versions.do?app_id='+global.appid,
											text : I18n.t('components.filters.version'),
											templDefault : '{{if is_shown}}<li><input type="checkbox" id="\${name}" {{if check}}checked=\${check}{{/if}}/>\${name}</li>{{/if}}',
											templSearch : '<li><input type="checkbox" id="\${name}" {{if check}}checked=\${check}{{/if}}/>\${name}</li></li>',
											templchecked : '{{if check}}<li><input type="checkbox" id="\${name}" checked="\${check}"/>\${name}</li>{{/if}}',
											callback : function(inst, data) {
												if (data.check) {
													global.filter.version = data.id;
													initTimeUnit(global.pickedDays);
												} else {
													global.filter.version = '';
													initTimeUnit(global.pickedDays);
												}
												if (typeof window.global.renderPage === "function") {
													window.global.renderPage();
												}
											}
										});
						$('#filter-channel_install')
								.Filter(
										{
											panelid : 'filt-chan',
											url : '/trends/load_channels.do',
											text : I18n.t('components.filters.channel'),
											callback : function(inst, data) {
												if (data.check) {
													global.filter.channel = data.id;
													initTimeUnit(global.pickedDays);
												} else {
													global.filter.channel = '';
													initTimeUnit(global.pickedDays);
												}
												if (typeof window.global.renderPage === "function") {
													window.global.renderPage();
												}
											}
										});
						$('#filter-segment_install')
								.Filter(
										{
											panelid : 'filt-segment',
											url : '/trends/load_segments.do',
											text : I18n
													.t('components.filters.segment'),
											panelTempl : '<div class="filterpanel" style="display:none;"><input type="text" class="input" placeholder="'
													+ I18n
															.t('components.filters.segment_search')
													+ '"/><ul class="filterlist"></ul><div class="load" style="margin:10px auto;text-align:center;display:block;"><img src="/static/img/ajax-loader.gif"/></div><div class="new-segment"><a href="/apps/'+ global.appid +'/segmentations/new" target="_blank">'
													+ I18n
															.t('components.filters.segment_new')
													+ '</a></div><div class="submitpanel"><a href="#" class="submit">'
													+ I18n
															.t('components.buttons.confirm')
													+ '</a></div></div>',
											callback : function(inst, data) {
												if (data.check) {
													global.filter.segment = data.id;
													initTimeUnit(global.pickedDays);
												} else {
													global.filter.segment = '';
													initTimeUnit(global.pickedDays);
												}
												if (typeof window.global.renderPage === "function") {
													window.global.renderPage();
												}
											}
										});
						// registrate time_unit to components centre
						window.global.components['default:time_unit'] = time_unit_comp;
					});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>