package com.douguo.dc.datashow.service;

import com.douguo.dc.datashow.dao.DataShowDao;
import com.douguo.dc.userbehavior.dao.UserBehaviorDao;
import com.douguo.dc.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lichang on 2017/8/23.
 */
@Repository("dataShowService")
public class DataShowService {
    @Autowired
    private DataShowDao dataShowDao;
    @Autowired
    private UserBehaviorDao userBehaviorDao;

    /**
     * 饼图、柱形图数据
     * @return
     * @throws Exception
     */
    public Map queryAllData(){

        Map maps=new HashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        //放年龄数据
        Map age=new TreeMap();


        //放忌口数据
        List taboosList=new ArrayList();
        List taboosNameList=new ArrayList();
        List taboosValueList=new ArrayList();

        //放慢性病数据
        List  chronList=new ArrayList();
        List chronNameList=new ArrayList();
        List chronValueList=new ArrayList();
        //从数据库取出数据
        List<Map<String,Object>> list=dataShowDao.queryDimention(startDate,endDate);

        if(list.isEmpty()){
             startDate = DateUtil.getSpecifiedDayBefore(today, 2);
             endDate = DateUtil.getSpecifiedDayBefore(today, 2);
            list=dataShowDao.queryDimention(startDate,endDate);
        }

        for (Map<String,Object> map:list){
            String type=(String)map.get("dimention_type");
            String name=(String) map.get("dimention_name");
            int value=(int)map.get("dimention_value");

            Map  tempMap=new LinkedHashMap();

            tempMap.put("name",name);
            tempMap.put("value",value);

            if (type.equals("age")){

                age.put(name,value);

            }if (type.equals("taboos")){
                taboosNameList.add(name);
                taboosValueList.add(value);
                taboosList.add(tempMap);
            }if (type.equals("chronicillness")){
                if (!name.equals("其他慢性病")){
                    chronList.add(tempMap);
                    chronNameList.add(name);
                    chronValueList.add(value);
                }
            }
        }


        //构造数据------收入
        Map income=new LinkedHashMap();
        income.put("3k以下",10);
        income.put("3k-5k",15);
        income.put("5k-10k",40);
        income.put("10k-20k",25);
        income.put("20k以上",10);

        //构造数据------职业
        Map profession=new HashMap();
        profession.put("全职主妇",17);
        profession.put("学生",10);
        profession.put("个体商人",8);
        profession.put("企业员工",29);
        profession.put("机关事业单位员工",14);
        profession.put("其他",22);

        //封装最终数据 name，value  数据样本：[{value:335, name:'直接访问'},{value:310, name:'邮件营销'},{value:234, name:'联盟广告'}]
        //list倒序输出

        maps.put("taboosData",taboosList);
       // maps.put("taboosName",taboosNameList);

        //封装最终数据name   数据样本：['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
        maps.put("ageName",converNameList(age));
        maps.put("ageData",converValueList(age));

        maps.put("incomeName",converNameList(income));
        maps.put("incomeData",converValueList(income));

        maps.put("professionData",converValueList(profession));
        maps.put("professionName",tempProfeList(profession));


         chronNameList =converChronList(chronNameList,11);
         chronValueList =converChronList(chronValueList,11);
        maps.put("chronName", chronNameList);
        maps.put("chronDatas", chronValueList );



        return maps;
    }


    /**
     * 省份、城市数据
     * @return
     */
    public Map queryCityData(){

        Map provinceMap=new LinkedHashMap();
        Map cityMap=new LinkedHashMap();

        provinceMap.put("广东",10.72);
        provinceMap.put("江苏",8.36);
        provinceMap.put("浙江",6.52);
        provinceMap.put("山东",6.41);
        provinceMap.put("河南",6.01);
        provinceMap.put("河北",5.12);
        provinceMap.put("北京",4.82);
        provinceMap.put("辽宁",4.28);
        provinceMap.put("四川",3.80);
        provinceMap.put("上海",3.68);
        provinceMap.put("江西",3.31);
        provinceMap.put("福建",3.14);
        provinceMap.put("内蒙古",2.97);
        provinceMap.put("湖北",2.88);
        provinceMap.put("陕西",2.77);
        provinceMap.put("黑龙江",2.69);
        provinceMap.put("山西",2.30);
        provinceMap.put("湖南",2.20);
        provinceMap.put("安徽",2.11);
        provinceMap.put("天津",2.02);

        cityMap.put("北京",5.43);
        cityMap.put("上海",3.69);
        cityMap.put("广州",3.43);
        cityMap.put("郑州",2.96);
        cityMap.put("济南",2.75);
        cityMap.put("成都",2.73);
        cityMap.put("杭州",2.72);
        cityMap.put("南京",2.69);
        cityMap.put("深圳",2.11);
        cityMap.put("西安",2.05);
        cityMap.put("天津",2.03);
        cityMap.put("呼和浩特",1.86);
        cityMap.put("沈阳",1.78);
        cityMap.put("武汉",1.73);
        cityMap.put("东莞",1.71);
        cityMap.put("重庆",1.68);
        cityMap.put("绍兴",1.68);
        cityMap.put("哈尔滨",1.65);
        cityMap.put("石家庄",1.58);
        cityMap.put("南昌",1.48);

        List provName=tempSizeList(converNameList(provinceMap),9);
        List provData=tempSizeList(converValueList(provinceMap),9);

        List cityName=tempSizeList(converNameList(cityMap),9);
        List cityData=tempCityDataList(converValueList(cityMap));


        Map map=new HashMap();
        map.put("provName",provName);
        map.put("provData",provData);

        map.put("cityName",cityName);
        map.put("cityData",cityData);

        return map;
    }


    /**
     * 热门词
     * @return
     */
    public Map  queryBehavior(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);


        List<Map<String,Object>> searchList=  userBehaviorDao.querySumListByQtype(startDate,endDate,"search_caipu");
        List<Map<String,Object>> veiewList= userBehaviorDao.querySumListByQtype(startDate,endDate,"view_caipu_detail");

        if (searchList.isEmpty()){
             startDate = DateUtil.getSpecifiedDayBefore(today, 2);
             endDate = DateUtil.getSpecifiedDayBefore(today, 2);
            searchList=  userBehaviorDao.querySumListByQtype(startDate,endDate,"search_caipu");
        }
        if (veiewList.isEmpty()){
            startDate = DateUtil.getSpecifiedDayBefore(today, 2);
            endDate = DateUtil.getSpecifiedDayBefore(today, 2);
            veiewList= userBehaviorDao.querySumListByQtype(startDate,endDate,"view_caipu_detail");
        }

       List  searchData =new ArrayList();

       Map map=new HashMap();


       //遍历热门搜索词数据
       for (Map<String,Object> tempMap:searchList){
           Map searchMap=new LinkedHashMap();
           String keyword = (String)tempMap.get("keyword");
           BigDecimal counts = (BigDecimal) tempMap.get("counts");
           searchMap.put("name",keyword);
           searchMap.put("value",counts);
           searchData.add(searchMap);
       }

       map.put("hotSearch",tempSizeList(searchData,30));
       map.put("viewCaipu",tempSizeList(converBehavior(veiewList),4));
       return map;
    }

    /**
     * 中国地图数据
     * @return
     */
    public Map  queryDgData(){

        Map map=new HashMap();
        Map<String,Object> dgData=null;
        List<Map<String,Object>> list=dataShowDao.queryDgData();
        if (!list.isEmpty()){
          dgData=list.get(0);
          //判断是否有异常数据Keep-Alive
          String code=(String) dgData.get("code");
            if (code.contains("Keep-Alive")){
                dgData=list.get(1);
            }
            map.put("status","OK");
        }else {
            map.put("status","NO");
        }
        map.put("data",dgData);
        return map;

    }

    /**
     * 世界地图数据
     * @return
     */
    public List queryWorldData(){
        Map<String,Object> dgData=null;
        List<Map<String,Object>> list=dataShowDao.queryDgData();
        if (!list.isEmpty()){
           dgData =list.get(0);
        }
       List<Map<String,Object>>  tempList=(List) dgData.get("code");
        List  currntList=new ArrayList();
        for (Map map:tempList){
            currntList.add(map.get("geoCoord"));
        }
        return  currntList;

    }

    /**
     * 每秒取一次在线人数
     * @return
     */
    public int getPersonNum(){
        String nowDateTime= DateUtil.getTimeByMinute(0);
        List<Map<String,Object>> list= dataShowDao.queryPersonNum(nowDateTime);
        System.out.println(list);
        if (list.isEmpty()){
            list= dataShowDao.queryPersonNum(DateUtil.getTimeByMinute(1));
        }
        Map<String,Object> numMap=list.get(0);
        int numPerson=(int)numMap.get("num");
        return numPerson;
    }



    /**
     * 拿到数据的name
     * @param map
     * @return
     */
    private List converNameList(Map map){
        List list=new ArrayList();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            list.add(key);
        }
        return  list;
    }

    /**
     * 拿到数据的value
     * @param map
     * @return
     */
    private List converValueList(Map map){
        List list=new ArrayList();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object value = entry.getValue();
            list.add(value);
        }
        return  list;
    }

    /**
     * 热门词数据转换
     * @param list
     * @return
     */
    private List converBehavior(List<Map<String,Object>> list){
        List list1=new ArrayList();
        for (Map<String,Object> map:list){
            list1.add(map.get("keyword"));
        }
        return list1;
    }

    /**
     * 获取指定长度的list集合
     * @param list
     * @return
     */
    private List tempSizeList(List list,int index){
        List templist=new ArrayList();
        if (list.size()>index){
            for (int i=index; i>=0;i--){
                templist.add(list.get(i));
            }
            return templist;
        }else {
            return  list;
        }
    }


    /**
     * cityData将值变为负数
     * @param list
     * @return
     */
    private List tempCityDataList(List list){
        List templist=new ArrayList();
        for (int i=9; i>=0;i--){
            double temp=(double)list.get(i);
            templist.add(-1*temp);
        }
        return templist;
    }

    /**
     * 职业雷达图数据格式转换
     * @param map
     * @return
     */
    private List tempProfeList(Map map){
        List list=new ArrayList();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map tempMap=new LinkedHashMap();
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            tempMap.put("text",key);
            tempMap.put("max",30);
            list.add(tempMap);
        }
        return  list;
    }

    /**
     * 慢性病取前11个
     * @param list
     * @return
     */
    private List converChronList(List list,int index){
        List templist=new ArrayList();
        int temp=1;
        if (list.size()>index){
            for (int i=list.size()-1;temp<=index;i--){
                templist.add(list.get(i));
                temp=temp+1;
            }
            Collections.reverse(templist);//倒序取后指定个数数据
            return templist;
        }else {
            return  list;
        }
    }

}
