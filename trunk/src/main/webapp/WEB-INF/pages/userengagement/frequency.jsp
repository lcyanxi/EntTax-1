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
		<jsp:param name="menu1" value="user_online" />
		<jsp:param name="menu2" value="Frequency" />
	</jsp:include>

	<div id="mainContainer">
		<div class="contentCol">
			<input type="hidden" value="frequency" id="action_stats" />
			<div class="operations">
				<div class="bd3">
					<div class="leftCol"></div>
					<div class="contentCol">
						<div class="filterPanel">
							<ul class="filteritems borders clearfix">
								<li class="items itemsfirst"><span id="filter-version">版本</span></li>
								<li class="items"><span id="filter-channel">渠道</span></li>
								<li class="items itemslast"  style="display: none;"><span id="filter-segment">用户群<b class="newV">new</b></span></li>
							</ul>
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="custom-select">
									<span class="start">${start_date}</span> - <span class="end">${end_date}</span><b class="icon pulldown"></b>
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
						日启动次数分布<a class="icon help poptips" action-frame="tip_freDistribute" title=""></a>
					</h2>
				</div>
				<div class="mod-body">
					<div class="content">
						<div class="chartpanel">
							<div id="chart_daily"
								style="min-width: 400px; height: 300px; margin: 0 auto"></div>
						</div>
						<div class="contrastpanel">
							<a href="#" class="constr borders" id="comp_daily">对比时段</a>
						</div>
					</div>
				</div>
			</div>
			<div class="wrap-table">
				<div class="mod mod1" id="table_daily">
					<div class="mod-header radius">
						<h2>日启动次数分布明细</h2>
						<div class="option">
							<span class="icon export exportCsv_daily hidden" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0">
							<thead>
								<tr>
									<th>启动次数</th>
									<th>用户数</th>
									<th>用户数比例</th>
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
				<div id="tip_frequency">
					<div id="tip_freDistribute" class="tips">
						<div class="corner"></div>
						<p class="margin-bottom">您可以查看用户在任意1天内日启动次数的分布情况，同时可以对日启动次数的数据进行版本、渠道、分群的交叉筛选。</p>
						<p>
							<span class="highlight">日启动次数：</span>（用户）一天内启动应用的次数
						</p>
					</div>
				</div>
			</div>


		</div>
	</div>
</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>

<script type="text/javascript">
global.renderPage = function(){
  render_chart_by_time_unit('daily', false);
  render_table_by_time_unit('daily');
  _track_components_usage();
}

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
//init constrast times
$('#comp_daily').Contrast({
  panelid: 'panel_comp_daily',
  contrastTo: 'chart_daily',
  callback : function(arr){
    if( typeof window.global.renderPage === "function" ){
      render_chart_by_time_unit('daily',true,arr[0],arr[1]);
    }
  }
});
$('.exportCsv_daily').bind('click',function(e){
  $(this)._export(e,{'time_unit':'daily'});
  var pageName = $('#siderNav li.on a').attr('page_id');
  var tableName = $(this).parents('.mod-header').find('h2').text() + '_daily';
  _gaq.push(['_trackEvent', '导出报表', pageName, tableName]);
})
function render_chart_by_time_unit(time_unit, isCompare,sday,eday){
  var url = '/userengagement/data/';
  var params = {
    start_date: sday || global.pickedStartDay,
    end_date: eday || global.pickedEndDay,
    channels:[global.filter.channel],
    versions:[global.filter.version],
    segments:[global.filter.segment],
    time_unit:time_unit,
    stats:global.action_stats
  };
  if (isCompare){
    params['is_compared'] = true;
  }
  render_chart('chart_'+time_unit,'',url+'load_chart_data.do',params,false,
    {
      'chart':{'defaultSeriesType': 'column'},
      'xAxis': {
        labels: {
          align: 'center'
        }
      },
      'yAxis': {
		    labels: {
          formatter: function() { return this.value +'%' }
        }
	    },
      'tooltip':{formatter: function() {  return this.x+': '+this.y+'%';} }
    }
  );
}
function render_table_by_time_unit(time_unit){
  var url = '/userengagement/data/';
  var params = {start_date: global.pickedStartDay,end_date: global.pickedEndDay,channels:[global.filter.channel],versions:[global.filter.version],segments:[global.filter.segment],time_unit:time_unit,stats:global.action_stats};
  $('#table_'+time_unit).renderTable({url : url+'load_table_data.do',params : params,
    temp : '<tr><td>\${key}</td><td>\${num}</td><td>\${percent}%</td></tr>',
  });
}
</script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>