package com.douguo.uprofile.user.dao;

import com.common.base.common.Page;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("userInfoDao")
public class UserInfoDao {

    //索引名称
    private static final String userProfileIndex = "user_profile_index";

    @Resource(name = "esClient")
    private TransportClient client;

    /**
     * 全量搜索
     *
     * @param userId
     * @param userName
     * @param nickName
     * @param mobile
     * @param sex
     * @param pageNo
     * @param pageSize
     * @param sort
     * @return
     */
    public Page queryAllUsers(String userId, String userName, String nickName, String mobile, String sex, int pageNo, int pageSize, String sort) {
        // 开始
        int start = (pageNo - 1) * pageSize;

        //
        List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
        SearchResponse response = client.prepareSearch(userProfileIndex).setQuery(QueryBuilders.matchAllQuery()).setFrom(start).setSize(pageSize).execute().actionGet();

        // 总行数
        long totalSize = response.getHits().totalHits();

        for (SearchHit hit : response.getHits().getHits()) {
            String strSourrce = hit.getSourceAsString();
            Map<String, Object> map = hit.getSource();
            rowList.add(map);
        }

        Page page = new Page(start, (int) totalSize, pageSize, rowList);

        return page;
    }


    /**
     * 指定key搜索
     *
     * @param userId
     * @param userName
     * @param nickName
     * @param mobile
     * @param sex
     * @param pageNo
     * @param pageSize
     * @param sort
     * @return
     */
    public Page queryDesignatedUsers(String userId, String userName, String nickName, String mobile, String sex, int pageNo, int pageSize, String sort) {
        // 开始
        int start = (pageNo - 1) * pageSize;

        List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();

        SearchRequestBuilder srb_uid = client.prepareSearch(userProfileIndex)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user_id", userId))
                .setFrom(start)
                .setSize(pageSize);

        SearchRequestBuilder srb_un = client.prepareSearch(userProfileIndex)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("username", userName))
                .setFrom(start)
                .setSize(pageSize);

        System.out.println(">>>> userName:" + userName);

        SearchRequestBuilder srb_unk = client.prepareSearch(userProfileIndex)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("nickname", nickName))
                .setFrom(start)
                .setSize(pageSize);

        MultiSearchResponse msr = client.prepareMultiSearch()
                .add(srb_uid).add(srb_un).add(srb_unk)
                .execute()
                .actionGet();

        // 总行数
        long totalSize = 0;

        for (MultiSearchResponse.Item item : msr.getResponses()) {
            SearchResponse response = item.getResponse();
            System.out.println(response.getHits().getHits());
            for (SearchHit hit : response.getHits().getHits()) {
                String strSourrce = hit.getSourceAsString();
                Map<String, Object> map = hit.getSource();
                rowList.add(map);
            }
        }

        Page page = new Page(start, (int) totalSize, pageSize, rowList);

        return page;
    }

    /**
     * 模糊搜索
     *
     * @param pageNo
     * @param pageSize
     * @param sort
     * @return
     */
    public Page queryLikeUsers(String args, int pageNo, int pageSize, String sort) {

        // 开始
        int start = (pageNo - 1) * pageSize;

        List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();

        SearchResponse response = client.prepareSearch(userProfileIndex)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.queryStringQuery(args))
                .setFrom(start)
                .setSize(pageSize)
                .execute()
                .actionGet();

        // 总行数
        long totalSize = 0;

        for (SearchHit hit : response.getHits().getHits()) {
            String strSourrce = hit.getSourceAsString();
            Map<String, Object> map = hit.getSource();
            rowList.add(map);
        }

        Page page = new Page(start, (int) totalSize, pageSize, rowList);

        return page;
    }

}