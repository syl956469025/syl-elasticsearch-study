package syl.study.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/20.
 */
public class ElasticPPT extends BaseElasticSearchTest {


    private static final String TWITTER = "complex";
    private static final String TWIT = "child";

    /**
     * 创建索引
     */
    @Test
    public void addIndex(){
        Settings set = Settings.settingsBuilder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1)
                .build();
        CreateIndexRequest request = new CreateIndexRequest(TWITTER,set);
        CreateIndexResponse response = client.admin().indices().create(request).actionGet();
        json(response);
    }

    /**
     * 添加文档
     */
    @Test
    public void addDocument(){
        IndexRequestBuilder builder = client.prepareIndex(TWITTER, "twit", "2");
        Map<String,Object> source =new HashMap<>();
        source.put("id",2);
        source.put("age",12);
        builder.setSource(source);
        IndexResponse response = builder.get();
        json(response);
    }

    /**
     * 添加NestedDocument
     * 1. 首先建立mapping的时候指定type为nested
     */
    @Test
    public void addNestDocument(){

    }


    /**
     * 更新文档
     */
    @Test
    public void updateDocument(){
        UpdateRequestBuilder builder = client.prepareUpdate(TWITTER, TWIT, "4");
        Map<String,Object> source =new HashMap<>();
        source.put("id",1);
        source.put("age",12);
        builder.setDoc(source);
        UpdateResponse response = builder.get();
        json(response);
    }

    /**
     * 更新或插入索引
     */
    @Test
    public void upsertDocument(){
        UpdateRequest request = new UpdateRequest(TWITTER,TWIT,"3");
        Map<String,Object> source =new HashMap<>();
        source.put("id",3);
        source.put("name","张无忌3");
        source.put("age",null);
        request.doc(source);
        request.upsert(source);
        UpdateResponse response = client.update(request).actionGet();
        json(response);
    }

    @Test
    public void deleteDocument(){
        DeleteResponse response = client.prepareDelete(TWITTER, TWIT, "1").get();
        json(response);
    }


    @Test
    public void query(){
        SearchResponse response = client.prepareSearch(TWITTER)
                .setTypes(TWIT)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.boolQuery())
                .setFrom(0)
                .setSize(10)
                .addSort("age", SortOrder.DESC)
                .get();
        writeSearchResponse(response);
    }



}
