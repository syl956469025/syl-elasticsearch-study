package syl.study.elasticsearch;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticBulkDemo extends BaseElasticSearchTest{


    /**
     * 批量插入数据
     */
    @Test
    public void addIndexList(){
        BulkRequestBuilder builder = client.prepareBulk();
        IndexRequestBuilder indexBuilder1 = client.prepareIndex("productor", "product", "1");
        Map<String,Object> productor1 = new HashMap<>();
        productor1.put("id","3");
        productor1.put("age","12");
        productor1.put("name",new String[]{"zhangsan","lisi"});
        productor1.put("birthday", LocalDateTime.now());
        productor1.put("nickname", "张无忌");
        productor1.put("username", "张无忌");

        indexBuilder1.setSource(productor1);
        IndexRequestBuilder indexBuilder2 = client.prepareIndex("productor", "product", "2");
        Map<String,Object> productor2 = new HashMap<>();
        productor2.put("id","4");
        productor2.put("age","12");
        productor2.put("name",new String[]{"zhangsan","lisi"});
        productor2.put("birthday", LocalDateTime.now());
        productor2.put("nickname", "张无忌2");
        productor2.put("username", "张无忌2");
        indexBuilder2.setSource(productor2);
        IndexRequestBuilder indexBuilder3 = client.prepareIndex("productor", "product", "3");
        Map<String,Object> productor3 = new HashMap<>();
        productor3.put("id","3");
        productor3.put("age","12");
        productor3.put("name",new String[]{"zhangsan","lisi"});
        productor3.put("birthday", LocalDateTime.now());
        productor3.put("nickname", "张无忌3");
        productor3.put("username", "张无忌3");
        indexBuilder3.setSource(productor3);
        IndexRequestBuilder indexBuilder4 = client.prepareIndex("productor", "product", "4");
        Map<String,Object> productor4 = new HashMap<>();
        productor4.put("id","4");
        productor4.put("age","12");
        productor4.put("name",new String[]{"zhangsan","lisi"});
        productor4.put("birthday", LocalDateTime.now());
        productor4.put("nickname", "张无忌4");
        productor4.put("username", "张无忌4");
        indexBuilder4.setSource(productor4);
        BulkResponse response = builder.add(indexBuilder1).add(indexBuilder2).add(indexBuilder3).add(indexBuilder4).get();
        if (response.hasFailures()){
            response.forEach(res ->{
                String errmsg = res.getFailureMessage();
                System.out.println(errmsg);
            });
        }

    }


    /**
     * 批量删除索引
     */
    @Test
    public void deleteIndexList(){
        BulkRequestBuilder builder = client.prepareBulk();
        DeleteRequest delete1 = new DeleteRequest("complex","child","1");
        DeleteRequest delete2 = new DeleteRequest("complex","child","3");
//        DeleteRequest delete3 = new DeleteRequest("productor","product","3");
//        DeleteRequest delete4 = new DeleteRequest("productor","product","4");
//        BulkResponse response = builder.add(delete1).add(delete2).add(delete3).add(delete4).get();
        BulkResponse response = builder.add(delete1).add(delete2).get();
        if (response.hasFailures()){
            response.forEach(res ->{
                String errmsg = res.getFailureMessage();
                System.out.println(errmsg);
            });
        }
    }


    @Test
    public void testImportJsonFile(){
        BulkRequestBuilder builder = client.prepareBulk();
//        builder.add(QueryBuilders.boolQuery())

    }

}
