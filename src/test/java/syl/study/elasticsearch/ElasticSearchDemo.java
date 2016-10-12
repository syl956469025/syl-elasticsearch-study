package syl.study.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import syl.study.utils.FastJsonUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/12.
 */
public class ElasticSearchDemo {

    /**
     * 获取elasticSearch 连接
     * @return
     * @throws UnknownHostException
     */
    public TransportClient getClient() throws UnknownHostException {
        TransportClient client = null;
        //如果集群的名称不是elasticsearch， 就需要设置集群的名称
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "shiyanlei")
                .put("client.transport.sniff", true)
                .build();
        client = TransportClient.builder().settings(settings).build();
        //设置transport addresses  是通过9300端口进行通讯的
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.52.104"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.52.105"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.51.105"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.51.104"), 9300));
        return client;
    }

    /**
     * 添加索引
     * @throws UnknownHostException
     */
    @Test
    public void addIndex() throws UnknownHostException {
        TransportClient client = getClient();
        //插入数据到elasticsearch
        Map<String,Object> customer = new HashMap<>();
        customer.put("id","1");
        customer.put("age","12");
        customer.put("name",new String[]{"zhangsan","lisi"});
        customer.put("birthday", LocalDateTime.now());
        String json = FastJsonUtil.bean2Json(customer);
        IndexResponse response = client.prepareIndex("productor", "product","1").setSource(json).get();
        String id = response.getId();
        System.out.println(id);
        String index = response.getIndex();
        System.out.println(index);
        String type = response.getType();
        System.out.println(type);
        long version = response.getVersion();
        System.out.println(version);
        client.close();
    }


    /**
     * 根据其他字段检索索引
     * @throws UnknownHostException
     */
    @Test
    public void getIndex() throws UnknownHostException {
        TransportClient client = getClient();
        TermQueryBuilder query = QueryBuilders.termQuery("name", "1");
        SearchResponse response = client.prepareSearch("customer")
                .setTypes("custom")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .execute().actionGet();
        System.out.println(response.toString());
        client.close();

    }

    /**
     * 根据主键ID获取索引
     * @throws UnknownHostException
     */
    @Test
    public void getIndexById() throws UnknownHostException {
        TransportClient client = getClient();
        GetResponse response = client.prepareGet("customer", "custom", "1").get();

        System.out.println(FastJsonUtil.bean2Json(response));
        client.close();
    }

    /**
     * 更新索引
     * @throws UnknownHostException
     */
    @Test
    public void updateIndex() throws UnknownHostException {
        TransportClient client = getClient();
        Map<String,Object> customer = new HashMap<>();
        customer.put("id","2");
        customer.put("age","13");
        customer.put("name",null);
        UpdateResponse response = client.prepareUpdate("customer", "custom", "1").setDoc(customer).get();
        System.out.println(FastJsonUtil.bean2Json(response));
        client.close();
    }

    /**
     * 删除索引
     * @throws UnknownHostException
     */
    public void deleteIndex() throws UnknownHostException {
        TransportClient client = getClient();
        DeleteResponse response = client.prepareDelete("customer", "custom", "1").get();
        System.out.println(response.toString());
        client.close();
    }
}
