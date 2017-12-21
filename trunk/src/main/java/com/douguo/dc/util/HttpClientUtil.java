package com.douguo.dc.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * User: zhangyaozhou
 */
public class HttpClientUtil {
	static HttpClient httpClient = new HttpClient();
	static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	static int TIMEOUT = 60 * 1200;
	static int MAX_HTTP_CONNECTION = 50;
	static int count = 0;

	static {
		// HttpClient 连接池属性设置，HOST并发数默认为2, 客户端总并发数为50, TimeOut时间为5s.
		HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
		httpConnectionManagerParams.setMaxTotalConnections(MAX_HTTP_CONNECTION);
		httpConnectionManagerParams.setSoTimeout(TIMEOUT);
		httpConnectionManagerParams.setConnectionTimeout(TIMEOUT);
		connectionManager.setParams(httpConnectionManagerParams);
		
	}

	/**
	 * POST 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doPostRequest(String url, Map<String, String> params) throws Exception {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setQueryString(getParams(params));
		client.executeMethod(method);
		return method.getResponseBodyAsString();
	}

	/**
	 * GET 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doGetRequest(String url, Map<String, String> params) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.setQueryString(getParams(params));
		int statusCode = client.executeMethod(method);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + method.getStatusLine());
		}
		return method.getResponseBodyAsString();
	}

	/**
	 * 处理 stream GET 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doGetRequestAsStream(String url, Map<String, String> params, List<Header> listHeader)
			throws Exception {
		//
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.setQueryString(getParams(params));
		if (listHeader != null) {
			for (Header h : listHeader) {
				method.addRequestHeader(h);
			}
		}

		client.executeMethod(method);

		InputStream is = method.getResponseBodyAsStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		StringBuffer sbLine = new StringBuffer("");
		while ((line = in.readLine()) != null) {
			sbLine.append(line);
		}
		return sbLine.toString();
	}

	/**
	 * 处理 gzip GET 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doGZipGetRequest(String url, Map<String, String> params, List<Header> listHeader)
			throws Exception {
		//
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.setQueryString(getParams(params));
		if (listHeader != null) {
			for (Header h : listHeader) {
				method.addRequestHeader(h);
			}
		}

		client.executeMethod(method);

		return parseGZipRes(method);
	}

	/**
	 * 解析GZip压缩后的http响应报文
	 * 
	 * @param req
	 *            HTTP请求
	 * @return 解压结果
	 */
	private static String parseGZipRes(HttpMethod req) {
		StringBuffer strBuf = new StringBuffer();
		try {
			GZIPInputStream gzipis = new GZIPInputStream(req.getResponseBodyAsStream());
			InputStreamReader isr = new InputStreamReader(gzipis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				strBuf.append(line);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		return strBuf.toString();
	}

	/**
	 * 获取参数
	 * 
	 * @param params
	 * @return
	 */
	public static NameValuePair[] getParams(Map<String, String> params) {
		List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0) {
			Iterator<Entry<String, String>> it = params.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> en = it.next();
				parametersBody.add(new NameValuePair(en.getKey(), en.getValue()));
			}
		}
		return parametersBody.toArray(new NameValuePair[] {});
	}

	public static void main(String[] args) throws Exception {
		// 游戏分类:http://xml.mumayi.com/v16/category.php?type=6
		// 某个分类下游戏列表:http://xml.mumayi.com/v16/list.php?cid=56&listtype=hotgame&page=1
		// 游戏属性:hhttp://xml.mumayi.com/v16/content.php?id=72341
		String Url1 = "http://xml.mumayi.com/v16/category.php?";

		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "6");
		String s = HttpClientUtil.doGetRequest(Url1, map);
		System.out.println(s);

	}
}
