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
			<input type="hidden" value="mall_user_week_order_retentions" id="action_stats" />
			<input type="hidden" value="${client}" id="client" />
			<div class="operations">
				<div class="bd3">
					<div class="leftCol"></div>
					<div class="contentCol" style="width: 720px;">

						<div class="filterPanel">
							<!-- 
							<ul class="filteritems borders clearfix">
								<li class="items itemsfirst  "><span class="ell" id="filter-version">版本 </span></li>
								<li class="items  itemslast"><span class="ell" id="filter-channel">渠道</span></li>
							</ul>
							 -->
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="rentention-date-select">
									<span class="start">${start_date}</span> - <span class="end">${end_date}</span>
									<b class="icon pulldown"></b>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
			<div class="wrap-table">
				<div class="mod mod1" id="rentention-detail">
					<div class="mod-header radius">
						<h2>
							留存用户<a class="icon help poptips" action-frame="tip_retention"
								title=""></a>
						</h2>
						<div class="option">
							<div class="particle">
								<ul id="retention-period"></ul>
							</div>
							<span class="icon export exportCsv hidden" title="导出"></span>
						</div>
					</div>
					<div class="mod-body">
						<table id="retention-table" class="data-load" width="100%"
							border="0" cellspacing="0">
							<thead>
								<tr>
									<th style="width: 150px">首次使用时间</th>
									<th style="width: 100px">新增用户</th>
									<th colspan="9">留存率</th>
								</tr>
								<tr id="daily_after_period" class="after_period_indicator">
									<th colspan="2"></th>
									<th>1天后</th>
									<th>2天后</th>
									<th>3天后</th>
									<th>4天后</th>
									<th>5天后</th>
									<th>6天后</th>
									<th>7天后</th>
									<th>14天后</th>
									<th>30天后</th>
								</tr>
								<tr id="weekly_after_period" class="after_period_indicator">
									<th colspan="2"></th>
									<th>1周后</th>
									<th>2周后</th>
									<th>3周后</th>
									<th>4周后</th>
									<th>5周后</th>
									<th>6周后</th>
									<th>7周后</th>
									<th>8周后</th>
									<th>9周后</th>
								</tr>
								<tr id="monthly_after_period" class="after_period_indicator">
									<th colspan="2"></th>
									<th>1月后</th>
									<th>2月后</th>
									<th>3月后</th>
									<th>4月后</th>
									<th>5月后</th>
									<th>6月后</th>
									<th>7月后</th>
									<th>8月后</th>
									<th>9月后</th>
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
			</div>
			<!-- <a class="icon help poptips" action-frame="tipdemo" title="poptip"></a> -->
			<div class="">
				<div id="tip_return">
					<div id="tip_retention" class="tips">
						<div class="corner"></div>
						<p>某段时间内的新增用户，经过一段时间后，仍继续使用应用的被认作是留存用户；这部分用户占当时新增用户的比例即是留存率。例如，5月份新增用户200，这200人在6月份启动过应用的有100人，7月份启动过应用的有80人，8月份启动过应用的有50人；则5月新增用户一个月后的留存率是50%，二个月后的留存率是40%，三个月后的留存率是25%。</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>

<script type="text/javascript">
	window.global.renderPage = function() {
		var client = $("#client").val();
		var params = {
			app_id : global.appid,
			client : client,
			start_date : global.pickedStartDay,
			end_date : global.pickedEndDay,
			channels : [ global.filter.channel ],
			versions : [ global.filter.version ],
			time_unit : $('#retention-period').renderTab('get_status'),
			stats : global.action_stats
		};
		console.log(params);
		var url = '/trends/';
		$('#rentention-detail').renderTable({
			url : url + 'load_table_data.do',
			params : params,
			temp : {
				id : 'tmplTable'
			},
			per_page : 1000,
			callback : function() {
				
				if(params.time_unit == 'daily'){
					$('.after_period_indicator').hide();
					$('#' + params.time_unit + '_after_period').show();
					console.log('the daily is run');
				} 
				if(params.time_unit == 'weekly'){
					$('.after_period_indicator').hide();
					$('#' + params.time_unit + '_after_period').show();
					console.log('the weekly is run');
				} 
				if(params.time_unit == 'monthly'){
					$('.after_period_indicator').hide();
					$('#' + params.time_unit + '_after_period').show();
					$('#action_stats').attr('installations');
					console.log('the monthly is run');
				}
				
				
			}
		});
	};
	
	function colorGrad(r) {
		if (typeof r == 'undefined') {
			return "x";
		} else {
			var clsGrad = r > 60 ? 1 : (r > 40 ? 2 : (r > 20 ? 3 : 4));
			return clsGrad;
		}
	};
	
	$(function() {
		//init time_unit tab
		var time_unit = $('#retention-period');
		time_unit.renderTab({
			data : [ {
				name : I18n.t('components.filters.time_unit.daily'),
				particle : "daily",
				flag : "true"
			},{
				name : I18n.t('components.filters.time_unit.weekly'),
				particle : "weekly",
				flag : "true"
			},{
				name : I18n.t('components.filters.time_unit.monthly'),
				particle : "monthly",
				flag : "true"
			}],
			default_type : "weekly",
			callback : function(tar, attr, index, txt) {
				global.time_unit = attr;
				console.log('the time_unit: '+global.time_unit);
				window.global.renderPage();
				//if (global.pickedDays != '') {
					// _track_userEvent('新功能监测_时间颗粒度', attr, global.pickedDays + '天');
				// }
				//if (typeof window.global.renderPage === "function") {
					// window.global.renderPage();
				//}
			}
		});
		if (window.global.components['default:time_unit'] === undefined) {
			window.global.components['default:time_unit'] = time_unit;
		}

		//init datepicker
		$('#rentention-date-select').ProSelect(
						{
							inputname : [
									I18n.t('components.filters.time_unit.last_60_day'),
									I18n.t('components.filters.time_unit.last_30_day'),
									I18n.t('components.filters.time_unit.last_7_day') ],
							inputval : [ '-60', '-30', '-7' ],
							callback : function(arr, n) {
								var time_unit_component = window.global.components['default:time_unit'];
								if (time_unit_component != undefined) {
									if (1 <= n && n <= 7) {
										time_unit_component.renderTab(
												'set_status', {
													'disable_arr' : [ 'weekly',
															'monthly' ],
													'able_arr' : [ 'hourly',
															'daily' ],
													'current_item' : 'daily'
												});
									} else if (n > 7 && n <= 30) {
										time_unit_component.renderTab(
												'set_status', {
													'disable_arr' : [ 'hourly',
															'monthly' ],
													'able_arr' : [ 'weekly',
															'daily' ],
													'current_item' : 'daily'
												});
									} else if (n > 30 && n <= 365) {
										time_unit_component
												.renderTab(
														'set_status',
														{
															'disable_arr' : [ 'hourly' ],
															'able_arr' : [
																	'weekly',
																	'daily',
																	'monthly' ],
															'current_item' : 'weekly'
														});
									} else if (n > 365) {
										time_unit_component
												.renderTab(
														'set_status',
														{
															'disable_arr' : [ 'hourly' ],
															'able_arr' : [
																	'weekly',
																	'daily',
																	'monthly' ],
															'current_item' : 'monthly'
														});
									}
								}
								window.global.renderPage();
							}
						});

	});
</script>

<script id="tmplTable" type="text/x-jquery-tmpl">
  <tr>
    <td>\${install_period}</td><td>{{if total_install != null}}\${total_install}{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[0])}">\${retention_rate[0]} {{if retention_rate[0] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[1])}">\${retention_rate[1]} {{if retention_rate[1] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[2])}">\${retention_rate[2]} {{if retention_rate[2] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[3])}">\${retention_rate[3]} {{if retention_rate[3] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[4])}">\${retention_rate[4]} {{if retention_rate[4] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[5])}">\${retention_rate[5]} {{if retention_rate[5] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[6])}">\${retention_rate[6]} {{if retention_rate[6] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[7])}">\${retention_rate[7]} {{if retention_rate[7] != null}}%{{else}}--{{/if}}</td>
    <td class="colorGrad\${colorGrad(retention_rate[8])}">\${retention_rate[8]} {{if retention_rate[8] != null}}%{{else}}--{{/if}}</td>
  </tr>
</script>

<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>