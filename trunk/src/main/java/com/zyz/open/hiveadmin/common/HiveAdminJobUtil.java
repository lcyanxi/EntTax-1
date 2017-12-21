package com.zyz.open.hiveadmin.common;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.douguo.dc.util.DateUtil;
import com.zyz.open.hiveadmin.model.HiveAdminJob;

public class HiveAdminJobUtil {
	
	/**
	 * 判断当查询的表为serverLog表时, 开始'结束统计时间跨度
	 * @author JianfeiZhang
	 * @param hiveAdminJob
	 * @param statBeginTime
	 * @param statEndTime
	 * @param hql
	 * @param request
	 * @return
	 */
	public static boolean tableDaysDiffCheck(HiveAdminJob hiveAdminJob, String statBeginTime, String statEndTime, String hql, HttpServletRequest request){
		
		// flag : status是否已经改变
        boolean statusChangeFalg = false ;
		
		// 当 dh_server_log > 7, dh_pv > 15, dh_wap_pv > 15 提示回显用户
		String[] regString = {"dh_server_log", "dh_pv", "dh_wap_pv"} ;
		
		for(String tempReg : regString){
			if(tempReg.equals("dh_server_log")){
				//计算时间差
				int daysDiff = 0;
				try {
					daysDiff = DateUtil.getBetweenDay(statBeginTime, statEndTime);
				} catch (ParseException e) {
					e.printStackTrace();
					daysDiff = 7 ; // 计算时间差失败，默认7天
				}
				//匹配表名
				Pattern p = Pattern.compile(tempReg) ;
				Matcher m = p.matcher(hql) ;
				boolean sqlRegResult = m.find() ;
				//判断
				if(sqlRegResult==true && daysDiff>7){
					request.setAttribute("dslMsg", "查询跨度大于7天，点击'申请执行'联系DataCenter");
					//设置状态:未审核
					hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_DRAFT);
					statusChangeFalg = true ;
				}     		
			}
			if(tempReg.equals("dh_pv")){
				//计算时间差
				int daysDiff = 0 ;
				try {
					daysDiff = DateUtil.getBetweenDay(statBeginTime, statEndTime);
				} catch (ParseException e) {
					e.printStackTrace();
					daysDiff = 15 ;
				}
				//匹配表名
				Pattern p = Pattern.compile(tempReg) ;
				Matcher m = p.matcher(hql) ;
				boolean sqlRegResult = m.find() ;
				if(sqlRegResult==true && daysDiff>15){
					request.setAttribute("dpMsg", "查询跨度大于15天，点击'申请执行'联系DataCenter");
					//设置状态:未审核
					hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_DRAFT);
					statusChangeFalg = true ;
				}     		
			}
			if(tempReg.equals("dh_wap_pv")){
				//计算时间差
				int daysDiff = 0 ;
				try {
					daysDiff = DateUtil.getBetweenDay(statBeginTime, statEndTime);
				} catch (ParseException e) {
					e.printStackTrace();
					daysDiff = 15 ;
				}
				//匹配表名
				Pattern p = Pattern.compile(tempReg) ;
				Matcher m = p.matcher(hql) ;
				boolean sqlRegResult = m.find() ;
				if(sqlRegResult==true && daysDiff>15){
					request.setAttribute("dwpMsg", "查询跨度大于15天，点击'申请执行'联系DataCenter");
					//设置状态:未审核
					hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_DRAFT);
					statusChangeFalg = true ;
				}     		
			}
		}
		
		return statusChangeFalg ;
		
	}

	
}
