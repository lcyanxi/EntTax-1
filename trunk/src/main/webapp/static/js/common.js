;$(document).ready(function(){
  //subscribe list 
  $.subscribe("timeUnit", initTimeUnit);
  //init pop frame
  $(document).bind('click',function(){
    window.hidePopFrame();
  });
  //init date filter
  if($('#date-select')){
    $('#date-select').ProSelect({
      callback : function(arr,n){
        um.userCustomDate.setDate(arr);
        //constrain time_unit component
        $.publish("timeUnit",n);
        window.global.renderPage();
      }
    });
  }
  //init version filter
  $('#filter-version').Filter({
    panelid : 'filt-version',
    url : '/trends/load_versions.do?app_id='+global.appid,
    text : '版本',
    templDefault : '{{if is_shown}}<li><input type="checkbox" id="${name}" {{if check}}checked=${check}{{/if}}/>${name}</li>{{/if}}',
    templSearch : '<li><input type="checkbox" id="${name}" {{if check}}checked=${check}{{/if}}/>${name}</li></li>',
    templchecked :  '{{if check}}<li><input type="checkbox" id="${name}" checked="${check}"/>${name}</li>{{/if}}',
    callback : function(inst,data){
      if(data.check){
        global.filter.version = data.id;
      }else{
        global.filter.version = '';
      }
      if( typeof window.global.renderPage === "function" ){
        window.global.renderPage();
      }
    }
  });
  //init channel filter
  $('#filter-channel').Filter({
    panelid : 'filt-chan',
    url : '/trends/load_channels.do',
    text : '渠道',
    callback : function(inst,data){
      if(data.check){
        global.filter.channel = data.id;
      }else{
        global.filter.channel ='';
      }
      if( typeof window.global.renderPage === "function" ){
        window.global.renderPage();
      }
    }
  });
//init focustype filter
  $('#filter-focustype').Filter({
    panelid : 'filt-chan',
    url : '/hiveadmin/load_focustype.do',
    text : '焦点图类型',
    callback : function(inst,data){
    	
      if(data.check){
        global.filter.focustype = data.id;
      }else{
        global.filter.focustype ='';
      }
      if( typeof window.global.renderPage === "function" ){
    	  console.log(window.global);
        window.global.renderPage(true);
      }
    }
  });
  //init segment filter
  $('#filter-segment').Filter({
    panelid : 'filt-segment',
    url : '/apps/load_segments',
    text : '分群',
    panelTempl : '<div class="filterpanel" style="display:none;"><input type="text" class="input" placeholder="搜索全部用户群"/><ul class="filterlist"></ul><div class="load" style="margin:10px auto;text-align:center;display:block;"><img src="/static/img/ajax-loader.gif"/></div><div class="new-segment"><a href="/apps/'+ global.appid +'/segmentations/new" target="_blank">新建分群</a></div><div class="submitpanel"><a href="#" class="submit">确定</a></div></div>',
    callback : function(inst,data){
      if(data.check){
        global.filter.segment = data.id;
      }else{
        global.filter.segment = '';
      }
      if( typeof window.global.renderPage === "function" ){
        window.global.renderPage();
      }
    }
  });
  //init constrast times
  $('#constr-date').Contrast({
    callback : function(arr){
      var time_unit = window.global.components['default:time_unit'];
      var curChart = $('#chartcontainer').data('current_chart');
      var ser = $('#chartcontainer').data(curChart);
      
      render_chart('chartcontainer','', '/trends/load_chart_data.do',{
        start_date: arr[0],
        end_date: arr[1],
        appid: global.appid,
        original_data_count:ser.xAxis.categories.length,
        channels:[global.filter.channel],
        versions:[global.filter.version],
        segments:[global.filter.segment],
        time_unit:time_unit.renderTab('get_status'),
        stats:global.action_stats,
        is_compared:true
      },false);
    }
  });
  //init time_unit tab
  var time_unit = $('#period');
  time_unit.renderTab({
    data: [
    {
      name: "日",
      particle : "daily",
      flag : "true"
    },
    {
      name: "周",
      particle : "weekly",
      flag : "false"
    },
    {
      name: "月",
      particle : "monthly",
      flag : "false"
    }
    ],
    default_type : "weekly",
    callback : function(tar,attr,index,txt){
      global.time_unit = attr;
      if( typeof window.global.renderPage === "function" ){
        window.global.renderPage();
      }
    }
  });
  // registrate date selector to components centre
  if( window.global.components['default:time_unit'] === undefined ){
    window.global.components['default:time_unit'] = time_unit;
  }

  // init version down-selector
  $('#version-select').DownList({
    is_ajax : true,
    search: 'on',
    url : '/apps/'+ global.appid + '/load_versions?show_all=true',
    temp : '<li><a class="event" href="?version=${id}" data-id="${id}" title="${name}">${name}</a></li>',
    callback : function(elem){
      global.filter.version = $(elem).data('id');
    }
  });

  //init page ajax
  if( typeof window.global.renderPage === "function" ){
    //publish timeunit
    var number = um.userCustomDate.getDate();
    $.publish("timeUnit",$.GetDate(number[0],number[1]));
    window.global.renderPage();
  }
  
  
});


