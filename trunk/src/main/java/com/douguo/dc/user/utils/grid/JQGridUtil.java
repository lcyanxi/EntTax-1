package com.douguo.dc.user.utils.grid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;


public class JQGridUtil {

	public static void main(String args[]) {
		String[][] arryFun = new String[6][7];
		arryFun[0] = new String[] { "1", "0", "1", "1", "1", "1" , "1" };
		arryFun[1] = new String[] { "3", "0", "3", "1", "2", "1" , "1" };
		arryFun[2] = new String[] { "2", "1", "2", "2", "1", "1" , "1" };
		arryFun[3] = new String[] { "5", "1", "5", "2", "2", "1" , "1" };
		arryFun[4] = new String[] { "6", "2", "6", "3", "1", "1" , "1" };
		arryFun[5] = new String[] { "4", "3", "4", "2", "1", "1" , "1" };
		JQGridUtil t = new JQGridUtil();
		
		int page = 1;
		int total = 1;
		int records = arryFun.length;
		
		//行数据
		List<Map> rows = new ArrayList<Map>();
		
		for(String[] axx : arryFun){
			Map map = new HashMap();
			map.put("id", "1");
			map.put("cell", axx);
			
			rows.add(map);
		}
		//rows
		GridPager<Map> gridPager = new GridPager<Map>( page, total, records, rows);
		t.toJson(gridPager, null);
	}
	
	/**
	 * 生成JQGride 需要的json数据
	 * 
	 * @param gridPager
	 * @param response
	 */
	public void toJson(GridPager gridPager, HttpServletResponse response) {
		JsonGenerator jsonGenerator = null;
		ObjectMapper objectMapper = null;
		objectMapper = new ObjectMapper();
		
		try {
			if(response != null){
//				System.out.println(gridPager.getRows().get(0));
				response.setContentType("text/html;charset=UTF-8");  
				objectMapper.writeValue(response.getWriter(),gridPager);
			}else{
				jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(
						System.out, JsonEncoding.UTF8);
				objectMapper.writeValue(jsonGenerator, gridPager);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}