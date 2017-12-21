package com.douguo.dc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BigDecimalUtil {

    /**
     * 计算比率
     *
     * @param sub
     * @param total
     * @param scale
     * @return
     */
    public static BigDecimal getPercentage(BigDecimal sub, BigDecimal total, int scale) {

        if (sub == null || total == null || sub.intValue() == 0 || total.intValue() == 0) {
            return new BigDecimal("0");
        }

        if (scale < 0) {
            scale = 2;
        }
        BigDecimal divisor = sub;
        BigDecimal dividend = total;
        double d = divisor.divide(dividend, scale + 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

        BigDecimal result = new BigDecimal(Double.toString(d));
        BigDecimal one = new BigDecimal("1");
        BigDecimal ret = result.divide(one, scale, BigDecimal.ROUND_HALF_UP);
        return ret;
    }

    public static String format(BigDecimal value, int scale) {
        if(value == null){
            return "0";
        }
        String strFormat = validate(scale);
        DecimalFormat mformat = new DecimalFormat(strFormat);
        return mformat.format(value.doubleValue());
    }

    /**
     * 公用部分
     *
     * @param scale
     * @return
     */
    public static String validate(int scale) {
        String m_strFormat = "###,###,###,###,###";

        if (scale < 0) {

            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");

        }
        if (scale > 0) {
            m_strFormat += ".";
            for (int i = 0; i < scale; i++) {
                m_strFormat += "0";
            }
        }

        return m_strFormat;
    }
}