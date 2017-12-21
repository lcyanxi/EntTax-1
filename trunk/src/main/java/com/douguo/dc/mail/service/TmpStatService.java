package com.douguo.dc.mail.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;

@Repository("tmpStatService")
public class TmpStatService {

	@Autowired
	private SysMailSetService sysMailSetService;

	public void sendGroupSumMail() {
		JavaSendMail javaSendMail = new JavaSendMail();
		try {
			SysMailSet sysMailSet = sysMailSetService.getSysMailSetByMailType(MailConstants.MAIL_TYPE_GROUP_BASE_SUM);

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
				subject = "豆果特殊日报_" + curDate;
				to = "zhangyaozhou@douguo.com";
			}

			// 1. 圈子基本数据统计
			String groupBaseStat = "";
			content.append("<h1>1、IOS重复提交订单数据统计</h1><br/>");
			content.append(groupBaseStat);
			content.append("<br/><br/>");

			// 5. 订单下单时间分布
			String groupHour = groupHourStat();
			content.append("<h1>2、APP圈子&优食汇统计</h1><br/>");
			content.append(groupHour);
			content.append("<br/>");

			content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

			// javaSendMail.sendHtmlEmail(subject, to, content.toString());
			// String[] imgPaths = new String[] { MailConstants.MAIL_CHART_PATH
			// + "/img-group-hour.jpg" };
			String[] imgPaths = new String[] { MailConstants.MAIL_CHART_PATH + "/data_mall_ios_repeat_user.txt" };

			javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getName(String id) {

		if (id.equals("g")) {
			return "总计";
		} else if (id.equals("1")) {
			return "情感生活";
		} else if (id.equals("2")) {
			return "美味厨房";
		} else if (id.equals("3")) {
			return "美容瘦身";
		} else if (id.equals("4")) {
			return "奶爸奶妈";
		} else if (id.equals("5")) {
			return "意见反馈";
		} else if (id.equals("6")) {
			return "烘焙联盟";
		} else {
			return "未知";
		}
	}

	private String groupHourStat() {
		return "";
	}
}