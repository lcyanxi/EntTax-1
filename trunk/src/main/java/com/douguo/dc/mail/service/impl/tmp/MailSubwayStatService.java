package com.douguo.dc.mail.service.impl.tmp;

import com.douguo.dc.applog.service.AppUserNewService;
import com.douguo.dc.channel.service.ChannelStatService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.dao.MailGaotieStatDao;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 临时性-地铁投放项目
 * @author zyz
 *
 */
@Repository("mailSubwayStatService")
public class MailSubwayStatService {

	@Autowired
	private AppUserNewService appUserNewService;

	@Autowired
	private ChannelStatService channelStatService;

//	@Autowired
//	private MailGaotieStatDao mailGaotieStatDao;

	@Autowired
	private SysMailSetService sysMailSetService;

	public void sendMail(SysMailSet sysMailSet ) {
		JavaSendMail javaSendMail = new JavaSendMail();
		try {
//			SysMailSet sysMailSet = sysMailSetService.getSysMailSetByMailType(MailConstants.MAIL_TYPE_GAOTIE_SUM);

			String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
			String subject = "";
			String to = "";
			StringBuffer content = new StringBuffer("");
			// 增加样式
			content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");

			if (sysMailSet != null) {
				subject = sysMailSet.getSubject() + "_" + curDate;
				to = sysMailSet.getMailTo();
			} else {
				subject = "豆果地铁数据日报_" + curDate;
				to = "zhangyaozhou@douguo.com";
			}

			// 1. 基本数据统计
			String strBaseStat = subwayBaseStat();
			content.append("<h1>1、地铁数据统计</h1><br/>");
			content.append(strBaseStat);
			content.append("<br/><br/>");

			// 2. 渠道数据统计
			String strSubwayChannelStat = subwayChannelStat();
			content.append("<h1>2、地铁Android渠道包数据统计</h1><br/>");
			content.append(strSubwayChannelStat);
			content.append("<br/><br/>");

			// 3. WAP商品数据统计
			String strWapProductStat = wapProductStat();
			content.append("<h1>3、WAP站商品数据统计</h1><br/>");
			content.append(strWapProductStat);
			content.append("<br/><br/>");

			content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

			javaSendMail.sendHtmlEmail(subject, to, content.toString());
//			String[] imgPaths = new String[] { MailConstants.MAIL_CHART_PATH + "/img-group-hour.jpg" };
//			javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private String subwayChannelStat() {
		StringBuffer sbContent = new StringBuffer();
		String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String startDate = DateUtil.getSpecifiedDayBefore(today, 7);
		String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		//
		List<Map<String, Object>> rowlistChannel = appUserNewService.queryListByChannel(startDate, endDate,"d0398");
		Map<Date,Integer> mapChannel = new HashMap<Date,Integer>();
		for (Map<String ,Object> map : rowlistChannel){
			Date statdate = (Date)map.get("statdate");
			Integer uid = (Integer)map.get("uid");
			mapChannel.put(statdate,uid);
		}

		List<Map<String, Object>> rowlist = channelStatService.queryChannelStatList(startDate, endDate,"");
		// 构造表格
		sbContent.append("<table class=\"gridtable\">");
		sbContent
				.append("<tr><th>序号</th><th>日期</th><th>渠道</th><th>新增激活</th><th>商品浏览</th><th>成交</th></tr>");
		int i = 0;
		for (Map<String, Object> map : rowlist) {

			Date statdate = (Date) map.get("statdate");
			String channelCode = (String) map.get("channel");
			if (channelCode.equals("d0398")){
				i++;
				Integer uids = mapChannel.get(statdate);
				Integer view_goods = (Integer) map.get("view_goods");
				Integer pays = (Integer) map.get("pays");
				//
				sbContent.append("<tr>");
				sbContent.append("<td>" + i + "</td>");
				sbContent.append("<td>" + statdate + "</td>");
				sbContent.append("<td>" + channelCode + "</td>");
				sbContent.append("<td>" + uids + "</td>");
				sbContent.append("<td>" + view_goods + "</td>");
				sbContent.append("<td>" + pays + "</td>");
				sbContent.append("</tr>");
			}
		}
		sbContent.append("</table>");
		sbContent.append("<br/>");

		return sbContent.toString();
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	private static String subwayBaseStat() {
		StringBuffer sbContent = new StringBuffer();
		String fileName = "/opt/DATA/goldmine/src/kpi/daily_count/mall/mall_wap/data/dataSubwayStat.log";

		File file = new File(fileName);
		BufferedReader reader = null;
		// 构造表格
		sbContent.append("<table class=\"gridtable\">");
		sbContent
				.append("<tr><th>日期</th><th>二维码总pv</th><th>m站商品总pv</th><th>9.9专题pv</th><th>19.9专题pv</th><th>29.9专题pv</th></tr>");
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// System.out.println("line " + line + ": " + tempString);
				line++;
				String[] arryLine = tempString.split(",");
				//
				sbContent.append("<tr>");
				sbContent.append("<td>" + arryLine[0] + "</td>");
				sbContent.append("<td>" + arryLine[1] + "</td>");
				sbContent.append("<td>" + arryLine[2] + "</td>");
				sbContent.append("<td>" + arryLine[3] + "</td>");
				sbContent.append("<td>" + arryLine[4] + "</td>");
				sbContent.append("<td>" + arryLine[5] + "</td>");
				sbContent.append("</tr>");
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		sbContent.append("</table>");
		sbContent.append("<br/>");
		return sbContent.toString();
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	private static String wapProductStat() {
		StringBuffer sbContent = new StringBuffer();
		String fileName = "/opt/DATA/goldmine/src/kpi/daily_count/mall/mall_wap/data/dataProductTuan.log";
		File file = new File(fileName);
		BufferedReader reader = null;
		// 构造表格
		sbContent.append("<table class=\"gridtable\">");
		sbContent
				.append("<tr><th>商品id</th><th>商品名称</th><th>pv</th><th>成交单</th></tr>");
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// System.out.println("line " + line + ": " + tempString);
				line++;
				String[] arryLine = tempString.split(",");
				//
				sbContent.append("<tr>");
				sbContent.append("<td>" + arryLine[0] + "</td>");
				sbContent.append("<td>" + arryLine[1] + "</td>");
				sbContent.append("<td>" + arryLine[2] + "</td>");
				sbContent.append("<td>" + arryLine[5] + "</td>");
				sbContent.append("</tr>");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		sbContent.append("</table>");
		sbContent.append("<br/>");
		return sbContent.toString();
	}

}