package com.douguo.dc.util;

import java.text.DecimalFormat;

/**
 *  数据计算类工具
 *  @author zjf
 *  @date 2017-12-08
 */
public class MathUtil {

    /**
     * INT类型相除，保留2位小数
     * @param molecule
     * @param denominator
     * @return
     */
    public static String parseIntDivisionStr (int molecule, int denominator) {
        if (molecule==0 || denominator==0){
            return "0.00";
        } else {
            DecimalFormat df=new DecimalFormat("0.00");
            String rate = df.format((float)molecule*100/denominator);
            return rate;
        }
    }

}
