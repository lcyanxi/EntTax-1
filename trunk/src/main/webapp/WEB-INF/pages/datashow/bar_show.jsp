<%--
  Created by IntelliJ IDEA.
  User: lcyanxi
  Date: 2017/8/22
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <meta http-equiv="refresh" content="10;url=/datashow/toDouguoPersonShow.do">
    <style>
        *{
            margin:0;padding:0;
        }
    </style>
    <!-- 引入 echarts.js -->
    <script src="/datashow/echarts3-7/echarts.min.js"></script>
    <script type='text/javascript' src='http://i1.douguo.net/static/wap/js/jquery-2.1.4.min.js'></script>
    <script src="/datashow/echarts3-7/world.js"></script>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="bj" style="background-color:#1b1b1b;overflow: hidden;">
</div>
<!--中国和外国数据-->
<script src="/datashow/echarts3-7/worldDa.js"></script>
<!--外国数据-->
<script src="/datashow/echarts3-7/worldData.js"></script>
<!--中国数据-->
<script src="/datashow/echarts3-7/chinaData.js"></script>
<script type="text/javascript">
    var $width = $(window).width(),$height = $(window).height();
    $("#bj").css({width:$width,height:$height});
    $('#main').css({width:$width*0.9,height:$height*1,"margin-top":$height*0.01});
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('bj'));

    // 指定图表的配置项和数据
    $(document).ready(function () {
            myChart.setOption(
                option = {
                    backgroundColor: '#1b1b1b',
                    title : {
                        text: '豆果用户分布图',
                        left: 'center',
                        top: 'top',
                        textStyle: {
                            color: '#fff',
                            fontSize:24
                        }
                    },
                    tooltip: {},
                    legend: {
                        left: 'left',
                        data: ['', '', ''],
                        textStyle: {
                            color: '#ccc'
                        }
                    },
                    geo: {
                        map: 'world',
                        layoutCenter: ['50%', '50%'],
                        layoutSize: 1800,
                        label: {
                            emphasis: {
                                show: false
                            }
                        },
                        itemStyle: {
                            normal: {
                                areaColor: '#323c48',
                                borderColor: '#111'
                            },
                            emphasis: {
                                areaColor: '#2a333d'
                            }
                        }
                    },
                    series: [
                               {
                               name: '弱',
                               type: 'scatter',
                               coordinateSystem: 'geo',
                               symbolSize: 3,
                               large: true,
                               itemStyle: {
                                   normal: {
                                       shadowBlur: 2,
                                       shadowColor:  'rgba(7, 120, 249, 0.7)',
                                       color:  'rgba(7, 120, 249, 0.7)'
                                   }
                               },
                               data: chinaData
                           },
                        {
                            name: '中',
                            type: 'scatter',
                            coordinateSystem: 'geo',
                            symbolSize: 1.5,
                            large: true,
                            itemStyle: {
                                normal: {
                                    shadowBlur: 5,
                                    shadowColor:  'rgba(14, 241, 242, 0.7)',
                                    color:  'rgba(14, 241, 242, 0.7)'
                                }
                            },
                            data:worldData
                        },
                        {
                            name: '强',
                            type: 'scatter',
                            coordinateSystem: 'geo',
                            symbolSize: 1,
                            large: true,
                            itemStyle: {
                                normal: {
                                    shadowBlur: 2,
                                    shadowColor: 'rgba(255, 255, 255, 0.7)',
                                    color:  'rgba(255, 255, 255, 0.7)'
                                }
                            },
                            data:chinaData
                        }
                    ]
                }
            );

    });

</script>
</body>
</html>

