function render_column_chart(url,panelid,params){
	var loading_img = $("<div style='text-align:center;padding-top:150px;'><img src='/images/pic/ajax-loader.gif' /></div>");
	$('#'+panelid).html('').append(loading_img);
	$.ajax({
		url : url,
		data : params,
		success : function(resp){
			if(resp.result == "success"){
				var colors = Highcharts.getOptions().colors;
				var categories =resp.categories;
				var name = resp.name;
				var data = resp.data;
				for(i in data){
					data[i].color = colors[i];
					data[i].drilldown.color = colors[i];
				}
				function setChart(chart, name, categories, data, color) {
					chart.xAxis[0].setCategories(categories);
					chart.series[0].remove();
					chart.addSeries({
						name: name,
						data: data,
						color: color || 'white'
					});
				}

				var install_devices;
				if( install_devices != undefined ) return;
				install_devices = new Highcharts.Chart({
					chart: {
						renderTo: panelid,
						type: 'column'
					},
					title: {
						text: ''
					},
					subtitle: {
						text: I18n.t('page_misc.device.column_chart_click')
					},
					xAxis: {
						categories: categories
					},
					yAxis: {
						title: {
							text: ''
						},
						labels: {
				                    formatter: function() {
				                        return this.value +'%'
				                    }
				                }
					},
					legend:{
						enabled:false
					},
					plotOptions: {
						column: {
							cursor: 'pointer',
							point: {
								events: {
									click: function() {
										var drilldown = this.drilldown;
										if (drilldown) { // drill down
											setChart(install_devices, drilldown.name, drilldown.categories, drilldown.data, drilldown.color);
										} else { // restore
											setChart(install_devices, name, categories, data);
										}
									}
								}
							},
							dataLabels: {
								enabled: true,
								color: colors[0],
								style: {
									fontWeight: 'bold'
								},
								formatter: function() {
									return this.y +'%';
								}
							}
						}
					},
					tooltip: {
						formatter: function() {
							var point = this.point,
							s = this.x +':<b>'+ this.y +'%</b><br/>';
							if (point.drilldown) {
								s += I18n.t('page_misc.device.click_check') +' '+ point.category+' '+I18n.t('page_misc.device.dis_detail');
							} else {
								s += I18n.t('page_misc.device.return_summary');
							}
							return s;
						}
					},
					series: [{
						name: name,
						data: data,
						color:  Highcharts.getOptions().colors[7]
					}],
					exporting: {
						enabled: false
					},
					credits:{
						enabled: false
					}
				});
			}else{
				$('#'+panelid).html("<div style='text-align:center;padding-top:150px;'>"+resp.msg+"</div>");
			}
		}
	})
};
