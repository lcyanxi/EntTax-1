<%--
  Created by IntelliJ IDEA.
  User: lcyanxi
  Date: 2017/8/24
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <title>Document</title>
    <%--<meta http-equiv="refresh" content="15;url=/datashow/queryList.do">--%>
    <style type="text/css">
        *{padding: 0; margin: 0;}
        /* 清除浮动 */
        .clearfix:after{clear:both;content:".";display:block;font-size:0;height:0;line-height:0;visibility:hidden}
        .clearfix{zoom:1;}

        .left { float: left ; }
        .right{ float: right; }

        #indexsrh1{ width:100%;}
        .container{
            width: 100%;
        }
        .wrap{
            width:100%;
        }
        .top,.bottom{
            width: 100%;
        }
        .bottom{
            position: relative;
            bottom:40px;
        }
        .top-left,.top-right,.bottom-left,.bottom-right{ width: 25% ;}
        .top-center,.bottom-middle{ width: 50%; }
        .left-top,.left-bottom,.right-top,.right-bottom{ width: 100%;}
        .top-right{color: #fff}
        .right-top-h3{
            line-height:40px;
            padding-left: 41%;
            font-weight: normal;
            color: #EDDBDB;
        }
        .right-bottom h3{
            line-height:40px;
            padding-left: 27%;
            font-weight: normal;
            color: #EDDBDB;
        }
        .right-bottom li{
            width:67%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
        .top-right ol{
            line-height:35px;

        }
        .bottom-middle-left,.bottom-middle-right{ width: 50%; }

        .right-top1{
            padding-left:95px;
            width:90%;
        }
        .right-bottom1{
            padding-left:95px;}
    </style>
</head>
<body>
<div class="container" style="background-color:#1b1b1b;overflow: hidden;">
    <div class="wrap">
        <div class="top clearfix">
            <div class="top-left left">
                <div id="age" class="left-top">left-top</div>
                <div id="sex" class="left-bottom">left-bottom</div>
            </div>
            <div id="indexsrh1" class="top-center left">top-center</div>
            <div class="top-right left">
                <h3 class="right-top-h3">热门搜索词</h3>
                <div id="taboos" class="right-top">
                </div>
                <div id="income" class="right-bottom">
                    <div class="right-bottom1">
                        <h3>热门菜谱</h3>
                        <ol>

                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="bottom clearfix">
            <div id="profession" class="bottom-left left">bottom-left</div>
            <div class="bottom-middle left clearfix">
                <div id="income2" class="bottom-middle-left left">bottom-middle-left</div>
                <div id="sex2" class="bottom-middle-right left">bottom-middle-right</div>
            </div>
            <div id="chron" class="bottom-right left" style="position:relative; bottom:0;">bottom-right</div>
        </div>
    </div>
</div>
<script src="http://i1.douguo.net/static/wap/js/jquery-2.1.4.min.js"></script>

<script src="/datashow/echarts2-2-7/echarts.js"></script>
<script src="/datashow/echarts2-2-7/echarts-all.js"></script>
<script src="/datashow/echarts2-2-7/myTheme.js"></script>
<script type="text/javascript">
    var devWidth = $(document).width();
    var devHeight = $(window).height();
    $(".container").css({
        width: devWidth,
        height: devHeight
    })
    var paddingNum = devWidth/20;
    var paddingTopNum = devWidth/30;
    $(".wrap").css({
        paddingTop: paddingTopNum,
        paddingBottom: 0,
        paddingRight: 0,
        paddingLeft: 0
    })
    $("#indexsrh1").css({
        width: devWidth*0.5,
        height:(devHeight-paddingNum)*(2/3)});
    var devHeight3 = (devHeight-paddingNum)/3;
    $(".left-top").css({height:devHeight3});
    $(".left-bottom").css({height:devHeight3});
    $(".right-top").css({height:devHeight3});
    $(".right-bottom").css({height:devHeight3});
    $(".bottom-left").css({height:devHeight3});
    $(".bottom-middle").css({height:devHeight3});
    $(".bottom-right").css({height:devHeight3});
    $(".bottom-middle-left").css({height:devHeight3});
    $(".bottom-middle-right").css({height:devHeight3});


    var myChart = echarts.init(document.getElementById('indexsrh1'));
    var myContent = echarts.init(document.getElementById('age'));
    var  chron = echarts.init(document.getElementById('profession'));
    var  sex = echarts.init(document.getElementById('sex'));
    var  taboos = echarts.init(document.getElementById('taboos'));
    var  income2 = echarts.init(document.getElementById('chron'));
    var  sex2 = echarts.init(document.getElementById('sex2'));
    var  profession = echarts.init(document.getElementById('income2'));

    //活跃数据
    var placeList="";
    //变化趋势
    var list = [40,-30,40,30,-40,30,20,-50,30,-20,50,20,-60,40,10,30,-20,-40,30,-20,50,-20,-30,10,50,-20,-40,20,40,10,-20,10,30,-40,30,20,-50,30,-20,50,20,-60,40,60,30,-20,-40,30,-20,50,-20,-30,10,50,-20,-40,20,40];
    var i=0;
    getdata();
    getBarData();
    function getdata() {
        $.ajax({
            'type':'GET',
            'dataType':'json',
            'url':'/datashow/queryAllData.do',
            'async' : false,
            'success':function(msg){
                if(msg.data) {
                    var code = msg.data.code
                    placeList =$.parseJSON(code);
                    var lg = placeList.length;
                    var time = 0;
                    draw(placeList,lg,lg,lg);
                    var st = setInterval(function(){
                        var increment = parseInt(Math.random()*list[time]*2);
                        //lg = lg+increment;
                        lg = lg;
                        time = time+1;
                        if(lg>=placeList.length)
                        {
                            lg = placeList.length;
                        }
                        if(time%3==0)
                        {
                            //draw(placeList,lg,300,500);
                        }else if(time%3==1){
                            //  draw(placeList,lg,Math.round(lg/2),1000);
                        }else if(time%3==2){
                            //draw(placeList,lg,Math.round(lg/2),lg);
                        }
                        if(time>10)
                        {
                            clearInterval(st);
                            run();
                        }
                    },1000);
                }
            },
            'error':function (res) {
                /*  alert("后台正在维护中。。。。。。。")*/
                run();
            }
        });



    }
    function getBarData() {
        $.get("/datashow/worldData.do",function(msg){
            barData(msg);
            caipuData(msg);
        });
        var bar = setInterval(function () {
            clearInterval(bar);
            getBarData();
            window.location.reload();

        },30000)
    }
    function run() {
        getdata();
    }
    //地图数据
    function draw(placeList,strlength,strlen1,strlen2) {
        option = {
            backgroundColor: 'transparent',
            color: [
                'rgba(7, 120, 249, 0.7)',
                'rgba(14, 241, 242, 0.7)',
                'rgba(255, 255, 255, 0.7)'
            ],
            title : {
                text: '豆果实时活跃用户',
                subtext: '',
                x:'center',
                textStyle : {
                    color: '#fff',
                    fontSize:24
                }
            },
            legend: {
                orient: 'vertical',
                x:'left',
                data:['强','中','弱'],
                textStyle : {
                    color: '#fff'
                },
                show:false
            },
            geo: {
                itemStyle: {
                    normal:{
                        areaStyle:"#1b1b1b",
                        borderColor: "#262626"
                    }
                }
            },
            toolbox: {
                show : true,
                orient : 'vertical',
                x: 'right',
                y: 'center',
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    restore : {show: true},
                    saveAsImage : {show: true}
                },
                show:false
            },
            series : [
                {
                    name: '弱',
                    type: 'map',
                    mapType: 'china',
                    itemStyle:{
                        normal:{
                            borderColor:'rgba(109,109,109,1)',
                            borderWidth:0.3,
                            areaStyle:{
                                color: '#262b31'
                            }
                        }
                    },
                    data : [],
                    markPoint : {
                        symbol : 'circle',
                        symbolSize: 3,
                        large: true,
                        effect : {
                            show: true,
                            period:10,
                            type: 'bounce',
                            bounceDistance:10,
                            shadowBlur:10
                        },
                        data : (function(){
                            var data = [];
                            var len = strlength;
                            var geoCoord
                            while(len--) {
                                if(placeList[len])
                                {
                                    geoCoord = placeList[len].geoCoord;
                                    data.push({
                                        name : placeList[len % placeList.length].name + len,
                                        value : 1,
                                        geoCoord : [
                                            geoCoord[0],
                                            geoCoord[1]
                                        ]
                                    })
                                }

                            }
                            return data;
                        })()
                    }
                },
                {
                    name: '中',
                    type: 'map',
                    mapType: 'china',
                    data : [],
                    markPoint : {
                        symbol:'circle',
                        symbolSize: 2,
                        large: true,
                        effect : {
                            show: true,
                            type: 'bounce',
                            period:3,
                            shadowBlur:0,
                        },
                        data : (function(){
                            var data = [];
                            var len = strlen1;
                            var geoCoord
                            while(len--) {
                                if(placeList[len])
                                {
                                    geoCoord = placeList[len].geoCoord;
                                    data.push({
                                        name : placeList[len % placeList.length].name + len,
                                        value : 5,
                                        geoCoord : [
                                            geoCoord[0] ,
                                            geoCoord[1]
                                        ]
                                    })
                                }
                            }
                            return data;
                        })()
                    }
                },
                {
                    name: '强',
                    type: 'map',
                    mapType: 'china',
                    hoverable: false,
                    roam:true,
                    data : [],
                    markPoint : {
                        symbol : 'diamond',
                        symbolSize: 1,
                        large: true,
                        effect : {
                            show: true,
                            type: 'bounce',
                            shadowBlur:0,
                            period:7,
                        },
                        data : (function(){
                            var data = [];
                            var len = strlen2;
                            while(len--) {
                                if(placeList[len])
                                {
                                    data.push({
                                        name : placeList[len].name,
                                        value : 9,
                                        geoCoord : placeList[len].geoCoord
                                    })
                                }
                            }
                            return data;
                        })()
                    }
                }
            ]
        };
        myChart.setOption(option);
    }

    //热门搜索词数据
    function caipuData(msg) {
        $("#income ol").html("");
        var view=msg.caipuData.viewCaipu;
        var html = "";
        for(var i=0;i< view.length;i++){
            var k = i+1;
            html = "<li>"+ k + "、"+view[i]+"</li>";
            $("#income ol").append(html);
        }

    }

    //饼图、柱形图数据
    function barData(msg) {
        //扇形图  年龄
        ageOption = {
            title : {
                text: '年龄',
                x:'center',
                textStyle: {
                    fontWeight: 'normal',              //标题颜色
                    color: '#EDDBDB'
                }

            },
            tooltip : {
                trigger: 'axis',
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            grid: {
                show:true,//
                x:120,
                y:80,
                x2:120,
                y2:80,

                borderWidth:'0',//边框宽度
                borderColor:'red',//边框颜色
                backgroundColor:'transparent'//数据域背景颜色
            },
            /*    legend:{
                    show:true ,
                    backgroundColor: 'rgba(0,0,0,0)'
                },*/
            xAxis : [
                {
                    show:true,
                    type : 'category',
                    boundaryGap : false,//x坐标刻度线
                    splitLine:{show:false},
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel: {  //x轴字体颜色
                        show: true,
                        /* interval:0,
                         rotate:-40,//字体斜着展示*/
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    data :msg.barData.ageName
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    splitLine:{show:false},
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel : {  //y轴字体颜色
                        show: false,
                        formatter: '{value}',
                        textStyle: {
                            color: '#fff'
                        }
                    }
                }
            ],
            series : [
                {
                    name:'年龄',
                    type:'bar',
                    barWidth : 35,
                    itemStyle:{
                        normal:{
                            color:'#436C95'
                        }
                    },
                    data:msg.barData.ageData,
                }
            ]
        };
        chron.setOption(ageOption);

        incomeOptions = {
            title : {
                text: '收入',
                x:'center',
                textStyle: {
                    fontWeight: 'normal',              //标题颜色
                    color: '#EDDBDB'
                }

            },
            tooltip : {
                trigger: 'axis',
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            grid: {
                show:true,//
                x:120,
                y:80,
                x2:120,
                y2:80,

                borderWidth:'0',//边框宽度
                borderColor:'red',//边框颜色
                backgroundColor:'transparent'//数据域背景颜色
            },
            /*    legend:{
                    show:true ,
                    backgroundColor: 'rgba(0,0,0,0)'
                },*/
            xAxis : [
                {
                    show:true,
                    type : 'category',
                    boundaryGap : false,//x坐标刻度线
                    splitLine:{show:false},
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel: {  //x轴字体颜色
                        show: true,
                        /* interval:0,
                         rotate:-40,//字体斜着展示*/
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    data :msg.barData.incomeName
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    splitLine:{show:false},
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel : {  //y轴字体颜色
                        show: false,
                        formatter: '{value}',
                        textStyle: {
                            color: '#fff'
                        }
                    }
                }
            ],
            series : [
                {
                    name:'收入',
                    type:'bar',
                    barWidth : 35,
                    itemStyle:{
                        normal:{
                            color:'#436C95'
                        }
                    },
                    data:msg.barData.incomeData
                }
            ]
        };
        income2.setOption(incomeOptions);

        //忌口
        taboosOption= {
            title : {
                text: '忌口分布',
                x:'center',
                textStyle: {
                    fontWeight: 'normal',              //标题颜色
                    color: '#EDDBDB'
                }
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {d}%"
            },
            color:[ '#748695','#5A7C9B', '#46719B','#27629D',
                '#748695','#5A7C9B', '#46719B','#27629D',
                '#748695','#5A7C9B', '#46719B','#27629D',
                '#393939', '#676A6C','#40556A','#525D64',
                '#7CB9F5', '#212E39'],
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel'],
                        option: {
                            funnel: {
                                x: '25%',
                                width: '50%',
                                funnelAlign: 'left',
                                max: 1548
                            }
                        }
                    },
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : false,


            series : [
                {
                    name:'忌口',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '55%'],
                    data:msg.barData.taboosData
                }
            ]
        };
        profession.setOption(taboosOption);


        //职业
        profOption = {
            title : {
                text: '职业',
                x:'center',
                textStyle: {
                    fontWeight: 'normal',              //标题颜色
                    color: '#EDDBDB'
                }

            },
            tooltip : {
                trigger: 'axis'
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },

            calculable : true,
            polar : [
                {
                    nameGap : 2,
                    name:{
                        show: true, // 是否显示工艺等文字
                        formatter: null, // 工艺等文字的显示形式
                        textStyle: {
                            color:'#eee' // 工艺等文字颜色
                        }
                    },
                    splitArea : {
                        show : true,
                        areaStyle : {
                            color: 'transparent'  // 图表背景网格的颜色
                        }
                    },
                    indicator :msg.barData.professionName,
                    radius : 80
                }
            ],
            series : [
                {
                    name: '职业',
                    type: 'radar',
                    itemStyle: {
                        normal: {
                            color:'#436C95',
                            areaStyle: {
                                type: 'default'
                            }
                        }
                    },
                    data : [
                        {value :msg.barData.professionData,
                            name : '职业'}
                    ]
                }
            ]
        };
        sex2.setOption(profOption);

        //慢性病柱形图
        chronOptions = {
            title : {
                text: '慢性病排名',
                x:'center',
                textStyle: {
                    fontWeight: 'normal',              //标题颜色
                    color: '#EDDBDB'
                }
            },
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            grid: {
                show:false,//
                borderWidth:'0',//边框宽度
                borderColor:'#eee'//边框颜色
            },
            calculable : true,
            xAxis : [
                {
                    show : false,
                    type : 'value',
                    splitLine:{show:false},
                    boundaryGap : [0, 0.01],
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//这里是为了突出显示加上的
                        }
                    },
                    axisLabel: {  //x轴字体颜色
                        show: true,
                        textStyle: {
                            color: '#fff'
                        }
                    }
                }
            ],
            yAxis : [
                {
                    show:true,
                    type : 'category',
                    boundaryGap : false,//x坐标刻度线
                    splitLine:{show:false},
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel : {  //y轴字体颜色
                        formatter: '{value}',
                        textStyle: {
                            color: '#fff',
                            fontSize:10
                        }
                    },
                    data : msg.barData.chronName
                }
            ],
            series : [
                {
                    type:'bar',
                    barWidth : 8,
                    itemStyle:{
                        normal:{
                            color:'#436C95'
                        }
                    },
                    data:msg.barData.chronDatas
                }
            ]
        };
        sex.setOption(chronOptions);

        //省份、城市柱形图
        cityOption = {
            title : {
                text: '城市、省份排名',
                x:'center',
                textStyle: {
                    fontWeight: 'normal',              //标题颜色
                    color: '#EDDBDB'
                }
            },
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            grid: {
                show:false,//
                borderWidth:'0',//边框宽度
                borderColor:'red'//边框颜色
            },
            calculable : true,
            xAxis : [
                {
                    show : false,
                    type : 'value',
                    splitLine:{show:false},
                    splitNumber: 10,//修改x轴数值间距
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel: {  //x轴字体颜色
                        show: true,
                        textStyle: {
                            color: '#fff'
                        }
                    }
                }
            ],
            yAxis : [
                {
                    type : 'category',
                    axisTick : {show: false},
                    splitLine:{show:false},
                    position: 'right',
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel : {  //y轴字体颜色
                        formatter: '{value}',
                        textStyle: {
                            color: '#fff',
                            fontSize:10
                        }
                    },
                    data : msg.provCityData.provName

                },
                {
                    type : 'category',
                    axisTick : {show: false},
                    splitLine:{show:false},
                    position: 'left',
                    axisLine:{
                        lineStyle:{
                            color:'#eee',
                            width:0,//去掉x轴、y轴线
                        }
                    },
                    axisLabel : {  //y轴字体颜色
                        formatter: '{value}',
                        textStyle: {
                            color: '#fff',
                            fontSize:10
                        }
                    },
                    data : msg.provCityData.cityName

                }
            ],
            series : [
                {
                    name:'省份',
                    type:'bar',
                    stack: '总量',
                    barWidth : 8,
                    itemStyle: {
                        normal: {
                            color:'#436C95',
                            label : {show: false}
                        }},
                    data:msg.provCityData.provData
                },
                {
                    name:'城市',
                    type:'bar',
                    stack: '总量',
                    barWidth : 8,
                    itemStyle: {normal: {
                        label : {show: false, position: 'left'}
                    }},
                    data:msg.provCityData.cityData
                }
            ]
        };
        myContent.setOption(cityOption);


        var newHotSearch = [];
        var hotSearch = msg.caipuData.hotSearch;
        for(var item in hotSearch){
            newHotSearch.push({
                name:hotSearch[item]['name'],
                value:hotSearch[item]['value'],
                itemStyle:createRandomItemStyle()
            })
        }
        //热门搜索词
        hotOption = {
            tooltip: {
                show: true
            },
            series: [{
                name: '热门搜索词',
                type: 'wordCloud',
                size: ['90%', '90%'],
                textRotation : [0, 45, 90, -45],
                textPadding: 0,
                autoSize: {
                    enable: true,
                    minSize: 1
                },
                data: newHotSearch
            }]
        };
        taboos.setOption(hotOption);

    }

    function createRandomItemStyle() {
        return {
            normal: {
                color: 'rgb(' + [
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160)
                ].join(',') + ')'
            }
        };
    }

</script>
</body>
</html>













