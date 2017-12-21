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
		<jsp:param name="menu1" value="ErrorAnalysis" />
		<jsp:param name="menu2" value="Errors" />
	</jsp:include>

	<div id="mainContainer">
		<div class="contentCol">
			<input type="hidden" value="error" id="action_stats" />
			<div class="operations">
				<div class="bd3">
					<div class="leftCol">
						<div class="mod-select mod-select-default"
							id="error-version-select">
							<h4 class="select-head">
								<div class="head-panel">
									<span class="selected" id="全部版本" title="全部版本">全部版本</span> <b
										class="icon pulldown fr"></b>
								</div>
							</h4>
							<div class="select-body">
								<ul class="select-list">
								</ul>
								<div class="load" style="margin: 10px auto; text-align: center;">
									<img src="/static/img/ajax-loader.gif" />
								</div>
							</div>
						</div>

					</div>
					<div class="contentCol">
						<div class="filterPanel">
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="date-select">
									<span class="start">${start_date}</span> - <span class="end">${end_date}</span><b
										class="icon pulldown"></b>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
			<div class="mod mod1">
				<div class="mod-header radius clearfix">
					<h2>错误分析</h2>
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="chartpanel">
							<div id="chartcontainer"
								style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
					</div>
				</div>
			</div>

			<div class="mod mod1 parent-table error_list">
				<div class="mod-header radius clearfix">
					<h2>错误列表</h2>
				</div>
				<div class="mod-body">

					<table id="data-load" class="data-load" width="100%" border="0"
						cellspacing="0">
						<thead>
							<tr>
								<th>应用版本</th>
								<th>日期</th>
								<th>错误总数</th>
								<th>启动次数</th>
							</tr>
						</thead>
						<tbody id="data-list">
						</tbody>
					</table>
				</div>
				<div class="mod-bottom">
					<div class="fr pagination"></div>
				</div>
				<div class="wait-load">
					<img src='/static/img/ajax-loader.gif' />
				</div>
			</div>
		</div>
	</div>
</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>
<script type="text/javascript">
	window.global.renderPage = function (){
		var params = {
			app_id : global.appid,
			start_date: global.pickedStartDay,
			end_date: global.pickedEndDay,
			versions:[global.filter.version],
			stats:global.action_stats+'_count' 
		};
		var paramstable = {
			app_id : global.appid,
			start_date: global.pickedStartDay,
			end_date: global.pickedEndDay,
			versions:[global.filter.version],
			stats:global.action_stats+'_list'
		};
		var url = '/erroranalysis/';
	    render_chart('chartcontainer','',url+'load_data.do',params,false,{'xAxis': {labels: {align: 'center'}},'yAxis': {allowDecimals: true}});
     	$('.parent-table').renderTable({
	        url : url+'load_data.do',
	        params : paramstable,
	        temp: '<tr><td>\${version}</td><td>\${stat_time}</td><td>\${error_count}</td><td>\${launch_count}</td></tr>'
	 	});
	    _track_components_usage();
	};

	$('#error-version-select').DownList({
		is_ajax: true,
        search: 'on',
        clearFilter: true,
        shiftName: I18n.t('components.filters.all_version'),
        searchTemp:'<li class="event" data-id="\${id}"><a>\${name}</a></li>',
		url: '/trends/load_versions.do?app_id='+global.appid,
		temp: '<li class="event" data-id="\${id}"><a>\${name}</a></li>',
		callback: function(elem) {
			global.filter.version = $(elem).attr('data-id');
        	$('#list_search').remove();
			window.global.renderPage();
        	return false;
		}
	});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>