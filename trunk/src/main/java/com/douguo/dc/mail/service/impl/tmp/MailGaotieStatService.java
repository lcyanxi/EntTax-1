package com.douguo.dc.mail.service.impl.tmp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.dao.MailGaotieStatDao;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;

/**
 * 临时性-春节高铁项目
 * @author zyz
 *
 */
@Repository("mailGaotieStatService")
public class MailGaotieStatService {

	@Autowired
	private MailGaotieStatDao mailGaotieStatDao;

	@Autowired
	private SysMailSetService sysMailSetService;

	public void sendGaotieStatMail() {
		JavaSendMail javaSendMail = new JavaSendMail();
		try {
			SysMailSet sysMailSet = sysMailSetService.getSysMailSetByMailType(MailConstants.MAIL_TYPE_GAOTIE_SUM);

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
				subject = "豆果高铁投放数据日报_" + curDate;
				to = "zhangyaozhou@douguo.com";
			}

			// 1. 高铁基本数据统计
			String gaotieBaseStat = gaotieBaseStat();
			content.append("<h1>1、高铁数据统计</h1><br/>");
			content.append(gaotieBaseStat);
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


	private String gaotieBaseStat() {
		StringBuffer sbContent = new StringBuffer();
		String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String startDate = DateUtil.getSpecifiedDayBefore(today, 7);
		String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		//
		List<Map<String, Object>> rowlist = mailGaotieStatDao.queryList(startDate, endDate);
		// 构造表格
		sbContent.append("<table class=\"gridtable\">");
		sbContent
				.append("<tr><th>序号</th><th>日期</th><th>WAP页面PV</th><th>收集手机号</th><th>用券单数</th><th>累计发券量</th></tr>");
		int i = 0;
		for (Map<String, Object> map : rowlist) {
			i++;
			Date statdate = (Date) map.get("statdate");
			Integer pv = (Integer) map.get("pv");
			Integer mobiles = (Integer) map.get("mobiles");
			Integer use_conpon = (Integer) map.get("use_conpon");
			Integer total_conpon = (Integer) map.get("total_conpon");

			//
			sbContent.append("<tr>");
			sbContent.append("<td>" + i + "</td>");
			sbContent.append("<td>" + statdate + "</td>");
			sbContent.append("<td>" + pv + "</td>");
			sbContent.append("<td>" + mobiles + "</td>");
			sbContent.append("<td>" + use_conpon + "</td>");
			sbContent.append("<td>" + total_conpon + "</td>");
			sbContent.append("</tr>");
		}
		sbContent.append("</table>");
		sbContent.append("<br/>");

		return sbContent.toString();
	}
}