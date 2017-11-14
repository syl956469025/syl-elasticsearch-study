package syl.study.elasticsearch.eshttp;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import syl.study.utils.FastJsonUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ES 查询 示例
 *
 * @author 史彦磊
 * @create 2017-11-01 11:47.
 */
public class ESHttpSearchDemo extends ESHttpBase {


    @Test
    public void getDocById() throws IOException {
        GetRequest request = new GetRequest("member","member","9");
        GetResponse res = ESRestClient.getClient().get(request);
        String str = res.getSourceAsString();
        System.out.println(str);
    }

    @Test
    public void searchDoc() throws IOException {
        SearchRequest request = new SearchRequest("member");
        request.types("member");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.queryStringQuery("id:4"));
        builder.from(0);
        builder.size(3);
        builder.sort("birthday", SortOrder.DESC);
        builder.fetchSource(false);
        request.source(builder);
        SearchResponse res= ESRestClient.getClient().search(request);
        SearchHits hits = res.getHits();
        for (SearchHit hit : hits) {
            String str = hit.getSourceAsString();
            System.out.println(str);
        }
    }


    @Test
    public void scrollSearch() throws IOException {
        SearchRequest request = new SearchRequest("member");
        request.types("member");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(2);
        request.source(builder);
        request.scroll(new Scroll(new TimeValue(1, TimeUnit.MINUTES)));
        SearchResponse res = ESRestClient.getClient().search(request);
//        SearchHits hits = res.getHits();
        String scrollId = res.getScrollId();

        SearchScrollRequest req = new SearchScrollRequest(scrollId);
        req.scroll(new TimeValue(30,TimeUnit.SECONDS));
        SearchResponse scrollReq = ESRestClient.getClient().searchScroll(req);
        System.out.println(scrollId);
        System.out.println(scrollReq.getScrollId());
        System.out.println(scrollId.equals(scrollReq.getScrollId()));
        clearScroll(scrollId);
    }

//    @Test
    public void clearScroll(String scrollId) throws IOException {
        ClearScrollRequest request = new ClearScrollRequest();
        request.addScrollId(scrollId);
        ClearScrollResponse response = ESRestClient.getClient().clearScroll(request);
        System.out.println(FastJsonUtil.bean2Json(response));
    }

    @Test
    public void matchQuery() throws IOException {
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("age", 3);
        query(matchQuery);
    }


    @Test
    public void nestedQuery() throws IOException {
        TermQueryBuilder term = QueryBuilders.termQuery("points.userId", 1);
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("points",term, ScoreMode.None);
        query(nestedQuery);
    }


    public void query(QueryBuilder query) throws IOException {
        SearchRequest request = new SearchRequest("member").types("member");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(10);
        builder.from(0);
        builder.query(query);
        request.source(builder);
        SearchResponse response = ESRestClient.getClient().search(request);
        pointSearchResponse(response);
    }

    public void slicedScroll(){

    }


}
