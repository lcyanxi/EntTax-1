var cacheChartData = function(id){
  var t = this;
  var a = "0";
  //id to jQobj
  var Cid = $('#'+id);
  var contrastNum = {};
  //add a new data to chart.data()
  this.addNewChartData = function(id,series,cacheSeries){
    var searchArr = function(arr,str){
      for(i in arr){
        if(arr[i] == str)
        return true;//already have this data
      }
      return false;
    };
    
    if(cacheSeries == undefined){
      var s = t.getSeries();
      
    }else{
      var s = cacheSeries.series;
    }

    if(!searchArr(umeng.ccd.getData(id), series.name)){
      s.push(series);
    }
    return s;
  };
  this.getSeries = function(){
    var curChart = Cid.data('current_chart');
    var curChartObj = Cid.data(curChart);
    return curChartObj.series;
  };
  
  this.init = function(type){
    switch (type){
      case 'get':
      break;
      case 'set':
      break;
    }
  };
  return {
    contrastNum: contrastNum,
    getData: function(chartid){
      var cid = $('#'+ chartid);
      var c = cid.data('current_chart');
      var arr = [];
      var dataObj = cid.data(c).series;
      for(i in dataObj){arr.push(dataObj[i].name)}
      return arr;
    },
    setData: function(id,stats,cacheSeries){
      var log = t.addNewChartData(id,stats,cacheSeries);
    }
  }
};
//var ccd = new cacheChartData();
//md.init();
