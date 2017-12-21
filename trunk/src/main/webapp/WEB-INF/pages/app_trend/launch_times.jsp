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
		<jsp:param name="menu1" value="app_trend" />
		<jsp:param name="menu2" value="Launch" />
	</jsp:include>


	<div id="mainContainer">
		<div class="contentCol">

			<input type="hidden" value="launches" id="action_stats" />

			<div class="operations">
				<div class="bd3">
					<div class="leftCol"></div>
					<div class="contentCol">

						<div class="filterPanel">
							<ul class="filteritems borders clearfix">
								<li class="items itemsfirst  "><span class="ell" id="filter-version_launch">版本 </span></li>
								<li class="items  "><span class="ell" id="filter-channel_launch">渠道</span></li>
								<li class="items itemslast" style="display: none;"><span class="ell" id="filter-segment_launch"><b class="newV fr">new</b>用户群</span></li>
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
						启动次数趋势<a class="icon help poptips" action-frame="tip_launchTrend" title=""></a>
					</h2>
					<div class="option">
						<div class="particle">
							<ul id="period_launch"></ul>
						</div>
					</div>
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="chartpanel">
							<div id="chartcontainer" style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
						<div class="contrastpanel">
							<a href="#" class="constr borders" id="constr-date">对比时段</a>
						</div>
					</div>
				</div>
			</div>

			<div class="mod mod1 parent-table" id="launch-detail-table">
				<div class="mod-header radius">
					<h2>
						启动次数明细<a class="icon help poptips" action-frame="tip_launchDetail" title=""></a>
					</h2>
					<div class="option" style="display: none;">
						<span class="icon export exportCsvLaunch" title="导出"></span>
					</div>
				</div>
				<div class="mod-body " id="data-load">
					<table class="data-load " width="100%" border="0" cellspacing="0">
						<thead>
							<tr>
								<th>日期</th>
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
			<!-- <a class="icon help poptips" action-frame="tipdemo" title="poptip"></a> -->
			<div class="">
				<div id="tip_launch">
					<div id="tip_launchTrend" class="tips">
						<div class="corner"></div>
						<p>按日、周或月查看数据可进行版本、渠道和分群的交叉筛选，小时数据最多展示7天并暂时不支持筛选，当日数据无筛选数据。按周（按月）显示启动次数时，界面上用每周的周一（每个月的第一日）来代表该周（该月）。</p>
						<p>
							<span class="highlight">启动次数：</span>
							在iOS4.x之后的系统，由于iOS开始支持后台运行，进入后台即算是当前统计会话结束。当再次进入前台时，算作一次新的启动行为，并开始新的统计会话。
						</p>
					</div>
					<div id="tip_launchDetail" class="tips">
						<div class="corner"></div>
						<p>
							<span class="highlight">启动次数占比：</span>指定时间段内启动次数占该时间段总启动次数的比例
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
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	  $('#period_launch').renderTab({
	    data: [
	      { name: I18n.t('components.filters.time_unit.daily'), particle: 'daily', flag: 'true' }
	    ],
	    default_type: 'daily',
	    callback: function(tar,attr,index,txt){
	      global.time_unit = attr;
	      // for hourly data no filter response, disabled 'hourly' channel/version pannel
	      if( attr == "hourly" )
	        $('.filteritems').hide();
	      else
	        $('.filteritems').show();
	      var time_opt = { "hourly":I18n.t('components.filters.time_unit.hourly'), "daily":I18n.t('components.filters.time_unit.daily') , "weekly":I18n.t('components.filters.time_unit.weekly') , "monthly":I18n.t('components.filters.time_unit.monthly') };
	      window.global.renderPage();
	    }
	  });

	  // init filter
	  // in daily/weekly/monthly, when selecting filter, disabled hourly, vice-versa.
		$('#filter-version_launch').Filter({
			panelid : 'filt-version',
			url : '/trends/load_versions.do?app_id='+global.appid,
			text : I18n.t('components.filters.version'),
			templDefault : '{{if is_shown}}<li><input type="checkbox" id="\${name}" {{if check}}checked=\${check}{{/if}}/>\${name}</li>{{/if}}',
			templSearch : '<li><input type="checkbox" id="\${name}" {{if check}}checked=\${check}{{/if}}/>\${name}</li></li>',
			templchecked :  '{{if check}}<li><input type="checkbox" id="\${name}" checked="\${check}"/>\${name}</li>{{/if}}',
			callback : function(inst,data){
				if(data.check){
					global.filter.version = data.id;
	        initTimeUnit(global.pickedDays);
				}else{
					global.filter.version = '';
	        initTimeUnit(global.pickedDays);
				}
				if( typeof window.global.renderPage === "function" ){
					window.global.renderPage();
				}
			}
		});
		$('#filter-channel_launch').Filter({
			panelid : 'filt-chan',
			url : '/trends/load_channels.do',
			text : I18n.t('components.filters.channel'),
			callback : function(inst,data){
				if(data.check){
					global.filter.channel = data.id;
	        initTimeUnit(global.pickedDays);
				}else{
					global.filter.channel ='';
	        initTimeUnit(global.pickedDays);
				}
				if( typeof window.global.renderPage === "function" ){
					window.global.renderPage();
				}
			}
		});
		$('#filter-segment_launch').Filter({
			panelid : 'filt-segment',
			url : '/trends/load_segments',
			text : I18n.t('components.filters.segment'),
			panelTempl : '<div class="filterpanel" style="display:none;"><input type="text" class="input" placeholder="'+I18n.t('components.filters.segment_search')+'"/><ul class="filterlist"></ul><div class="load" style="margin:10px auto;text-align:center;display:block;"><img src="/static/img/ajax-loader.gif"/></div><div class="new-segment"><a href="/apps/'+ global.appid +'/segmentations/new" target="_blank">'+I18n.t('components.filters.segment_new')+'</a></div><div class="submitpanel"><a href="#" class="submit">'+I18n.t('components.buttons.confirm')+'</a></div></div>',
			callback : function(inst,data){
				if(data.check){
					global.filter.segment = data.id;
	        initTimeUnit(global.pickedDays);
				}else{
					global.filter.segment ='';
	        initTimeUnit(global.pickedDays);
				}
				if( typeof window.global.renderPage === "function" ){
					window.global.renderPage();
				}
			}
		});

	  // register time_unit to components centre
	  window.global.components['default:time_unit'] = $('#period_launch');
	});

	window.global.renderPage = function(){
	  var params = {
	    app_id : global.appid,
	    start_date: global.pickedStartDay,
	    end_date: global.pickedEndDay,
	    channels:[global.filter.channel],
	    versions:[global.filter.version],
	    segments:[global.filter.segment],
	    time_unit: $('#period_launch').renderTab('get_status'),
	    stats:global.action_stats
	  };

	  var url = '/trends/';
	  render_chart('chartcontainer','',url+'load_chart_data.do',params,false, {'xAxis': {labels: {align: 'center'}}});
	  $('.parent-table').renderTable({ url: url+'load_table_data.do', params: params,
	                temp : '<tr><td>\${date}</td><td>\${data}</td><td>\${rate} %</td></tr>'
	  });
	  _track_components_usage();
	};

	$('.exportCsvLaunch').bind('click',function(e){
	  $(this)._export(e,{time_unit: $('#period_launch').renderTab('get_status')});
	  var pageName = $('#siderNav li.on a').attr('page_id');
	  var tableName = $(this).parents('.mod-header').find('h2').text() + $('#period_launch').renderTab('get_status');
	  _gaq.push(['_trackEvent', '导出报表', pageName, tableName]);
	});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>