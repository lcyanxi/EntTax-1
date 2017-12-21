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
		<jsp:param name="menu1" value="page_function" />
		<jsp:param name="menu2" value="Event" />
	  </jsp:include>
	
<div id="mainContainer">
<div class="contentCol">
<div class="operations">
	<div class="bd3">
		<div class="leftCol clearfix">
      <div class="fl">
        <div class="mod-select mod-select-default" id="eventgroup">
  <h4 class="select-head">
    <div class="head-panel">
      <span class="selected" id="${id }" title="${eventName}">${eventName }</span>
      <b class="icon pulldown fr"></b>
    </div>
  </h4>
  <div class="select-body">
    <ul class="select-list">
    </ul>
    <div class="load" style="margin:10px auto;text-align:center;"><img src="/static/img/ajax-loader.gif"/></div>
  </div>
</div>

      </div>
      <div class="fl margin-l-1">
        
        <div class="plugin-tabs" id="ekvType">
          <ul class="clearfix">
          </ul>
        </div>
      </div>
		</div>
		<div class="contentCol">								
			
<div class="filterPanel">
  <!-- for .filteritems has a style, when hiding both, skip it -->
    <ul class="filteritems borders clearfix">
        <li class="items itemsfirst  ">
          <span class="ell" id="filter-version">版本 </span>
        </li>
    </ul>
      <div class="datepickerPanel custom1 borders">
        <div class="dateselect" id="date-select"><span class="start">${startDate }</span> - <span class="end">${endDate }</span><b class="icon pulldown"></b></div>
      </div>
</div>

		</div>
	</div>
</div>

<div class="mod mod1">
	<div class="mod-header radius clearfix">
		<h2>事件统计<a class="icon help poptips" action-frame="tip_eventStat" title=""></a></h2>
		<div class="option">
		</div>
	</div>
	<div class="mod-body">
		<div class="content">
			<div class="tabpanel">
				<ul id="tabpanel_items" class="borders">
				</ul>
			</div>
			<div class="chartpanel">
				<div id="chartcontainer" style="min-width: 400px; height: 300px; margin: 0 auto;"></div>
			</div>
			
		</div>
	</div>
</div>

<div class="mod mod1 clearfix" id="table-chartDetails">
  <div class="mod-header radius clearfix">
    <h2>事件统计明细</h2>
  </div>
  <div class="mod-body " id="data-load">
  <table class="data-load " width="100%" border="0" cellspacing="0">
    <thead>
      <tr>
          <th>日期</th>
          <th>消息数量</th>
          <th>消息数/启动次数</th>
          <th>独立用户数</th>
          <th>独立用户数/活跃用户</th>
      </tr>
    </thead>
    <tbody id="data-list">
    </tbody>
  </table>
  <div class="wait-load"><img src= '/static/img/ajax-loader.gif'/></div>
</div>
<div class="mod-bottom clearfix">
    <div class="fr pagination"></div>
</div>


</div>


<!-- <a class="icon help poptips" action-frame="tipdemo" title="poptip"></a> -->
<div class="">
  <div id="tip_eventDetail">
    <div id="tip_eventStat" class="tips">
      <div class="corner"></div>
      <p><span class="highlight">事件消息数：</span>事件被触发的次数</p>
      <p><span class="highlight">消息数/启动次数：</span>平均每次启动被触发的次数</p>
      <p><span class="highlight">独立用户数：</span>每日触发事件的独立用户数（以设备为判断标准）</p>
      <p><span class="highlight">持续时长：</span>事件持续的时间长度</p>
    </div>
    <div id="tip_eventStat_num" class="tips">
      <div class="corner"></div>
      <p><span class="highlight">消息数量：</span>该数值型参数被触发的次数</p>
      <p><span class="highlight">独立用户数：</span>每日触发该参数的独立用户数（以设备为判断标准）</p>
      <p><span class="highlight">累计值：</span>每日触发的参数值的加和</p>
      <p><span class="highlight">单次均值：</span>累计值/消息数量</p>
      <p><span class="highlight">单用户均值：</span>累计值/独立用户数</p>
    </div>
    <div id="tip_paramStat" class="tips">
      <div class="corner"></div>
      <p>您可以为事件设置参数，丰富自定义统计的内容，每个事件至多同时传10个参数，每个参数至多有1000个取值。</p>
    </div>
    <div id="tip_quantile" class="tips">
      <div class="corner"></div>
      <p><span class="highlight">按次数的分位数：</span>当日该参数每次发生的数值由小到大排列后第p%的数值</p>
      <p><span class="highlight">按人数的分位数：</span>当日该参数按独立设备的累计数值由小到大排列后第p%的数值</p>
    </div>
    <div id="tip_contribute" class="tips">
      <div class="corner"></div>
      <p><span class="highlight">按次数的累计值贡献：</span>（x%,y）表示单次取值小于y的所有次数的累计值贡献了总体累计值的x%</p>
      <p><span class="highlight">按人数的累计值贡献：</span>（x%,y）表示设备参数值加和小于y的所有设备的累计值贡献了总体累计值的x%</p>
    </div>
  </div>
</div>


</div>
</div>
</div>
<div class="hiddenft"></div>
</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>

    <script type="text/javascript">

	var chart_unit = '';
	var version_id = '${version}';
	global.filter.version = version_id;
  if (version_id != ''){
    $('#filter-version').text(version_id).parent().addClass('on');
  }
	global.renderPage = function(){
		render_chart_by_stat();
		_track_components_usage();
	}
	$('#tabpanel_items').renderTab({
		data: [
		{ name: I18n.t('page_misc.events.count'), particle : "count", flag : "true" , unit:''},
		{ name: I18n.t('page_misc.events.count_div_launches'), particle : "count_over_launch", flag : "true" , unit:''},
		{ name: I18n.t('page_misc.events.user'), particle : "device", flag : "true" ,unit:''},
		{ name: '独立用户数/活跃用户', particle : "user_per_uv", flag : "true" ,unit:''},
		],
		default_type : "count",
		template:'<li particle=\${particle} flag=\${flag} href="#" unit=\${unit}>\${name}</li>',
		callback : function(tar,attr,index,txt){
			chart_unit = tar.attr('unit');
			render_chart_by_stat();
		}
	});


	$('#eventgroup').DownList({
		is_ajax: true,
    search: 'on',
    searchTemp: '<li><a class="event" id="\${id}" title="\${name}" href="/events/detail.do?appId='+global.appid+'&id=\${id}">\${name}</a></li>',
		url: '/events/load_event_groups.do?appId='+global.appid,
		temp:'<li><a class="event" id="\${id}" title="\${name}" re-href=/events/detail.do?appId='+global.appid+'&id=\${id}>\${name}</a></li>',
		callback:function(link){
			location.href = link.attr('re-href') + '&version='+ global.filter.version;
			return false;
		}
	});

	function render_chart_by_stat(){
		var stat = $('#tabpanel_items').renderTab('get_status');
		var params = {start_date: global.pickedStartDay,end_date: global.pickedEndDay,stat:stat,version:global.filter.version,id:'${id}',appId:global.appid};
			render_chart('chartcontainer', '', '/events/detail/load_chart_data.do', params, false, {
			    'xAxis': {
			        labels: {
			            align: 'center'
			        }
			    },
          'yAxis': {
            allowDecimals: true
          }
			});
		Filter().Set($('#const-version'),I18n.t('components.compare.version'));
	  $('#table-chartDetails').renderTable({
	    url: '/events/detail/load_table_data.do',
      temp : '<tr><td>\${date}</td><td>\${count}</td><td>\${count_per_launch}</td><td>\${device}</td><td>\${duration}</td></tr>',
      per_page: 1000,
      params: {
				start_date: global.pickedStartDay,
				end_date: global.pickedEndDay,
  				version:global.filter.version,
  				id:'${id}',
  				appId:global.appid
      }
	  })
	}

</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>