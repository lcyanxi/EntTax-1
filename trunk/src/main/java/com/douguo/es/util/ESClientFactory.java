package com.douguo.es.util;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ESClientFactory {

    public ESClientFactory() {

    }

    @Value("${es.clusterName}")
    private String clusterName;

    @Value("${es.serverIp}")
    private String serverIp;

    @Value("${es.serverPort}")
    private Integer serverPort;

    @Bean(name = "esClient")
    public TransportClient getESClient() {
        TransportClient client = null;
        try {
            Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();
            client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), serverPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}