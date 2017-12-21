package com.douguo.uprofile.user.dao;

import com.common.base.common.Page;
import com.douguo.hbase.util.HbaseClientFactory;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userProfileDao")
public class UserProfileDao {

    //索引名称
    private final String ip = "192.168.1.162";
    private final String port = "2181";

    public Map<String, String> getUserProfile (String table_name,String row_key, String column_family, int version) throws IOException {
        Map<String, String> map = new HashMap<String,String>();

        HbaseClientFactory.getConn(ip,port);

        try {
            map = HbaseClientFactory.getAllDateByRowKeyAndCfAndVersion(table_name,row_key,column_family,version);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HbaseClientFactory.closeConnection();

        return map;
    }

}