package com.douguo.dc.datamoniter.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class DgEsUtils {

	private static Log logger = LogFactory.getLog(DgEsUtils.class);

	public static Client client;

	/**
	 * startup Transport Client 启动es
	 * 
	 * @throws UnknownHostException
	 */
	public static void startupClient(String serverIp, String clusterName, Integer port) throws UnknownHostException {
		/**
		 * 可以设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
		 * 这样做的好 处是一般你不用手动设置集群里所有集群的ip到连接客户端，它会自动帮你添加，并且自动发现新加入集群的机器。
		 */
		Settings settings = Settings.builder().put("cluster.name", clusterName)
				.build();

		client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), port));
	}

	/**
	 * on shutDownClient 停止es
	 */
	public static void shutDownClient() {
		client.close();
	}

	/**
	 * 获取所有index
	 */
	public static Set getAllIndices() {
		ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
		Set<String> set = isr.actionGet().getIndices().keySet();

        //@TEST
//        Set<String> setn = new HashSet<>();
//        setn.add("es_app_user_article_recommend_test");
//        setn.add("es_reg_user_article_recommend_test");

        return set;
	}

	/**
	 * 打印SearchResponse结果集
	 * 
	 * @param response
	 *            response
	 */
	static void writeSearchResponse(SearchResponse response) {
		SearchHit[] searchHitsByPrepareSearch = response.getHits().hits();

		// 获取结果集打印
		for (SearchHit searchHit : searchHitsByPrepareSearch) {
			System.out.println(searchHit.getSourceAsString());
		}
	}


	/**
	 * 获取索引doc数量
	 * @param index,type
	 * Elastic Search Java API 2.4
	 */
	public static String getClusterStatus(String index) {

		String response = client.prepareSearch(index)
				.setSize(0)
				.execute()
				.actionGet()
				.toString();

		JSONObject JsonResponse = JSONObject.parseObject(response);
		JSONObject JsonResponse_hits = JsonResponse.getJSONObject("hits");
		String count_num = JsonResponse_hits.get("total").toString();

		return count_num;

	}

	/**
	 * 聚合查询 分桶信息
	 * @param index
     */
	public static String getHistoryDateCounts(String index){

        SearchRequestBuilder sbuilder = client.prepareSearch(index);

		TermsBuilder teamAgg = AggregationBuilders.terms("date_buckets").field("create_date");
		sbuilder.addAggregation(teamAgg);
		SearchResponse response = sbuilder.execute().actionGet();

        List<String> resultList = new ArrayList<>();

		//分解 json 数据格式
		JSONObject JsonResponse = JSONObject.parseObject(response.toString());
		JSONObject jsonAggregations = JsonResponse.getJSONObject("aggregations");
		JSONObject jsonBuckets = jsonAggregations.getJSONObject("date_buckets");
		JSONArray jsonDates = jsonBuckets.getJSONArray("buckets");

		//遍历 json 数组
		for (int i=0; i<jsonDates.size(); i++){
			JSONObject jsonObject = jsonDates.getJSONObject(i);
            String date = jsonObject.get("key").toString();
			String docs = jsonObject.get("doc_count").toString();
			String results = date + ":" + docs;

            resultList.add(results);
		}

        //构建历史数据 title标签下的数据 使用 &#10;或者&#13;分隔符实现换行
        StringBuffer stringBuffer = new StringBuffer();
        for (String sb : resultList){
            stringBuffer.append(sb);
            stringBuffer.append("&#10;");
        }

        return stringBuffer.toString();

	}

	/**
	 * 获取最后一次更新数据量、更新时间
	 * @param index
	 * @return
     */
	public static String getLastUpdateTimeAndDocs(String index){

		SearchRequestBuilder sbuilder = client.prepareSearch(index);
		TermsBuilder teamAgg = AggregationBuilders.terms("date_buckets").field("create_date");
		sbuilder.addAggregation(teamAgg);
		SearchResponse response = sbuilder.execute().actionGet();

		List<String> lastDate = new ArrayList<String>();
        Map<String, String> lastDateDocs= new HashMap<String,String>();

		//分解 json 数据格式
		JSONObject JsonResponse = JSONObject.parseObject(response.toString());
		JSONObject jsonAggregations = JsonResponse.getJSONObject("aggregations");

        //打印Aggregation信息
        System.out.println(jsonAggregations.toString());

		JSONObject jsonBuckets = jsonAggregations.getJSONObject("date_buckets");
		JSONArray jsonDates = jsonBuckets.getJSONArray("buckets");

		//遍历 json 数组
		for (int i=0; i<jsonDates.size(); i++){
			JSONObject jsonObject = jsonDates.getJSONObject(i);
            //String date = jsonObject.get("key_as_string").toString().substring(0,10);
            String date = jsonObject.get("key").toString();
            String docs = jsonObject.get("doc_count").toString();

            lastDateDocs.put(date, docs);
            lastDate.add(date);
		}

		//list 转 string[]
		String[] dateArrstr = (String[]) lastDate.toArray(new String[lastDate.size()]);

		//冒泡排序
		String dateTmp = null;
		for (int i=0; i<dateArrstr.length; i++){
			for (int j=0; j<dateArrstr.length-i-1; j++){
				if (DateUtil.compareDate(dateArrstr[j],dateArrstr[j+1])){
					dateTmp = dateArrstr[j];
					dateArrstr[j] = dateArrstr[j+1];
					dateArrstr[j+1] = dateTmp;
				}
			}
		}

        if (dateArrstr.length == 0){
            return "unknown";
        } else {
            String lastDateRT = dateArrstr[dateArrstr.length-1];
            String lastDateR = null;
            //处理时间为时间戳的情况
            if (lastDateRT.contains("-")){
                lastDateR = lastDateRT;
            } else {
                lastDateR = DateUtil.stampToDate(lastDateRT);
            }
            String lastDocsR = lastDateDocs.get(lastDateRT);
            return lastDateR + "~" + lastDocsR;
        }

	}


    /**
     * 获取索引创建时间
     * @param index
     */
    public static String getIndexCretedFormatTime(String index){

        Settings stat = client.admin().cluster().prepareState()
                .execute().actionGet()
                .getState()
                .getMetaData()
                .getIndices()
                .get(index)
                .getSettings();
        Map<String,String> a  = stat.getAsMap();

        String formatLastDate = null;
        formatLastDate = DateUtil.stampToDesignatedDate(a.get("index.creation_date"),"yyyy-MM-dd HH:mm:ss");

        if (formatLastDate == null){
            return "未知创建时间";
        } else {
            return formatLastDate;
        }
    }


}