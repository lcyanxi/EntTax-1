<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../includes/header.jsp"></jsp:include>

<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>

<div class="bd clearfix">

	<jsp:include page="../includes/left_menu.jsp">
		<jsp:param name="menu1" value="Terminal" />
		<jsp:param name="menu2" value="Device" />
	</jsp:include>

	<div id="mainContainer">
		<div class="contentCol">
			<input type="hidden" value="devices_resolutions" id="action_stats" />
			<div class="operations">
				<div class="bd3">
					<div class="leftCol">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=><a href="/terminal/device.do?appId=${globalAppid }&which=devices">机型</a></li>
									<li class=on><a href="/terminal/device.do?appId=${globalAppid }&which=resolutions">分辨率</a></li>
									<li class=><a href="/terminal/device.do?appId=${globalAppid }&which=system">操作系统</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol">

						<div class="filterPanel">
							<!-- for .filteritems has a style, when hiding both, skip it -->
							<ul class="filteritems borders clearfix"  style="display: none;">
								<li class="items itemsfirst  "><span class="ell" id="filter-version">版本 </span></li>
								<li class="items  "><span class="ell" id="filter-channel">渠道</span></li>
								<li class="items itemslast"><span class="ell" id="filter-segment"><b class="newV fr">new</b>用户群</span></li>
							</ul>
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="custom-select">
									<span class="start">${start_date}</span> - <span class="end">${end_date}</span><b
										class="icon pulldown"></b>
								</div>
								<div id="proTemp2" style="display: none;">
									<div class="mod-body"></div>
								</div>								
							</div>
						</div>

					</div>
				</div>
			</div>
			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>
						Top 10 分辨率<a class="icon help poptips"
							action-frame="tip_resolutions" title=""></a>
					</h2>
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="tabpanel">
							<ul id="tabpanel_items" class="borders">
							</ul>
						</div>
						<div class="chartpanel">
							<div id="chartcontainer"
								style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
					</div>
				</div>
			</div>

			<div class="wrap-table">
				<div class="mod mod1 parent-table">
					<div class="mod-header radius">
						<h2>数据明细</h2>
						<div class="option" style="display: none;">
							<a class="icon export exportCsv" title="导出"></a>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0">
							<thead>
								<tr>
									<th>分辨率</th>
									<th>活跃用户</th>
									<th>活跃用户占比</th>
									<th>启动次数</th>
									<th>启动次数占比</th>
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
				<div id="tip_device">
					<div id="tip_resolutions" class="tips">
						<div class="corner"></div>
						<p>您可以查看在指定日期内(活跃用户、启动次数)分辨率的分布情况。</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>
<script src="/static/js/renderColumnChart.js" type="text/javascript"></script>
<script type="text/javascript">
	window.global.renderPage = function(renderTable){
		renderTable = (typeof(renderTable) == 'undefined') ? true : false
		var stats = $('#tabpanel_items').renderTab('get_status');
		var params = {
			app_id : global.appid,
			start_date: global.pickedStartDay,
			end_date: global.pickedEndDay,
			channels:[global.filter.channel],
			versions:[global.filter.version],
            segments:[global.filter.segment],
			time_unit:'daily',
			stats:global.action_stats+'_'+stats,
		};
		var url = '/terminal/';

		// display chart data for iPhone app
		render_chart('chartcontainer','',url+'load_chart_data.do',params,false, {
			chart:{type: 'bar'},
			tooltip:{
				formatter: function() {
					return '' + this.x + ': ' + this.y;
				}
			},
      plotOptions: { series: { pointWidth : 15} }
		});

		if(renderTable) {
			$('.parent-table').renderTable({
				url: url + 'load_table_data.do',
				params: params,
				temp : '<tr><td>\${date}</td><td>\${active_data}</td><td>\${active_rate}%</td><td>\${launch_data}</td><td>\${launch_rate}%</td></tr>'
			});
		}
    _track_components_usage();
	}

	$('#tabpanel_items').renderTab({
		data: [
		{ name: I18n.t('metrics.active_user.name'), particle : "active_user", flag : "true" },
		{ name: I18n.t('metrics.launch.name'), particle : "launches", flag : "true" }
		],
		default_type : "active_user",
		callback : function(tar,attr,index,txt){
			window.global.renderPage(false);
		}
	});

	$('#custom-select').click(function(){
		  var panel =  $('#proTemp2');
		  if(panel.is(':visible')){
		    panel.hide();
		  }else{
		    panel.show();
		  }
		  panel.find('.mod-body').datepicker({
		    yearRange: '2000:'+window.thisYear,
		    maxDate : -1,
		    onSelect:function(dateText, inst,e){
		      $('.dateselect .start').text($.replaceDate(dateText));
		      $('.dateselect .end').text($.replaceDate(dateText));
		      window.global.pickedStartDay = dateText;
		      window.global.pickedEndDay = dateText;
		      window.global.renderPage();
		      panel.hide();
		    }
		  })
		  return false;
	});
	$('#proTemp2').bind('click',function(e){
		  return false;
	});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>