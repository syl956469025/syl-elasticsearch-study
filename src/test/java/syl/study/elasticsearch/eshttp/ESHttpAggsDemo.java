package syl.study.elasticsearch.eshttp;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * ES http 聚合 示例
 *
 * @author 史彦磊
 * @create 2017-11-01 14:28.
 */
public class ESHttpAggsDemo {


    @Test
    public void testTermAgg() throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermsAggregationBuilder termAgg = AggregationBuilders.terms("csTerm")
                .field("age");
        builder.aggregation(termAgg);
        SearchRequest request = new SearchRequest("member").types("member");
        request.source(builder);
        SearchResponse response = ESRestClient.getClient().search(request);
//        System.out.println(FastJsonUtil.bean2Json(response));
        Aggregations agg = response.getAggregations();
        Terms term = agg.get("csTerm");
        List<? extends Terms.Bucket> buckets = term.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println("key:"+bucket.getKey()+" num:"+bucket.getDocCount());
        }

    }




}
