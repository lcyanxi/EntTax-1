package com.douguo.dc.user.utils.export.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelDemo {
	private static Map<String, String> mapCityData = new HashMap<String, String>();//城市对照
	private static List<String> listColDate = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		//		String fileName = "C:/Users/yaozhou.zhang.QUNARSERVERS/Downloads/20120725_hotel/hotel_new.txt";//源文件名
		//		String excelFileName = "C:/Users/yaozhou.zhang.QUNARSERVERS/Downloads/20120725_hotel/hotel.xls";//输出文件名
		//		String strCityFileName = "C:/Users/yaozhou.zhang.QUNARSERVERS/Downloads/20120725_hotel/citys.txt";//中英文对照文件名
		//		String strMonths = "7,8,9";

		if (args.length < 4) {
			throw new Exception("参数必须大于4");
		}
		String fileName = args[0];//源文件名
		String excelFileName = args[1];//输出文件名
		String strCityFileName = args[2];//中英文对照文件名
		String strMonths = args[3];//月份串：7,8,9

		//初始化城市对照表
		initCityData(strCityFileName);
		//初始化日期列
		initColData(strMonths);

		//组织数据
		String[][] arryDataNew = getData(fileName);

		//生成excel
		String sheetName = "报表";//sheet名
		List<String> listHeader = new ArrayList<String>();
		listHeader.addAll(listColDate);
		listHeader.add(0, "cityName");
		listHeader.add(0, "cityCNName");

		String[] arryHeader = listHeader.toArray(new String[listHeader.size()]);
		Excel excel = new Excel(excelFileName);
		ExcelSheet eSheet = excel.getSheet(sheetName);
		eSheet.setHeader(arryHeader);
		eSheet.addRecord(arryDataNew);
		excel.save(excelFileName);
	}

	/**
	 * 初始化列数据
	 * 
	 * @param months
	 */
	private static void initColData(String months) {
		String[] arryMonth = months.split(",");
		for (String month : arryMonth) {
			if (month.length() < 2) {
				month = "0" + month;
			}

			for (int j = 1; j <= 31; j++) {
				String tDate = String.valueOf(j);
				if (tDate.length() < 2) {
					tDate = "0" + j;
				}

				if ("02".equals(month)) {
					if (j > 28) {
						continue;
					}
				} else if ("02".equals(month) || "04".equals(month) || "06".equals(month) || "09".equals(month)
						|| "11".equals(month)) {
					if (j > 30) {
						continue;
					}
				}
				listColDate.add("2011-" + month + "-" + tDate);
			}
		}
	}

	private static void initColData1() {
		for (int i = 7; i <= 9; i++) {
			String tmp = "";
			String tmpMonth = String.valueOf(i);
			if (tmpMonth.length() < 2) {
				tmpMonth = "0" + i;
			}
			for (int j = 1; j <= 31; j++) {
				String tp = String.valueOf(j);
				if (tp.length() < 2) {
					tp = "0" + j;
				}

				if (i == 7) {
					if (j < 26) {
						continue;
					}

				} else if (i == 9) {
					if (j > 30) {
						continue;
					}
				}
				tmp = "2011-" + tmpMonth + "-" + tp;
				listColDate.add(tmp);
			}
		}
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String[][] getData(String fileName) {

		File file = new File(fileName);
		BufferedReader reader = null;
		Map<String, List> map = new HashMap<String, List>();
		String[][] arryReturn = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号

				line++;
				String[] arryLine = tempString.split(" |	");
				int xx = arryLine.length;
				if (xx == 3) {
					String tmpCityName = arryLine[0];
					String tmpDate = arryLine[1];
					String tmpCount = arryLine[2];
					if (!map.containsKey(tmpCityName)) {
						Map<String, String> mapCity = new HashMap<String, String>();
						mapCity.put(tmpDate, tmpCount);
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.add(mapCity);

						map.put(arryLine[0], list);
					} else {
						Map<String, String> mapCity = new HashMap<String, String>();
						mapCity.put(tmpDate, tmpCount);
						List<Map<String, String>> list = map.get(tmpCityName);
						list.add(mapCity);
					}
				}
			}
			reader.close();
			arryReturn = new String[map.size()][];
			int i = 0;
			for (Entry<String, List> entry : map.entrySet()) {

				String strCityName = entry.getKey();
				strCityName = getCNCityName(strCityName);
				List<Map<String, String>> listData = entry.getValue();

				String[] arryRow = new String[listColDate.size() + 2];
				arryRow[0] = entry.getKey();
				arryRow[1] = strCityName;

				for (int j = 0; j < listColDate.size(); j++) {
					String strDate = listColDate.get(j);
					int nCount = 0;

					for (Map<String, String> mm : listData) {
						if (mm.containsKey(strDate)) {
							nCount = Integer.parseInt(mm.get(strDate));
						}
					}

					arryRow[j + 2] = String.valueOf(nCount);
				}
				arryReturn[i] = arryRow;
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return arryReturn;
	}

	/**
	 * 中英文城市转换
	 * 
	 * @param enName
	 * @return
	 */
	private static String getCNCityName(String enName) {
		return mapCityData.get(enName);
	}

	/**
	 * init 城市 中英文对照 Map
	 * 
	 * @param fileName
	 * @return
	 */
	private static void initCityData(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] arryLine = tempString.split(" |	");
				if (arryLine.length >= 2) {
					mapCityData.put(arryLine[0].trim(), arryLine[1].trim());
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
