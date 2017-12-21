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
		<jsp:param name="menu2" value="Active" />
	</jsp:include>


	<div id="mainContainer">
		<div class="contentCol">

			<input type="hidden" value="active_users" id="action_stats" />

			<div class="operations">
				<div class="bd3">
					<div class="leftCol"></div>
					<div class="contentCol">

						<div class="filterPanel">
							<!-- for .filteritems has a style, when hiding both, skip it -->
							<ul class="filteritems borders clearfix">
								<li class="items itemsfirst  "><span class="ell" id="filter-version">版本 </span></li>
								<li class="items  "><span class="ell" id="filter-channel">渠道</span></li>
								<li class="items itemslast" style="display: none;"><span class="ell" id="filter-segment"><b class="newV fr">new</b>用户群</span></li>
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
						活跃用户趋势<a class="icon help poptips" action-frame="tip_activeTrend"
							title=""></a>
					</h2>
					<div class="option">
						<div class="particle">
							<ul id="period2"></ul>
						</div>
					</div>
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
						<div class="contrastpanel">
							<a href="#" class="constr borders" id="acitve_user-constr-date">对比时段</a>
						</div>
					</div>
				</div>
			</div>

			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							活跃用户明细<a class="icon help poptips" action-frame="tip_activeTable"
								title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0">
							<thead>
								<tr>
									<th>日期</th>
									<th>活跃用户</th>
									<th>活跃用户占比</th>
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
			
			<div class="">
				<div id="tip_active">
					<div id="tip_activeTrend" class="tips">
						<div class="corner"></div>
						<p>按日查看数据可进行版本、渠道和分群的交叉筛选，当日数据无筛选数据。</p>
						<p>
							<span class="highlight">活跃用户：</span>启动过应用的用户（去重），启动过一次的用户即视为活跃用户，包括新用户与老用户
						</p>
						<!--  
					      <p><span class="highlight">周活跃用户：</span>当周启动过应用的用户（去重），界面显示日期为当周周一</p>
					      <p><span class="highlight">周活跃率：</span>当周活跃用户占累计用户的比例，界面显示日期为当周周一</p>
					      <p><span class="highlight">月活跃用户：</span>当月启动过应用的用户（去重），界面显示日期为当月1日</p>
					      <p><span class="highlight">月活跃率：</span>当月活跃用户占累计用户的比例，界面显示日期为当月1日</p>
      					-->
					</div>

					<div id="tip_activeOverview" class="tips">
						<div class="corner"></div>
						<p>您可以查看昨日活跃/过去7天活跃和昨日活跃/过去30天活跃用户的情况。昨日活跃/过去30天活跃可认为是用户活跃度指数，该指标越接近100%，用户越活跃，粘性越强。当该指标低于20%时，应用的整体服务进入一个衰退期，开发者需要格外注意。</p>
						<p>这里的活跃用户是去重后的活跃用户。</p>
					</div>

					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>
							<span class="highlight">活跃用户占比：</span>指定时间段内活跃用户占该时间段总活跃用户的比例
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
    if(global.pickedDays<7){
        $('#tabpanel_items').html('').hide();
    }else if(global.pickedDays<365 && global.pickedDays>7){
        $.publish('/active_users','weekly');
    }else{
        $.publish('/active_users','monthly');
    }
    
    window.global.renderPage = function(){
        var time_unit = $('#period2').renderTab('get_status');
        var stat = $('#tabpanel_items').renderTab('get_status');
        var stats = global.action_stats;
        if( stat == "rate" && time_unit != "daily" )  stats += "_rate";
        var params = {
        	app_id : global.appid,
            start_date: global.pickedStartDay,
            end_date: global.pickedEndDay,
            channels:[global.filter.channel],
            versions:[global.filter.version],
            segments:[global.filter.segment],
            time_unit:time_unit,
            stats:stats
        };
        var url = '/trends/';
        if(time_unit != 'daily' && stat == 'rate'){
            render_chart('chartcontainer','',url+'load_chart_data.do',params,false,{
                'xAxis': {labels: {align: 'center'}},
                'tooltip':{formatter: function() {  return this.x+': '+this.y+'%';} }
            });
        }else{
            render_chart('chartcontainer','',url+'load_chart_data.do',params,false,{'xAxis': {labels: {align: 'center'}}});
        }
        if(time_unit == "daily"){
            if( $('#data-load thead tr th').size() == 4 )
            $('#data-load thead tr th:eq(3)').remove();
            $('.parent-table').renderTable({url : url+'load_table_data.do',params:params,
                temp : '<tr><td>\${date}</td><td>\${data}</td><td>\${rate} %</td></tr>'
            });
        }else{
            if(time_unit == "weekly")
            if( $('#data-load thead tr th').size() == 3 )
            $('#data-load thead tr').append("<th>"+I18n.t('metrics.active_user.weekly_ratio')+"</th>");
            else
            $('#data-load thead tr th:eq(3)').html(I18n.t('metrics.active_user.weekly_ratio'));
            else
            if( $('#data-load thead tr th').size() == 3 )
            $('#data-load thead tr').append("<th>"+I18n.t('metrics.active_user.monthly_ratio')+"</th>");
            else
            $('#data-load thead tr th:eq(3)').html(I18n.t('metrics.active_user.monthly_ratio'));
            $('.parent-table').renderTable({url : url+'load_table_data.do',params:params,
            temp: '<tr><td>${date}</td><td>${data}</td><td>${rate} %</td><td>${active_user_rate} %</td></tr>'});
        }
        _track_components_usage();
    };

    time_unit_comp = $('#period2');
    window.global.components['default:time_unit'] = time_unit_comp;

    time_unit_comp.renderTab({
        data: [
        { name: I18n.t('components.filters.time_unit.daily'), particle : "daily", flag : "true"}
        ],
        default_type : "daily",
        callback : function(tar,attr,index,txt){
            global.time_unit = attr;
            if(global.pickedDays != ''){
                _track_userEvent('新功能监测_时间颗粒度', attr, global.pickedDays);
            }
            $.publish('/active_users',attr);
            //var flag = global.filter.version != "" || global.filter.channel != "";
            window.global.renderPage();
        }
    });
  
    function renderChartTab(e,period){
        if(period == 'daily'){
            $('#tabpanel_items').html('').hide();
            return false;
        }else{
            $('#tabpanel_items li').remove();
            $('#tabpanel_items').show();
            var text = {
                'weekly':[I18n.t('metrics.active_user.weekly'),I18n.t('metrics.active_user.weekly_rate')],
                'monthly':[I18n.t('metrics.active_user.monthly'),I18n.t('metrics.active_user.monthly_rate')]
            };
            var temp = [
                {name:text[period][0],particle: "gangnam",flag: "true"},
                {name:text[period][1],particle: "rate",flag: "true"}
            ];
            $('#tabpanel_items').renderTab({
                data: temp,
                default_type : "gangnam",
                callback : function(tar,attr,index,txt){
                    window.global.renderPage();
                }
            });
        }
    };
    
    $.subscribe('/active_users',renderChartTab);

    $('#acitve_user-constr-date').Contrast({
        callback : function(arr){
            var stat = $('#tabpanel_items').renderTab('get_status');
            var stats = global.action_stats;
            var curChart = $('#chartcontainer').data('current_chart');
            var ser = $('#chartcontainer').data(curChart);
      
            if( stat == "rate" ) { stats += "_rate" };
            render_chart('chartcontainer','', '/trends/load_chart_data.do',{
                start_date: arr[0],
                end_date: arr[1],
                original_data_count:ser.xAxis.categories.length,
                channels:[global.filter.channel],
                versions:[global.filter.version],
                segments:[global.filter.segment],
                time_unit:$('#period2').renderTab('get_status'),
                stats:stats,
                is_compared:true
            },false);
        }
    });
	});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>

<jsp:include page="../includes/footer.jsp"></jsp:include>