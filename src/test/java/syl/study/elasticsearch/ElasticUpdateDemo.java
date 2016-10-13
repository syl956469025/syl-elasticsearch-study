package syl.study.elasticsearch;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import syl.study.utils.FastJsonUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticUpdateDemo {

    private TransportClient client;

    /**
     * 获取elasticSearch 连接
     *
     * @return
     * @throws UnknownHostException
     */
    @Before
    public void getClient() throws UnknownHostException {
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
    }


    /**
     * 通过 groovy 脚本更新索引
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updateByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("customer").type("custom").id("2");
        Map<String, String> param = new HashMap<>();
        param.put("newName", "zhangsan");
        request.script(new Script("ctx._source.name=newName", ScriptService.ScriptType.INLINE, null, param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 通过groovy 脚本更新索引的数组类型的字段
     * 往数组中添加字段
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updateListFieldByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("productor").type("product").id("1");
        Map<String, String> param = new HashMap<>();
        param.put("anotherName", "zhangsan");
//        request.script(new Script("ctx._source.name += anotherName", ScriptService.ScriptType.INLINE,"groovy",param));
        request.script(new Script("ctx._source.name -= anotherName", ScriptService.ScriptType.INLINE,"groovy",param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 通过脚本添加新字段
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void addNewFieldByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("productor").type("product").id("1");
        Map<String,Object> param = new HashMap<>();
        param.put("prefers",new String[]{"swim","eat","run","sing"});
        request.script(new Script("ctx._source.prefer=prefers", ScriptService.ScriptType.INLINE,"groovy",param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 使用groovy 脚本删除一个字段
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void addRemoveFieldByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("productor").type("product").id("1");
        Map<String,String> param = new HashMap<>();
        param.put("removeField","prefer");
        request.script(new Script("ctx._source.remove(removeField)", ScriptService.ScriptType.INLINE,"groovy",param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 使用groovy 脚本 upsert 索引
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updsertByScript() throws ExecutionException, InterruptedException {
        IndexRequest index = new IndexRequest("productor","product","2");
        index.source("id","2","name","zhaoliu","age",54);
        UpdateRequest update = new UpdateRequest("productor","product","2");
        update.doc("name","zhaoqi").upsert(index);
        UpdateResponse response = client.update(update).get();
        System.out.println(FastJsonUtil.bean2Json(response));

    }


    /**
     * 使用doc 更新索引
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updateByDoc() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest("productor","product","2");
        request.doc("birthday", LocalDateTime.now());
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }



    /**
     * 关闭连接
     */
    @After
    public void close() {
        client.close();
    }
}
