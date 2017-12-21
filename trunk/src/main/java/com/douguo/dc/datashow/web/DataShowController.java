package com.douguo.dc.datashow.web;

import com.douguo.dc.datashow.service.DataShowService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by lichang on 2017/8/22.
 */
@Controller
@RequestMapping("/datashow")
public class DataShowController {
    @Autowired
    private DataShowService dataShowService;

    @RequestMapping(value = "/queryList",method = RequestMethod.GET)
    public String queryList(){
        return  "/datashow/bar_show";
    }

    @RequestMapping(value = "/worldData",method = RequestMethod.GET)
    @ResponseBody
    public Map queryWorldData(){
        Map map=new HashMap();
        map.put("barData",dataShowService.queryAllData());
        map.put("caipuData",dataShowService.queryBehavior());
        map.put("provCityData",dataShowService.queryCityData());
        return map;
    }

    @RequestMapping(value = "/toRoastShow",method = RequestMethod.GET)
    public String toDataShowPage(){
        return "/datashow/douguo_roast_show";
    }


    @RequestMapping(value = "/toDouguoPersonShow",method = RequestMethod.GET)
    public String toDouguoPersonShowPage(){
        return "/datashow/douguo_person_show";
    }


    @RequestMapping(value = "/numData",method = RequestMethod.GET)
    @ResponseBody
    public Map queryNumData(){
        Map map=new HashMap();
        int num=dataShowService.getPersonNum();
        map.put("numData",num);
        return map;
    }


    @RequestMapping(value = "/queryAllData",method = RequestMethod.GET)
    @ResponseBody
    public Map queryAllData(){
      Map map=dataShowService.queryDgData();
        return map;
    }


}
