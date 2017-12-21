package com.douguo.dc.article.lifenumber.util;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EchartsJsonParseUtil {

    public static JSONObject buildJson(String name, String type, List data) throws JSONException {
        JSONObject jsonObj = new JSONObject();

        if (StringUtils.isNotBlank(name)) {
            jsonObj.put("name", name);
        }
        jsonObj.put("type", type);
        jsonObj.put("data", data);
        return jsonObj;
    }

}
