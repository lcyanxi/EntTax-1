package com.douguo.dc.mail.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.mall.common.MallConstants;
import com.douguo.dc.mall.service.AppTuanService;
import com.douguo.dc.mall.service.MallRepeatBuyService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.GeoUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dg.mall.service.DgMallOrderService;

@Repository("mallOrderGeoService")
public class MallOrderGeoService {

    @Autowired
    private DgMallOrderService dgMallOrderService;

    @Autowired
    private MallRepeatBuyService mallRepeatBuyService;

    @Autowired
    private SysMailSetService sysMailSetService;

    public void sendMail(SysMailSet sysMailSet) {
        try {
            String curDate = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
            List<Map<String, Object>> list = dgMallOrderService.getOrderList(curDate);
            for (Map<String, Object> map : list) {
                String address = (String) map.get("address");
                String s = GeoUtil.getGeo(address);
                JSONObject jsonObj = new JSONObject(s);
                JSONObject obj = jsonObj.getJSONObject("result").getJSONObject("location");
                String lng = obj.getString("lng");
                String lat = obj.getString("lat");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}