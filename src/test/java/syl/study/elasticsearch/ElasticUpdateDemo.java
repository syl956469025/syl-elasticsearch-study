package syl.study.elasticsearch;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.junit.Test;
import syl.study.utils.FastJsonUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticUpdateDemo extends BaseElasticSearchTest{




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
        request.script(new Script("ctx._source.name=newName", ScriptService.ScriptType.FILE, null, param));
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
        request.index("productor").type("product").id("4");
        Map<String,Object> param = new HashMap<>();
//        param.put("prefers",new String[]{"swim","eat","run","sing"});
        request.script(new Script("ctx._source.prefer=prefers", ScriptService.ScriptType.INLINE,"groovy",param));
        param.put("username","张无忌4");
        param.put("nickname","张无忌4");
        request.script(new Script("ctx._source.username=username", ScriptService.ScriptType.INLINE,"groovy",param));
        request.script(new Script("ctx._source.nickname=nickname", ScriptService.ScriptType.INLINE,"groovy",param));
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



}
