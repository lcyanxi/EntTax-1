package com.douguo.dc.userbehavior.common;

public class UserBehaviorConstants {

    /**
     * 统计类型
     */
    public enum StatType {
        //统计所有
        ALL("ALL"),

        //按渠道统计
        CHANNEL("CHANNEL");

        public String type;

        StatType(String statType) {
            this.type = statType;
        }
    }

    /**
     * 用户类型
     */
    public enum UserType {
        //所有用户
        ALL("ALL"),

        //新注册用户
        NEW_REG_USER("NEW_REG_USER");
        public String type;

        UserType(String userType) {
            this.type = userType;
        }
    }

    /**
     * 时间类型
     */
    public enum TimeType {
        //天
        DAY("DAY1"),

        //7天内
        DAY7("DAY7"),

        //自然周
        WEEK("WEEK"),

        //自然月
        MONTH("MONTH"),

        //季度
        QUARTER("QUARTER"),

        //年
        YEAR("YEAR");
        public String type;

        TimeType(String timeType) {
            this.type = timeType;
        }
    }
}