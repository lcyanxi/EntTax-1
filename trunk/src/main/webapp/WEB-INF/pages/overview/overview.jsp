<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../includes/header.jsp"></jsp:include>

<div class="bd clearfix">

	<jsp:include page="../includes/left_menu.jsp">
		<jsp:param name="menu1" value="overview" />
	</jsp:include>

	<div id="mainContainer">
		<div class="contentCol">
			<input type="hidden" value="index" id="action_stats" />
			<div class="mod mod1" id="today_table">
				<div class="mod-header radius">
					<h2>
						数据对比<a class="icon help poptips" action-frame="tip_todayData"
							title=""></a>&nbsp;&nbsp;
					</h2>
				</div>
				<div class="mod-body " id="data-load">
					<table class="data-load " width="100%" border="0" cellspacing="0">
						<thead>
							<tr>
								<th></th>
								<th>累计用户</th>
								<th>新增用户</th>
								<th>活跃用户</th>
								<th>新增用户比例</th>
								<th>启动次数</th>
								<th>平均单次使用时长(秒)</th>
							</tr>
						</thead>
						<tbody id="data-list">
						</tbody>
					</table>
					<div class="wait-load" style="display: none;">
						<img src='/static/img/ajax-loader.gif' />
					</div>
				</div>


			</div>
			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>
						时段分析<a class="icon help poptips" action-frame="tip_periodAna"
							title=""></a>
					</h2>
<!-- 					<div class="option">
						<span class="icon export" id="export_hours" title="导出"></span>
					</div> -->
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="tabpanel">
							<ul id="today_tabpanel_items" class="borders">
							</ul>
						</div>
						<div class="chartpanel">
							<div id="today_chartcontainer"
								style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
						<div class="contrastpanel">
							<a href="#" class="constr borders" id="daily_constr_date">对比时段</a>
							<div id="today_constr_date_popform" class="mod singledate"
								style="display: none;">
								<div class="mod-body"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>
						整体趋势<a class="icon help poptips" action-frame="tip_appTrend"
							title=""></a>
					</h2>
					<div class="option">
						<div class="particle">
							<ul id="trends_period"></ul>
						</div>
					</div>
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="tabpanel">
							<ul id="trends_tabpanel_items" class="borders">
							</ul>
						</div>
						<div class="chartpanel">
							<div id="trends_chartcontainer"
								style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
					</div>
				</div>
				<div class="mod-bottom">
					<div class="hidden" id="totalDetails">
						<div class="mod parent-table">
							<div class="mod-header" style="border-top: 0px">
								<h2>
									整体趋势数据明细<a class="icon help poptips"
										action-frame="tip_appTrend" title=""></a>
								</h2>
<!-- 								<div class="option">
									<span class="icon export" id="export_trends"></span>
								</div> -->
							</div>
							<div class="mod-body " id="data-load">
								<table class="data-load " width="100%" border="0"
									cellspacing="0">
									<thead>
										<tr>
											<th>日期</th>
											<th>新增用户</th>
											<th>累计用户</th>
											<th>活跃用户</th>
											<th>活跃用户构成</th>
											<th>启动次数</th>
											<th>平均单次使用时长(秒)</th>
<!-- 											<th>平均上传流量(KB)</th>
											<th>平均下载流量(KB)</th> -->
										</tr>
									</thead>
									<tbody id="data-list">
									</tbody>
								</table>
								<div class="wait-load">
									<img src='/static/img/ajax-loader.gif' />
								</div>
							</div>
							<div class="mod-bottom">
								<div class="fr pagination"></div>
							</div>


						</div>
					</div>
					<div class="mod mod1 showdetails">
						<a href="#" class="expandCollapse" expand-id="totalDetails">展开明细数据</a>
					</div>
				</div>
			</div>
<!-- 			<div class="fix-column clearfix" m="15">
				<div class="col">
					<div class="mod mod1" id="scale_table">
						<div class="mod-header radius">
							<h2>
								应用规模<a class="icon help poptips" action-frame="tip_appScale"
									title=""></a>
							</h2>
						</div>
						<div class="mod-body " id="data-load">
							<table class="data-load " width="100%" border="0" cellspacing="0">
								<thead>
									<tr>
									</tr>
								</thead>
								<tbody id="data-list">
								</tbody>
							</table>
							<div class="wait-load">
								<img src='/static/img/ajax-loader.gif' />
							</div>
						</div>


					</div>
				</div>
				<div class="col">
					<div class="mod mod1" id="summary_table">
						<div class="mod-header radius">
							<h2>
								使用摘要<a class="icon help poptips" action-frame="tip_useSum"
									title=""></a>
							</h2>
						</div>
						<div class="mod-body " id="data-load">
							<table class="data-load " width="100%" border="0" cellspacing="0">
								<thead>
									<tr>
									</tr>
								</thead>
								<tbody id="data-list">
								</tbody>
							</table>
							<div class="wait-load">
								<img src='/static/img/ajax-loader.gif' />
							</div>
						</div>


					</div>
				</div>
			</div> -->

			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>
						Top 版本<a class="icon help poptips" action-frame="tip_top10Ver"
							title=""></a>
					</h2>
<!-- 					<div class="option">
						<a href="/apps/a20000aac57fc2112a949bd4/reports/app_version">详情</a>
					</div> -->
				</div>
				<div class="mod-body">
					<div class="content">
						<div class='tabpanel'>
							<ul id='top10_version_tabpanel' , class='borders'>
							</ul>
						</div>
						<div class="chartpanel">
							<div id="version_chartcontainer"
								style="min-width: 400px; height: 400px; margin: 0 auto"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>
						Top 渠道<a class="icon help poptips" action-frame="tip_top10Chan"
							title=""></a>
					</h2>
<!-- 					<div class="option">
						<a href="/apps/a20000aac57fc2112a949bd4/channels">详情</a>
					</div> -->
				</div>
				<div class="mod-body">
					<div class="content">
						<div class='tabpanel'>
							<ul id='top10_channel_tabpanel' , class='borders'>
							</ul>
						</div>
						<div class="chartpanel">
							<div id="channel_chartcontainer"
								style="min-width: 400px; height: 400px; margin: 0 auto"></div>
						</div>
					</div>
				</div>
			</div>
			<!-- <a class="icon help poptips" action-frame="tipdemo" title="poptip"></a> -->
			<div class="">
				<div id="tip_app_dashboard">
					<div id="tip_todayData" class="tips">
						<div class="corner"></div>
						<p>
							<span class="highlight">新增用户：</span>第一次启动应用的用户（以设备为判断标准）
						</p>
						<p>
							<span class="highlight">活跃用户：</span>启动过应用的用户（去重），启动过一次的用户即视为活跃用户，包括新用户与老用户
						</p>
						<p>
							<span class="highlight">新增用户比例：</span>新增用户占活跃用户的比例，即新增用户/活跃用户
						</p>
						<p>
							<span class="highlight">启动次数：</span>
							启动是通过在所有Activity中调用MobclickAgent.onResume()和MobclickAgent.onPause()方法来监测的。若用户使用过程中进入home或其他程序，经过一段时间间隔后才返回之前的应用，如间隔超过x（x可以由开发者自由设定）秒，则被认为是两次启动。
						</p>
						<p>
							<span class="highlight">平均单次使用时长：</span>单次使用时长的均值，即应用的总使用时长/总启动次数
						</p>
					</div>
					<div id="tip_periodAna" class="tips">
						<div class="corner"></div>
						<p class="margin-bottom">提供了0-24时各时段内新增用户与启动次数的数据，帮您分析用户使用应用的时段规律。</p>
						<p>
							<span class="highlight">新增用户：</span>第一次启动应用的用户（以设备为判断标准）
						</p>
						<p>
							<span class="highlight">启动次数：</span>
							启动是通过在所有Activity中调用MobclickAgent.onResume()和MobclickAgent.onPause()方法来监测的。若用户使用过程中进入home或其他程序，经过一段时间间隔后才返回之前的应用，如间隔超过x（x可以由开发者自由设定）秒，则被认为是两次启动。
						</p>
					</div>
					<div id="tip_appTrend" class="tips">
						<div class="corner"></div>
						<p class="margin-bottom">展示了应用在一段时期内的变化趋势，方便您跟踪和评估应用的运营状况。</p>
						<p>
							<span class="highlight">新增用户：</span>第一次启动应用的用户（以设备为判断标准）
						</p>
						<p>
							<span class="highlight">活跃用户：</span>启动过应用的用户（去重），启动过一次的用户即视为活跃用户，包括新用户与老用户
						</p>
						<p>
							<span class="highlight">活跃用户构成：</span>每日活跃用户中新用户与老用户的分布
						</p>
						<p>
							<span class="highlight">启动次数：</span>
							启动是通过在所有Activity中调用MobclickAgent.onResume()和MobclickAgent.onPause()方法来监测的。若用户使用过程中进入home或其他程序，经过一段时间间隔后才返回之前的应用，如间隔超过x（x可以由开发者自由设定）秒，则被认为是两次启动。
						</p>
						<p>
							<span class="highlight">平均单次使用时长：</span>单次使用时长的均值，即应用的总使用时长/总启动次数





						
						<p>
							<span class="highlight">平均上传流量：</span>平均每次使用应用发送到网络的数据流量（单位：KB），只支持Android平台
						</p>
						<p>
							<span class="highlight">平均下载流量：</span>平均每次使用应用接收网络传回的数据流量（单位：KB），只支持Android平台
						</p>
						</p>
					</div>
					<div id="tip_appScale" class="tips">
						<div class="corner"></div>
						<p>
							<span class="highlight">累计用户：</span>截止到现在，启动过应用的所有独立用户（去重，以设备为判断标准）
						</p>
						<p>
							<span class="highlight">过去7天活跃用户：</span>过去7天启动过应用的用户（去重），启动过一次的用户即视为活跃用户，包括新用户与老用户
						</p>
						<p>
							<span class="highlight">过去7天活跃占比：</span>过去7天的活跃用户占累计用户的比例
						</p>
						<p>
							<span class="highlight">过去30天活跃用户：</span>过去30天启动过应用的用户（去重），启动过一次的用户即视为活跃用户，包括新用户与老用户
						</p>
						<p>
							<span class="highlight">过去30天活跃占比：</span>过去30天的活跃用户占累计用户的比例
						</p>
					</div>
					<div id="tip_useSum" class="tips">
						<div class="corner"></div>
						<p>
							<span class="highlight">累计启动(人均)：</span>截止到现在，应用的总启动次数（累计启动/累计用户）
						</p>
						<p>
							<span class="highlight">平均单次使用时长：</span>单次使用时长的均值，即应用的总使用时长/总启动次数
						</p>
						<p>
							<span class="highlight">平均日使用时长：</span>日使用时长的均值，即全部用户的日使用时长/总活跃用户
						</p>
						<p>这里展示的是过去7天的平均单次使用时长的均值、过去7天的平均日使用时长的均值。</p>
					</div>
					<div id="tip_top10Ver" class="tips">
						<div class="corner"></div>
						<p>新增用户展示的是按今日新增排名Top5版本的今日与昨日用户新增情况。活跃用户展示的是按今日活跃排名的Top5版本的今日与昨日用户活跃情况。累计用户展示的是截至今日Top10版本的累计用户数。</p>
						<p>通过详情可以跳转到版本分布页面。</p>
					</div>
					<div id="tip_top10Chan" class="tips">
						<div class="corner"></div>
						<p>新增用户展示的是按今日新增排名Top5渠道的今日与昨日用户新增情况。活跃用户展示的是按今日活跃排名的Top5渠道的今日与昨日用户活跃情况。累计用户展示的是截至今日Top10渠道的累计用户数。</p>
						<p>通过详情可以跳转到渠道列表页面。</p>
					</div>
				</div>
			</div>


		</div>
	</div>
</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	var umeng = {};
	global.fixedStartDay = "${startDay}";
	global.fixedEndDay = "${endDay}";
//	global.appid; //已存在
	$('#totalDetails .mod-bottom').addClass('clearfix').css('margin-bottom',
			'10px');

	renderTodayChart = function(date, is_compared, callback) {
		var stats = $('#today_tabpanel_items').renderTab('get_status');
		var url = '/json/reports/';
		if(date.length ==0){
	       var oDate = new Date();
	       var month = oDate.getMonth() + 1;
	       if (month <= 9)
	           month = "0" + month
	       var day = oDate.getDate();
	       if (day <= 9) 
	          day = "0" + day; 
	       var date1 = oDate.getFullYear() + "-" + month + "-" + day; 
		   global.contrast.push(date1);
		   
		   var d = new Date();  
		   d.setDate(d.getDate()-1);//获取AddDayCount天后的日期  
	       var month = d.getMonth() + 1;
	       if (month <= 9)
	           month = "0" + month
	       var day = d.getDate();
	       if (day <= 9) 
	          day = "0" + day; 
	       var date2 = d.getFullYear() + "-" + month + "-" + day; 
		   global.contrast.push(date2);
		   date=date1+","+date2;
		}
		var params = {
			date : date,
			time_unit : 'hourly',
			is_compared : is_compared,
			app_id : global.appid,
			stats : global.action_stats + '_hours_' + stats
		};
		render_chart('today_chartcontainer', '', url + 'load_chart_data.do',
				params, false, {'xAxis': {labels: {align: 'center'}}}, callback);
	};

	renderVersionChart = function(metric) {
		var url = '/json/reports/';
		var params = {
			metric : metric,
			app_id : global.appid,
			stats : global.action_stats + '_versions'
		};

		render_chart('version_chartcontainer', '', url + 'load_chart_data.do',
				params, false, {
					chart : {
						type : 'bar'
					},
					tooltip : {
						  formatter: function() { return this.x + "<br />"+$('#top10_version_tabpanel > li.on').text()+": " + this.y + ' | 占比：' + this.key; },
					      followPointer: true,
					      followTouchMove: true
					},
					legend : {
						reversed : true
					},
					plotOptions : {
						series : {
							pointWidth : 15
						}
					}
				});
	};

	renderChannelChart = function(metric) {
		var url = '/json/reports/';
		var params = {
			metric : metric,
			app_id : global.appid,
			stats : global.action_stats + '_channels'
		};

		render_chart('channel_chartcontainer', '', url + 'load_chart_data.do',
				params, false, {
					chart : {
						type : 'bar'
					},
					tooltip : {
						  formatter: function() { return this.x + "<br />"+$('#top10_version_tabpanel > li.on').text()+": " + this.y + ' | 占比：' + this.key; },
					      followPointer: true,
					      followTouchMove: true
					},
					legend : {
						reversed : true
					},
					plotOptions : {
						series : {
							pointWidth : 15
						}
					}
				});
	};

	renderTrendsChart = function() {
		var url = '/json/reports/';
		var stats = $('#trends_tabpanel_items').renderTab('get_status');
		var params = {
			start_date : global.fixedStartDay,
			end_date : global.fixedEndDay,
			app_id : global.appid,
			stats : global.action_stats + '_' + stats
		};
		
		render_chart('trends_chartcontainer', '', url + 'load_chart_data.do',
				params, false);
	}

	renderTrendsTable = function() {
		var url = '/json/reports/';
		var params = {
			start_date : global.fixedStartDay,
			end_date : global.fixedEndDay,
			app_id : global.appid,
			stats : global.action_stats + "_details"
		};

		$('.parent-table')
				.renderTable(
						{
							url : url + 'load_table_data.do',
							params : params,
							temp : '<tr><td>\${date}</td><td>\${install_data}</td><td>\${accumulations}</td><td>\${active_data}</td><td>\${install_rate}</td><td>\${launch_data}</td><td>\${duration_data}</td><td>\${upload_data}</td><td>\${download_data}</td></tr>'
						});
	};

	renderTodayTable = function() {
		var url = '/json/reports/';
		var params = {
				app_id : global.appid,
			    stats : 'index_today'
		};

		$('#today_table')
				.renderTable(
						{
							url : url + 'load_table_data.do',
							params : params,
							temp : '<tr><td>\${date}</td><td>\${sum}</td><td>\${install}</td><td>\${active}</td><td>\${rate}</td><td>\${launch}</td><td>\${average}</td></tr>',
							callback : function(inst, param) {
								var tds = $('#today_table table tbody tr:last-child td');
							}
						});
	}

	renderScaleTable = function() {
		var url = '/json/reports/';
		var params = {
				app_id : global.appid,
			    stats : 'index_scale'
		};

		$('#scale_table').renderTable({
			url : url + 'load_table_data.do',
			params : params,
			temp : '<tr><td>\${date}</td><td>\${data}</td></tr>'
		});
	}

	renderSummaryTable = function() {
		var url = '/json/reports/';
		var params = {
				app_id : global.appid,
			    stats : 'index_summary'
		};

		$('#summary_table').renderTable({
			url : url + 'load_table_data.do',
			params : params,
			temp : '<tr><td>\${date}</td><td>\${data}</td></tr>'
		});
	};

	$(document)
			.ready(
					function() {
						renderTodayTable();

						$('#today_tabpanel_items')
								.renderTab(
										{
											data : [ {
												name : "活跃用户",
												particle : "active",
												flag : "true"
											}, {
												name : "启动次数",
												particle : "launches",
												flag : "true"
											} ],
											default_type : "active",
											callback : function(tar, attr,
													index, txt) {
												var dates1="";
									            for(i in global.contrast){
										              if(i == 0){
										            	  dates1=global.contrast[i];  
										              }else{
															dates1=dates1+","+global.contrast[i];
														}
										        }												
												renderTodayChart(
														dates1,
														false,
														function() {
															var glo = global.contrast.length;
															var dates="";
															var ser = umeng.ccd
																	.getData(
																			'today_chartcontainer')
																	.slice(4);
															if (glo > ser.length) {
																for (i = ser.length; i < glo; i++) {
																	dates=dates+","+global.contrast[i];
																}
															}
														});
											}
										});
						umeng.ccd = new cacheChartData('today_chartcontainer');
						//add contrast daily date
						$('#daily_constr_date').click(function() {
							var panel = $('#today_constr_date_popform');
							
							panel.show();
							panel.find('.mod-body').datepicker({
								yearRange : '2000:' + window.thisYear,
								maxDate : +0,
						        onSelect:function(date){
						            panel.hide();
						            var dates;
						            var hasDate=0;
						            for(i in global.contrast){
						              if(global.contrast[i] == date){
						            	  hasDate=1;
						              }
						            }
						            dates=date;
						            if(hasDate<1){
						            	global.contrast.push(date);
						            }
						            
						            for(i in global.contrast){
							              if(global.contrast[i] == date){
							              }else{
												dates=dates+","+global.contrast[i];
											}
							        }
						        
						            renderTodayChart(dates, true);
						          }
						        
							})
							return false;
						});

						renderTodayChart("", false);

						// top 10 version chart
						$('#top10_version_tabpanel').renderTab({
							data : [ {
								name : "新增用户",
								particle : "install",
								flag : "true"
							}, {
								name : "活跃用户",
								particle : "active",
								flag : "true"
							}, {
								name : "累计用户",
								particle : "add_up",
								flag : "true"
							} ],
							default_type : "install",
							callback : function(tar, attr, index, txt) {
								renderVersionChart(attr);
							}
						});
						renderVersionChart("install")

						// top 10 channel chart
						$('#top10_channel_tabpanel').renderTab({
							data : [ {
								name : "新增用户",
								particle : "install",
								flag : "true"
							}, {
								name : "活跃用户",
								particle : "active",
								flag : "true"
							}, {
								name : "累计用户",
								particle : "add_up",
								flag : "true"
							} ],
							default_type : "install",
							callback : function(tar, attr, index, txt) {
								renderChannelChart(attr);
							}
						});
						renderChannelChart("install")

						// trends chart
						$('#trends_period')
								.renderTab(
										{
											data : [ {
												name : "过去7天",
												particle : "-7",
												flag : "true"
											}, {
												name : "过去30天",
												particle : "-30",
												flag : "true"
											}, {
												name : "过去90天",
												particle : "-90",
												flag : "true"
											}, {
												name : "过去6个月",
												particle : "-180",
												flag : "true"
											} ],
											default_type : '-7',
											callback : function(tar, attr,
													index, txt) {
//												alert(attr);
												var intdate = parseInt(attr);
												var days = $.GetDate(intdate);
												global.fixedStartDay = days[0];
												global.fixedEndDay = days[1];

												var url = '/json/reports/';
												var stats = $(
														'#trends_tabpanel_items')
														.renderTab('get_status');

												if (stats == 'installationRate') {
													var params = {
														start_date : global.fixedStartDay,
														end_date : global.fixedEndDay,
														stats : global.action_stats
																+ '_' + stats
													};
													render_chart(
															'trends_chartcontainer',
															'',
															url
																	+ 'load_chart_data.do',
															params,
															false,
															{
																yAxis : {
																	labels : {
																		formatter : function() {
																			return this.value;
																		}
																	}
																},
																chart : {
																	type : 'column'
																},
																tooltip : {
																	enabled : true,
																	formatter : function() {
																		return '<b>'
																				+ this.x
																				+ '</b><br/>'
																				+ this.series.name
																				+ ': '
																				+ this.y
																				+ '<br/>'
																				+ '当日活跃: '
																				+ this.point.stackTotal;
																	}
																},
																plotOptions : {
																	column : {
																		stacking : 'normal',
																		dataLabels : {
																			enabled : false,
																			color : (Highcharts.theme && Highcharts.theme.dataLabelsColor)
																					|| 'white'
																		}
																	},
																},
															});
												} else if (stats == 'avgDuration') {
													var params = {
														start_date : global.fixedStartDay,
														end_date : global.fixedEndDay,
														app_id : global.appid,
														stats : global.action_stats
																+ '_' + stats
													};
													render_chart(
															'trends_chartcontainer',
															'',
															url
																	+ 'load_chart_data.do',
															params,
															false,
															{
																yAxis : {
																	labels : {
																		formatter : function() {
																			var hh = Math
																					.floor(this.value / 3600);
																			var mm = Math
																					.floor(this.value % 3600 / 60);
																			var ss = Math
																					.floor(this.value % 60);

																			if (hh == 0) {
																				return mm
																						+ '分'
																						+ ss
																						+ '秒';
																			} else {
																				return hh
																						+ '时'
																						+ mm
																						+ '分'
																						+ ss
																						+ '秒';
																			}
																		}
																	}
																},
																tooltip : {
																	enabled : true,
																	formatter : function() {
																		var hh = Math
																				.floor(this.y / 3600);
																		var mm = Math
																				.floor(this.y % 3600 / 60);
																		var ss = Math
																				.floor(this.y % 60);

																		if (hh == 0) {
																			return this.x
																					+ '日'
																					+ this.series.name
																					+ ' : '
																					+ mm
																					+ '分'
																					+ ss
																					+ '秒';
																		} else {
																			return this.x
																					+ '日'
																					+ this.series.name
																					+ ' : '
																					+ hh
																					+ '时'
																					+ mm
																					+ '分'
																					+ ss
																					+ '秒';
																		}
																	}
																},
															});
												} else {
													renderTrendsChart();
												}

												renderTrendsTable();
												if (attr != -7) {
													$('#totalDetails').find(
															'.pagination')
															.removeClass(
																	'hidden');
													if (!$('#totalDetails')
															.hasClass('hidden')) {
														$('.expandCollapse')
																.parent()
																.css(
																		'border-top',
																		'1px solid #B4B4B4');
													}
												}
											}
										});

						$('#trends_tabpanel_items')
								.renderTab(
										{
											data : [ {
												name : '新增用户',
												particle : 'installation',
												flag : "true"
											}, {
												name : '累计用户',
												particle : 'accumulation',
												flag : "true"
											}, {
												name : '活跃用户',
												particle : 'activeUser',
												flag : "true"
											}, {
												name : '活跃用户构成',
												particle : 'installationRate',
												flag : "true"
											}, {
												name : '启动次数',
												particle : 'launch',
												flag : "true"
											}, {
												name : '平均单次使用时长(秒)',
												particle : 'avgDuration',
												flag : "true"
											}
/* 											,{
												name : '平均上传流量',
												particle : 'traffic_upload',
												flag : "true"
											}, {
												name : '平均下载流量',
												particle : 'traffic_download',
												flag : "true"
											}  */
											],
											default_type : 'installation',
											callback : function(tar, attr,
													index, txt) {
												if (attr == 'installationRate') {
													var url = '/json/reports/';
													var stats = $(
															'#trends_tabpanel_items')
															.renderTab(
																	'get_status');
													var params = {
														start_date : global.fixedStartDay,
														end_date : global.fixedEndDay,
														app_id : global.appid,
														stats : global.action_stats
																+ '_' + stats
													};
													render_chart(
															'trends_chartcontainer',
															'',
															url
																	+ 'load_chart_data.do',
															params,
															false,
															{
																yAxis : {
																	labels : {
																		formatter : function() {
																			return this.value;
																		}
																	}
																},
																chart : {
																	type : 'column'
																},
																tooltip : {
																	enabled : true,
																	formatter : function() {
																		return '<b>'
																				+ this.x
																				+ '</b><br/>'
																				+ this.series.name
																				+ ': '
																				+ this.y
																				+ '<br/>'
																				+ '当日活跃: '
																				+ this.point.stackTotal;
																	}
																},
																plotOptions : {
																	column : {
																		stacking : 'normal',
																		dataLabels : {
																			enabled : false,
																			color : (Highcharts.theme && Highcharts.theme.dataLabelsColor)
																					|| 'white'
																		}
																	},
																},
															});
												} else if (attr == 'avgDuration') {
													var url = '/json/reports/';
													var stats = $(
															'#trends_tabpanel_items')
															.renderTab(
																	'get_status');

													var params = {
														start_date : global.fixedStartDay,
														end_date : global.fixedEndDay,
														app_id : global.appid,
														stats : global.action_stats
																+ '_' + stats
													};
													render_chart(
															'trends_chartcontainer',
															'',
															url
																	+ 'load_chart_data.do',
															params,
															false,
															{
																yAxis : {
																	labels : {
																		formatter : function() {
																			var hh = Math
																					.floor(this.value / 3600);
																			var mm = Math
																					.floor(this.value % 3600 / 60);
																			var ss = Math
																					.floor(this.value % 60);

																			if (hh == 0) {
																				return mm
																						+ '分'
																						+ ss
																						+ '秒';
																			} else {
																				return hh
																						+ '时'
																						+ mm
																						+ '分'
																						+ ss
																						+ '秒';
																			}
																		}
																	}
																},
																tooltip : {
																	enabled : true,
																	formatter : function() {
																		var hh = Math
																				.floor(this.y / 3600);
																		var mm = Math
																				.floor(this.y % 3600 / 60);
																		var ss = Math
																				.floor(this.y % 60);

																		if (hh == 0) {
																			return this.x
																					+ '日'
																					+ this.series.name
																					+ ' : '
																					+ mm
																					+ '分'
																					+ ss
																					+ '秒';
																		} else {
																			return this.x
																					+ '日'
																					+ this.series.name
																					+ ' : '
																					+ hh
																					+ '时'
																					+ mm
																					+ '分'
																					+ ss
																					+ '秒';
																		}
																	}
																},
															});
												} else {
													renderTrendsChart();
												}
											}
										})

						renderTrendsChart();
						renderTrendsTable();

						renderScaleTable();
						renderSummaryTable();
						//export reports
						$('#export_hours')
								.bind(
										'click',
										function(e) {
											$(this)
													._export(
															e,
															{
																'stats' : 'index_hours_csv',
																'url' : '/apps/'
																		+ global.appid
																		+ '/reports/load_chart_data'
															});
										})
						$('#export_trends').bind('click', function(e) {
							$(this)._export(e, {
								'stats' : 'index_trends_csv'
							});
						})
					});
	$('#today_constr_date_popform').bind('click', function(e) {
		stopBubble(e);
	})
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<script src="/static/js/caChartData.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>