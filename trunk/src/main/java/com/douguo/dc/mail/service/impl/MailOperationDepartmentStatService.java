package com.douguo.dc.mail.service.impl;

import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.taskmonitor.dao.TaskMonitorDao;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("mailOperationDepartmentStatService")
public class MailOperationDepartmentStatService {

	@Autowired
	private TaskMonitorDao taskMonitorDao;

	public void sendMail(SysMailSet sysMailSet) {
		JavaSendMail javaSendMail = new JavaSendMail();
		try {

			String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
			String subject = "";
			String to = "";
			StringBuffer content = new StringBuffer("");
			// 增加样式
			content.append("<style type=\"text/css\"> " +
							"table.gridtable { " +
								"font-family: verdana,arial,sans-serif; " +
								"font-size:11px; " +
								"color:#333333; " +
								"border-width: 1px; " +
								"border-color: #666666; " +
								"border-collapse: collapse; " +
							"} " +
							"table.gridtable th { " +
								"border-width: 1px; " +
								"padding: 8px; " +
								"border-style: solid; " +
								"border-color: #666666; " +
								"background-color: #dedede; " +
							"} " +
							"table.gridtable td { " +
								"border-width: 1px; " +
								"padding: 8px; " +
								"border-style: solid; " +
								"border-color: #666666; " +
								"background-color: #ffffff; " +
							"} " +
							"table.gridtable tr:nth-child(odd){" +
								"background-color: #CCCCCC;" +
							"}" +
							"</style>");

			if (sysMailSet != null) {
				to = sysMailSet.getMailTo();
				subject = sysMailSet.getSubject() + "_" + curDate;
			} else {
				subject = "豆果运营操作统计记录_" + curDate;
				to = "zhangyaozhou@douguo.com,zhangjianfei@douguo.com";
			}

			// 1. 数据统计
			String gaotieBaseStat = operationStat();
			content.append("<h1>昨日操作统计<h4>（菜谱KPI暂不参与进度统计算法）</h4></h1><br/>");
			content.append(gaotieBaseStat);
			content.append("<br/><br/>");

			content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

			javaSendMail.sendHtmlEmail(subject, to, content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String operationStat() {
		StringBuffer sbContent = new StringBuffer();

		String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
		String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

		// 构造表格
		sbContent.append("<table class=\"gridtable\">");
		// 构造表头
		sbContent.append("<tr>" +
				"<th colspan=\"5\">基础信息</th>" +
				"<th colspan=\"3\">菜谱（周）</th>" +
				"<th colspan=\"3\">作品</th>" +
				"<th colspan=\"3\">帖子</th>" +
//				"<th colspan=\"3\">问答</th>" +
				"<th colspan=\"3\">菜单</th>" +
				"<th colspan=\"3\">菜谱评论</th>" +
				"<th colspan=\"3\">作品评论</th>" +
//				"<th colspan=\"3\">作品点赞</th>" +
				"<th colspan=\"3\">帖子评论</th>" +
				"<th colspan=\"3\">问答回复</th>");
		sbContent.append("<tr>" +
				"<th>时间</th><th>ID</th><th>姓名</th><th>总体进度</th><th>状态</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
//				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
//				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>" +
				"<th width=40px>kpi</th><th style=\"width:40px\">实际</th><th style=\"width:60px\">进度</th>"
		);
//		sbContent.append("<tr>" +
//				"<th>状态</th><th>时间</th>" +
//				"<th>姓名</th><th>ID</th>" +
//				"<th>目标菜谱数</th><th>实际菜谱数</th><th>菜谱完成进度</th>" +
//				"<th>目标作品数</th><th>实际作品数</th><th>作品完成进度</th>" +
//				"<th>目标发帖数</th><th>实际发帖数</th><th>发帖完成进度</th>" +
//				"<th>目标问答帖子</th><th>实际问答帖子</th><th>问答帖子完成进度</th>" +
//				"<th>目标菜单数</th><th>实际菜单数</th><th>菜单完成进度</th>" +
//				"<th>目标菜谱评论数</th><th>实际菜谱评论数</th><th>菜谱评论完成进度</th>" +
//				"<th>目标作品评论数</th><th>实际作品评论数</th><th>作品评论完成进度</th>" +
//				"<th>目标作品点赞</th><th>实际作品点赞</th><th>作品点赞完成进度</th>" +
//				"<th>目标帖子评论</th><th>实际帖子评论</th><th>帖子评论完成进度</th>" +
//				"<th>目标问答回复数</th><th>实际问答回复数</th><th>问答回复完成进度</th></tr>");

		// DAO 获取源数据
		List<Map<String, Object>> maplist = taskMonitorDao.queryAllList(startDate, endDate);

		int lineOddEvnFlag = 0;

		for (Map<String, Object> map : maplist) {
			lineOddEvnFlag ++;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String statdate = sdf.format(map.get("statdate"));
			String name = (String) map.get("name");
			String id = (String) map.get("id");

			String aim_cook_num = (String) map.get("aim_cook_num");
			String cook_num = (String) map.get("cook_num");
			String cook_rate = (String) map.get("cook_rate");

			String aim_dish_num = (String) map.get("aim_dish_num");
			String dish_num = (String) map.get("dish_num");
			String dish_rate = (String) map.get("dish_rate");

			String aim_post_num = (String) map.get("aim_post_num");
			String post_num = (String) map.get("post_num");
			String post_rate = (String) map.get("post_rate");

			String aim_question_num = (String) map.get("aim_question_num");
			String question_num = (String) map.get("question_num");
			String question_rate = (String) map.get("question_rate");

			String aim_caidan_num = (String) map.get("aim_caidan_num");
			String caidan_num = (String) map.get("caidan_num");
			String caidan_rate = (String) map.get("caidan_rate");

			String aim_cookcomment_num = (String) map.get("aim_cookcomment_num");
			String cookcomment_num = (String) map.get("cookcomment_num");
			String cookcomment_rate = (String) map.get("cookcomment_rate");

			String aim_dishcomment_num = (String) map.get("aim_dishcomment_num");
			String dishcomment_num = (String) map.get("dishcomment_num");
			String dishcomment_rate = (String) map.get("dishcomment_rate");

			String aim_dishfav_num = (String) map.get("aim_dishfav_num");
			String dishfav_num = (String) map.get("dishfav_num");
			String dishfav_rate = (String) map.get("dishfav_rate");

			String aim_postcomment_num = (String) map.get("aim_postcomment_num");
			String postcomment_num = (String) map.get("postcomment_num");
			String postcomment_rate = (String) map.get("postcomment_rate");

			String aim_questioncomment_num = (String) map.get("aim_questioncomment_num");
			String questioncomment_num = (String) map.get("questioncomment_num");
			String questioncomment_rate = (String) map.get("questioncomment_rate");

			// 全部完成标记
			int flag = isAllCompleted(cook_rate, dish_rate, post_rate, question_rate,
										caidan_rate,cookcomment_rate, dishcomment_rate,
										dishfav_rate, postcomment_rate, questioncomment_rate);

			// 总体得分
			String scoreDeprecated = isAllCompletedB(cook_rate, dish_rate, post_rate, question_rate,
					caidan_rate,cookcomment_rate, dishcomment_rate,
					dishfav_rate, postcomment_rate, questioncomment_rate);

			String score = isAllCompletedC(cook_rate, dish_rate, post_rate, question_rate,
					caidan_rate,cookcomment_rate, dishcomment_rate,
					dishfav_rate, postcomment_rate, questioncomment_rate);

			sbContent.append("<tr>");

			sbContent.append("<td>" + statdate + "</td>");
			sbContent.append("<td>" + id + "</td>");
			sbContent.append("<td>" + name + "</td>");
			sbContent.append("<td>" + score + "%</td>");
			if (Double.valueOf(score) == 100.0) {
//				sbContent.append("<td style=\"background:green;color:#ffffff\">" + "DONE" + "</td>");
				sbContent.append("<td style=\"border-bottom:solid 1.5px\">" + "DONE" + "</td>");
			}
			if (Double.valueOf(score) < 100.0) {
				sbContent.append("<td style=\"background:#E43252;color:#ffffff;border-bottom:#cccccc solid 1.5px\">" + "TODO" + "</td>");
			}
			if (Double.valueOf(score) > 100.0) {
				sbContent.append("<td style=\"background:#7BA951;color:#ffffff\";border-bottom:#cccccc solid 1.5px\">" + "GOOD" + "</td>");
			}

			sbContent.append("<td width=40px>" + aim_cook_num + "</td>");
			sbContent.append("<td width=40px>" + cook_num + "</td>");
//			sbContent.append(stringRateToDouble(cook_rate));
			// 菜谱指标暂不参与计算与判断，原样输出
			sbContent.append("<td width=60px>" + cook_rate + "</td>");

			sbContent.append("<td width=40px>" + aim_dish_num + "</td>");
			sbContent.append("<td width=40px>" + dish_num + "</td>");
			sbContent.append(stringRateToDouble(dish_rate));

			sbContent.append("<td width=40px>" + aim_post_num + "</td>");
			sbContent.append("<td width=40px>" + post_num + "</td>");
			sbContent.append(stringRateToDouble(post_rate));

//			sbContent.append("<td>" + aim_question_num + "</td>");
//			sbContent.append("<td>" + question_num + "</td>");
//			sbContent.append(stringRateToDouble(question_rate));

			sbContent.append("<td width=40px>" + aim_caidan_num + "</td>");
			sbContent.append("<td width=40px>" + caidan_num + "</td>");
			sbContent.append(stringRateToDouble(caidan_rate));

			sbContent.append("<td width=40px>" + aim_cookcomment_num + "</td>");
			sbContent.append("<td width=40px>" + cookcomment_num + "</td>");
			sbContent.append(stringRateToDouble(cookcomment_rate));

			sbContent.append("<td width=40px>" + aim_dishcomment_num + "</td>");
			sbContent.append("<td width=40px>" + dishcomment_num + "</td>");
			sbContent.append(stringRateToDouble(dishcomment_rate));

//			sbContent.append("<td>" + aim_dishfav_num + "</td>");
//			sbContent.append("<td>" + dishfav_num + "</td>");
//			sbContent.append(stringRateToDouble(dishfav_rate));

			sbContent.append("<td width=40px>" + aim_postcomment_num + "</td>");
			sbContent.append("<td width=40px>" + postcomment_num + "</td>");
			sbContent.append(stringRateToDouble(postcomment_rate));

			sbContent.append("<td width=40px>" + aim_questioncomment_num + "</td>");
			sbContent.append("<td width=40px>" + questioncomment_num + "</td>");
			sbContent.append(stringRateToDouble(questioncomment_rate));

			sbContent.append("</tr>");

//			sbContent.append("<tr>" +
//								"<td style=\"height:12px\" colspan=\"33\"> </td>" +
//							 "</tr>");
		}

		sbContent.append("</table>");
		sbContent.append("<br/>");

		return sbContent.toString();
	}

	// 比率转换table样式
	private static String stringRateToDouble(String aimRate) {
		if ("-".equals(aimRate)){
			String tableRate = "<td width=60px>" + aimRate + "</td>";
			return tableRate;
		} else {
			Double doubleRate = parseRate(aimRate);
			String tableRate = null;
			if (doubleRate<50.0) {
				tableRate = "<td width=60px style=\"color:red\">" + doubleRate + "%</td>";
			}
			if (doubleRate>=50.0 && doubleRate<100.0){
				tableRate = "<td width=60px style=\"color:red\">" + doubleRate + "%</td>";
			}
			if (doubleRate>=100.0){
				tableRate = "<td width=60px style=\"color:#228B22\">" + doubleRate + "%</td>";
			}
			return tableRate;
		}
	}

	// 比率转化
	private static double parseRate(String stringRate){
		String tmpRate = stringRate.replace("%","");
		Double doubleRate = Double.valueOf(tmpRate);
		return doubleRate;
	}

	/**
	 * 指标算法 1
	 * @param cook_rate
	 * @param dish_rate
	 * @param post_rate
	 * @param question_rate
	 * @param caidan_rate
	 * @param cookcomment_rate
	 * @param dishcomment_rate
	 * @param dishfav_rate
	 * @param postcomment_rate
     * @param questioncomment_rate
     * @return
     */
	private static int isAllCompleted(String cook_rate,String dish_rate,String post_rate,String question_rate,
									  String caidan_rate,String cookcomment_rate,String dishcomment_rate,
									  String dishfav_rate,String postcomment_rate,String questioncomment_rate){

//		if ("-".equals(cook_rate)){cook_rate="100.0%";}
		if ("-".equals(dish_rate)){dish_rate="100.0%";}
		if ("-".equals(post_rate)){post_rate="100.0%";}
//		if ("-".equals(question_rate)){question_rate="100.0%";}
		if ("-".equals(caidan_rate)){caidan_rate="100.0%";}
		if ("-".equals(cookcomment_rate)){cookcomment_rate="100.0%";}
		if ("-".equals(dishcomment_rate)){dishcomment_rate="100.0%";}
//		if ("-".equals(dishfav_rate)){dishfav_rate="100.0%";}
		if ("-".equals(postcomment_rate)){postcomment_rate="100.0%";}
		if ("-".equals(questioncomment_rate)){questioncomment_rate="100.0%";}

		if (
//			parseRate(cook_rate) >= 100.0 &&
			parseRate(dish_rate) >= 100.0 &&
			parseRate(post_rate) >= 100.0 &&
//			parseRate(question_rate) >= 100.0 &&
			parseRate(caidan_rate) >= 100.0 &&
			parseRate(cookcomment_rate) >= 100.0 &&
			parseRate(dishcomment_rate) >= 100.0 &&
//			parseRate(dishfav_rate) >= 100.0 &&
			parseRate(postcomment_rate) >= 100.0 &&
			parseRate(questioncomment_rate) >= 100.0
			) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 指标算法 2
	 * @param cook_rate
	 * @param dish_rate
	 * @param post_rate
	 * @param question_rate
	 * @param caidan_rate
	 * @param cookcomment_rate
	 * @param dishcomment_rate
	 * @param dishfav_rate
	 * @param postcomment_rate
     * @param questioncomment_rate
     * @return
     */
	private static String isAllCompletedB(String cook_rate,String dish_rate,String post_rate,String question_rate,
										  String caidan_rate,String cookcomment_rate,String dishcomment_rate,
										  String dishfav_rate,String postcomment_rate,String questioncomment_rate){
		String completedRate = null;
//		String cr = cook_rate.replace("%","");
		String dr = dish_rate.replace("%","");
		String pr = post_rate.replace("%","");
		String crr = caidan_rate.replace("%","");
		String ccrr = cookcomment_rate.replace("%","");
		String drr = dishcomment_rate.replace("%","");
		String prr = postcomment_rate.replace("%","");
		String qrr = questioncomment_rate.replace("%","");

//		String[] hasUndo = {cr,dr,pr,crr,ccrr,drr,prr,qrr};
		String[] hasUndo = {dr,pr,crr,ccrr,drr,prr,qrr};
		ArrayList<String> hasUndoListR = new ArrayList<>();
		ArrayList<String> hasUndoList = new ArrayList<>();

		for (String singleUndo : hasUndo){
			hasUndoList.add(singleUndo);
			if (!"-".equals(singleUndo)){
				hasUndoListR.add(singleUndo);
			}
		}

		Double scoreAll = 0.0;
		for (String singleUndo : hasUndoListR){
			Double tmpSud = Double.valueOf(singleUndo);
			scoreAll = scoreAll + tmpSud;
		}

		completedRate = String.valueOf(Math.round(scoreAll/hasUndoListR.size()));

		return completedRate;
	}


	/**
	 * 指标算法 3
	 * @param cook_rate
	 * @param dish_rate
	 * @param post_rate
	 * @param question_rate
	 * @param caidan_rate
	 * @param cookcomment_rate
	 * @param dishcomment_rate
	 * @param dishfav_rate
	 * @param postcomment_rate
	 * @param questioncomment_rate
	 * @return
	 */
	private static String isAllCompletedC(String cook_rate,String dish_rate,String post_rate,String question_rate,
										  String caidan_rate,String cookcomment_rate,String dishcomment_rate,
										  String dishfav_rate,String postcomment_rate,String questioncomment_rate){

//		String cr = cook_rate.replace("%","");
		String dr = dish_rate.replace("%","");
		String pr = post_rate.replace("%","");
		String crr = caidan_rate.replace("%","");
		String ccrr = cookcomment_rate.replace("%","");
		String drr = dishcomment_rate.replace("%","");
		String prr = postcomment_rate.replace("%","");
		String qrr = questioncomment_rate.replace("%","");

		String[] hasUndo = {dr,pr,crr,ccrr,drr,prr,qrr};
		ArrayList<String> hasUndoListR = new ArrayList<>();
		ArrayList<String> hasUndoList = new ArrayList<>();

		// 过滤有效KPI
		for (String singleUndo : hasUndo){
			hasUndoList.add(singleUndo);
			if (!"-".equals(singleUndo)){
				hasUndoListR.add(singleUndo);
			}
		}

		// 如果所有有效KPI >= 100% , 那么 执行子算法a, 总体进度有机会 >100%
		// 否则执行子算法b,
		int algorithmFlag = 0;
		for (String singleRate : hasUndoListR){
			Double jrate = parseRate(singleRate);
			if (jrate >= 100){algorithmFlag++;}
		}

		Double scoreA = 0.0;
		String scoreB = null;
		// 算法选取
		if (algorithmFlag >= hasUndoListR.size()){
			//执行子算法a
			for (String sscore : hasUndoListR){
				scoreA += parseRate(sscore);
			}
			return String.valueOf(scoreA/hasUndoListR.size());
		} else {
			ArrayList<Double> scoreList = new ArrayList<>();
			//执行子算法b
			for (String sscore : hasUndoListR){
				Double tmpScore = parseRate(sscore);
				if (tmpScore >= 100.0) {
					scoreList.add(100.0);
				} else {
					scoreList.add(tmpScore);
				}
			}
			Double scoreBAll = 0.0;
			for (Double tmps : scoreList){
				scoreBAll += tmps;
			}
			scoreB = String.valueOf(Math.round(scoreBAll/scoreList.size()));

			return scoreB;
		}

	}


}