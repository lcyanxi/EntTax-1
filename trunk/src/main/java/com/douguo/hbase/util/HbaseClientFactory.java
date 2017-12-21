package com.douguo.hbase.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * HBASE 客户端代理工厂
 */
public class HbaseClientFactory {

    // Hbase connection object
    private static Connection conn;

    // database metadate connection object
    private static Admin admin;

    // get logs
    private static Log logger = LogFactory.getLog(HbaseClientFactory.class);


    /**
     *  get hbase connection object
     *  @author JianfeiZhang
     *  @since 1.0.0
     */
    public static void getConn (String host, String port) {

        // get configuration params object
        Configuration conf = HBaseConfiguration.create();

        //set connection params, ip
        conf.set("hbase.zookeeper.quorum", host);

        //set connection params, port
        conf.set("hbase.zookeeper.property.clientPort", port);

        try {
            //get connetion
            conn = ConnectionFactory.createConnection(conf);
            //get admin
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (admin == null){
            logger.error("connection failed. check it please ... ");
        } else {
            logger.info(" the connection is successful ! ready to perform the operation ... ");
        }

    }


    /**
     * hbase close connections
     * including close connection and admin
     * @throws IOException
     * @author JianfeiZhang
     * @since 1.0.0
     */
    public static void closeConnection() throws IOException{

        try {
            admin.close();
            conn.close();
            logger.info("successfuly closed connections !");
        } catch (Exception e) {
            logger.error("failed during colsing connections !");
            e.printStackTrace();
        }
    }


    /**
     * 根据行键值按行获取多个version版本的数据
     * @param tableName
     * @param rowKeyName
     * @param versions
     * @throws IOException
     */
    public static Map<String, String> getAllDateByRowKeyAndCfAndVersion (String tableName, String rowKeyName, String columnFamilyName, int versions) throws IOException {
        System.out.println("根据 rowkey、cf、verions 获取值");
        System.out.println("开始查询："+tableName + " 中的：" + rowKeyName + "， 列族：" + columnFamilyName + "， 版本数：" + versions);

        // test
        long startTime = System.currentTimeMillis();

        // get hbase table object
        Table table = conn.getTable(TableName.valueOf(tableName));

        // create query object
        Get get = new Get(Bytes.toBytes(rowKeyName));

        // set version nums
        get.setMaxVersions(versions);

        // query by rowKey
        Result result = table.get(get);

        // parse result as map
        Map<String,String> map = formatResultMap(result);

        //test
        long endTime = System.currentTimeMillis();
        System.out.println("查询耗时：" + (endTime - startTime) + "ms");

        return map;
    }


    /**
     * 格式化并输出Result结果
     * @param result
     */
    private static void formatResult (Result result) {
        for (Cell cell : result.rawCells()){
            String rowKey = Bytes.toString (result.getRow());
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes. toString (CellUtil. cloneQualifier (cell));
            String value =  Bytes. toString (CellUtil. cloneValue (cell));

            String jsonStr = "{\"rowkey\":\"" + rowKey +
                    "\",\"family\":\"" + family +
                    "\",\"qualifier\":\"" + qualifier +
                    "\",\"value\":\"" + value +
                    "\"}";
            System.out.println(jsonStr);
        }
    }

    /**
     * 格式化Result结果为map
     * @param result
     */
    private static Map<String,String> formatResultMap (Result result) {
        Map<String,String> map = new HashMap<String, String>();

        for (Cell cell : result.rawCells()){
//			String rowKey = Bytes.toString (result.getRow());
//			String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes. toString (CellUtil. cloneQualifier (cell));
            String value =  Bytes. toString (CellUtil. cloneValue (cell));

            map.put(qualifier, value);
        }

        System.out.println("共获取字段：" + map.size() + " 个");

        return map;
    }


}
