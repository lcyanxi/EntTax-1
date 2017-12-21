<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../includes/header.jsp"></jsp:include>

<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>      
<div class="bd clearfix" style="overflow:visible;">
<div id="mainContainer"  style="margin-left: 0px;">
<div class="contentCol">
<div class="operations">
  <div class="bd3">
    <div class="clearfix">
      <div class="mod-select mod-select-default fl" id="version-select-cus">
        <h4 class="select-head">
          <div class="head-panel">
            <span class="selected" id="${version}">${verName}</span><b class="icon pulldown fr"></b>
          </div>
        </h4>
        <div class="select-body">
          <ul class="select-list">
          </ul>
          <div class="load" style="margin:10px auto;text-align:center;"><img src="/static/img/ajax-loader.gif"/></div>
        </div>
      </div>
      <!--  事件搜索暂时屏蔽
      <div class="fl margin-l-1">
        <div class="mod-select mod-select-default" id="eventgroup">
            <h4 class="select-head">
                <div class="head-panel">
                    <span class="selected" id="" title="事件搜索">事件搜索</span>
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
      -->
      <div class="contentCol">
						<div class="filterPanel">
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="custom-select">
									<span class="start">${endDate}</span> - <span class="end">${endDate}</span><b class="icon pulldown"></b>
								</div>
								<div id="proTemp2" style="display: none;">
									<div class="mod-body"></div>
								</div>
							</div>

						</div>
					</div>
        </div>
        
  </div>
</div>

<div class="wrap-table">
  <div id="table_evs" class="mod mod1">
    <div class="mod-header radius">
      <h2>事件列表<a class="icon help poptips" action-frame="tip_eventList" title=""></a></h2>
      
    </div>
    <div class="mod-body " id="data-load">
      <table class="data-load tablesorter tableSorter" width="100%" border="0" cellspacing="0">
        <thead>
          <tr>
            <th width="250px">事件ID</th>
            <th width="250px">事件名称</th>
            <th>消息数</th>
            <th>独立用户数</th>
            <!--
            <th width="100px">详情</th>
            -->
          </tr>
        </thead>
        <tbody id="data-list">
        </tbody>
      </table>
      <div class="wait-load"><img src= '/static/img/ajax-loader.gif'/></div>
    </div>
    <div class="mod-bottom">
        <div class="fr pagination"></div>
        <div class="shown_all" style='float: right; font-size: 14px; height: 50px; line-height: 50px;'></div>
    </div>
  </div>
</div>
<!-- <a class="icon help poptips" action-frame="tipdemo" title="poptip"></a> -->
<div class="">
  <div id="tip_events">
    <div id="tip_eventList" class="tips">
      <div class="corner"></div>
      <p class="margin-bottom">如果想使用自定义事件统计，必须先在设置—事件页面注册事件ID。</p>
      <p><span class="highlight">事件ID：</span>在事件管理页面添加的eventID，与代码中的String eventID一致</p>
      <p><span class="highlight">事件名称：</span>为eventID命名，方便管理和使用报表</p>
      <p><span class="highlight">消息数：</span>该事件被触发的次数</p>
      <p><span class="highlight">独立用户数：</span>触发事件的独立用户数（以设备为判断标准）</p>
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
    $('.dateselect .start').text($.replaceDate('${endDate}'));
    $('.dateselect .end').text($.replaceDate('${endDate}'));
    window.global.pickedStartDay = '${endDate}';
    window.global.pickedEndDay = '${endDate}';
  var url = '/events/dashboard/';
  var params = {version:'${version}',appId:global.appid,stat_date:window.global.pickedEndDay};
  $('#table_evs').renderTable({url : url+'load_table_data.do',params : params,
    //temp : '<tr><td title="\${name}"><div class="limit-height">\${name}</div></td><td title="\${display_name}"><div class="limit-height">\${display_name}</div></td><td>\${pv}</td><td>\${uv}</td><td><a href=/events/detail.do?appId='+global.appid+'&id=\${id}&version=${version}>'+I18n.t('components.links.check')+'</a></td></tr>',
    temp : '<tr><td title="\${name}"><div class="limit-height">\${name}</div></td><td title="\${display_name}"><div class="limit-height">\${display_name}</div></td><td>\${pv}</td><td>\${uv}</td></tr>',
    per_page: 1000,
    callback:function(){
      $('.tablesorter').tablesorter({
        headers:{4:{sorter:false}}
      });
    }
  });
  $('#version-select-cus').DownList({
      is_ajax : true,
      search: 'on',
      clearFilter: true,
      shiftName: I18n.t('components.filters.all_version'),
      url : '/events/load_versions.do?appId='+global.appid,
      temp : '<li><a class="event" href="?appId='+global.appid+'&version=\${id}" data-id="\${id}" title="\${name}">\${name}</a></li>',
      callback : function(elem){
          global.filter.version = $(elem).data('id');
      }
  });

  
   $('#eventgroup').DownList({
	is_ajax: true,
    search: 'on',
    searchTemp: '<li><a class="event" id="\${id}" title="\${name}" href="/events/detail.do?appId='+global.appid+'&id=\${id}">\${name}</a></li>',
		url: '/events/load_event_groups.do?appId='+global.appid,
		temp:'<li><a class="event" id="\${id}" title="\${name}" re-href=/events/detail.do?appId='+global.appid+'&id=\${id}>\${name}</a></li>',
		callback:function(link){
			location.href = link.attr('re-href') + '&version='+ $('#version-select-cus').DownList('get').attr('id');
			return false;
		}
	});
  $(document).bind('click',function(){
    $('.panelExportCustom').remove();
  });
	$('.ui-datepicker-calendar').live('click',function(e){
		e.stopPropagation();
	});
	$('.ui-datepicker-header').live('click',function(e){
		e.stopPropagation();
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
		      $('#table_evs').renderTable({url : url+'load_table_data.do',params : {version:'${version}',appId:global.appid,stat_date:window.global.pickedEndDay},
		    	    //temp : '<tr><td title="\${name}"><div class="limit-height">\${name}</div></td><td title="\${display_name}"><div class="limit-height">\${display_name}</div></td><td>\${pv}</td><td>\${uv}</td><td><a href=/events/detail.do?appId='+global.appid+'&id=\${id}&version=${version}>'+I18n.t('components.links.check')+'</a></td></tr>',
		    	    temp : '<tr><td title="\${name}"><div class="limit-height">\${name}</div></td><td title="\${display_name}"><div class="limit-height">\${display_name}</div></td><td>\${pv}</td><td>\${uv}</td></tr>',
		    	    per_page: 1000,
		    	    callback:function(){
		    	      $('.tablesorter').tablesorter({
		    	        headers:{4:{sorter:false}}
		    	      });
		    	    }
		    	  });
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