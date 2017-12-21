package com.douguo.dc.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: zhangyaozhou
 */
public class GeoUtil {
    // 豆果 百度api的密钥
    private static String DOUGUO_BAIDU_KEY = "194eb135b0f4785b4cd080cca3212939";

    public static void main(String[] args) throws Exception {
        String address = URLEncoder.encode("安宁佳园", "UTF-8");
        address = "北京市 海淀区 清河 安宁佳园";
        address = "湖北省 武汉市 武昌区 石牌岭80号路西干休所";
        address = "矩阵小区18号楼301";
//        String s = getGeo(address);
//        System.out.println(s);
//        buildJsonData();
//        exportJson();
        exportHotJson();
    }

    public static String getGeo(String address) throws Exception {
        return getGeo(address, "");
    }

    public static String getGeo(String address, String city) throws Exception {
        String Url1 = "http://api.map.baidu.com/geocoder/v2/";

        Map<String, String> map = new HashMap<String, String>();
        map.put("ak", DOUGUO_BAIDU_KEY);// ak 密钥
        map.put("address", address);
        map.put("output", "json");
        if (StringUtils.isNotBlank(city)) {
            map.put("city", city);
        }

        String s = HttpClientUtil.doGetRequest(Url1, map);

        return s;
    }

    /**
     * 导出热力图json
     *
     * @throws JSONException
     */
    public static void exportHotJson() throws JSONException {
        try {
            String filePath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_beijing_geo_hot.log";
            String fileOutPath = "/Users/zyz/Documents/mall_order_geo/data-beijing-hot.js";

//            String filePath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_qingcai_geo_hot.log";
//            String fileOutPath = "/Users/zyz/Documents/mall_order_geo/data-qingcai-hot.js";


            List<JSONObject> list = new ArrayList<JSONObject>();
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            FileUtil fUtil = new FileUtil(fileOutPath);
            JSONArray array = new JSONArray();
            while (line != null) {
                line = br.readLine();
                if (line == null) {
                    continue;
                }
                String [] arryL = line.trim().split(" |,");
                if(arryL.length >2){
                    JSONObject jsonObject = new JSONObject();
//                    List<Float> listTmp = new ArrayList<Float>();
//                    listTmp.add(new Float(arryL[1]));
//                    listTmp.add(new Float(arryL[2]));
//                    listTmp.add(new Float(arryL[0]));
                    jsonObject.put("lng",arryL[1]);
                    jsonObject.put("lat",arryL[2]);
                    jsonObject.put("count",arryL[0]);
                    array.put(jsonObject);
                }
            }

            br.close();
            fis.close();
            //

            array.put(list);
            //

            fUtil.writeFile("var points=" + array.toString());
            fUtil.finish();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportJson() throws JSONException {
        try {
//            String filePath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_beijing_geo.log";
//            String fileOutPath = "/Users/zyz/Documents/mall_order_geo/data-beijing.js";

            String filePath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_qingcai_geo.log";
            String fileOutPath = "/Users/zyz/Documents/mall_order_geo/data-qingcai.js";

            JSONObject jsonObject = new JSONObject();
            List<List<Float>> list = new ArrayList<List<Float>>();
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            FileUtil fUtil = new FileUtil(fileOutPath);

            while (line != null) {
                line = br.readLine();
                if (line == null) {
                    continue;
                }
                String [] arryL = line.split(",");
                if(arryL.length >1){
                    List<Float> listTmp = new ArrayList<Float>();
                    listTmp.add(new Float(arryL[0]));
                    listTmp.add(new Float(arryL[1]));
                    listTmp.add(new Float(1));
                    list.add(listTmp);
                }

            }

            br.close();
            fis.close();
            //
            JSONArray array = new JSONArray();
            array.put(list);
            //
            jsonObject.put("data", list);
            jsonObject.put("total", list.size());
            jsonObject.put("rt_loc_cnt", "47764510");
            jsonObject.put("errorno", "0");
            jsonObject.put("userTime", "2015-09-27 00:00:00");
            jsonObject.put("NearestTime", "2015-09-27 00:00:00");
            //System.out.println("var data=" + jsonObject.toString());
            fUtil.writeFile("var data=" + jsonObject.toString());
            fUtil.finish();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void buildJsonData() {
        try {
//            String filePath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_beijing.log";
//            String fileOutPath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_new.log";
            String filePath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_qingcai.log";
            String fileOutPath = "/Users/zyz/Documents/mall_order_geo/dataMallOrderGeo_qingcai_new.log";
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            FileUtil fUtil = new FileUtil(fileOutPath);


            while (line != null) {
                try {
                    line = br.readLine();
                    if (line == null) {
                        continue;
                    }
//                    System.out.println(line);
                    String strReturnValue = getGeo(line);

//                    System.out.println(strReturnValue);
                    JSONObject jsonObject = new JSONObject(strReturnValue);
//                JSONObject..getJSONObject(strReturnValue);
                    //
                    JSONObject jsonResult = jsonObject.optJSONObject("result");
                    if (jsonResult.has("location")) {
                        JSONObject jsonLocation = jsonResult.optJSONObject("location");
                        String lng = jsonLocation.optString("lng");
                        String lat = jsonLocation.optString("lat");
                        fUtil.writeFile(line + "~" + lng + "," + lat);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            fUtil.finish();
            br.close();
            fis.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}