package com.zyz.open.hiveadmin.common;

import java.util.ArrayList;
import java.util.List;

public class HiveAdminJobConstants {

    // JOB状态
    // 0:待审核;1:审核未通过；2:审核通过;3:执行中;4:执行失败；5：执行成功;
    public class JobStatus {
        public static final int CHECK_DRAFT = 0;
        public static final int CHECK_NO = 1;
        public static final int CHECK_OK = 2;
        public static final int EXEC_RUNNING = 3;
        public static final int EXEC_FAILURE = 4;
        public static final int EXEC_SUCCESS = 5;
    }

    public static final int PAGE_LIMIT_50 = 50;
    public static final int PAGE_LIMIT_500 = 500;

    //

    /**
     * 需排除qtype
     *
     * @param qtype
     * @return
     */
    public static boolean excludeQtype(String qtype) {
        List<String> qtypeList = new ArrayList<String>();
        qtypeList.add("user_unreadmsg");

        return qtypeList.contains(qtype);
    }

    public static String convertJobStatus(int status) {
        if (status == JobStatus.CHECK_DRAFT) {
            return "待审核";
        } else if (status == JobStatus.CHECK_NO) {
            return "审核未通过";
        } else if (status == JobStatus.CHECK_OK) {
            return "审核通过";
        } else if (status == JobStatus.EXEC_RUNNING) {
            return "运行中";
        } else if (status == JobStatus.EXEC_FAILURE) {
            return "失败";
        } else if (status == JobStatus.EXEC_SUCCESS) {
            return "成功";
        } else {
            return "未知";
        }
    }

    //生成文件路径前缀
    public static String JOB_WORK_DATA_DIR = "/usr/local/goldmine/tomcat/default/webapps/ROOT/hiveadmin/data/";

    //最大支持导出excel的文件大小  单位：字节
    public static long MAX_TXT_LENGTH = 30 * 1024 * 1024;

    //测试 or 审核邮件列表
    public static String TEST_CHECK_MAIL_LIST = "zhangyaozhou@douguo.com,zhangjianfei@douguo.com,lichang@douguo.com";

}