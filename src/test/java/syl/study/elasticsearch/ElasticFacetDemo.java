package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.junit.Test;
import syl.study.elasticsearch.client.ESSearchUtil;
import syl.study.elasticsearch.elasticmeta.SearchResult;
import syl.study.elasticsearch.model.IndexAggGroup;
import syl.study.elasticsearch.model.Member;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/21.
 */
public class ElasticFacetDemo extends BaseElasticSearchTest{


    @Test
    public void facetTest(){
        GlobalAggregationBuilder global = AggregationBuilders.global("sregion").subAggregation(
                AggregationBuilders.terms("term").field("name").subAggregation(
                        AggregationBuilders.topHits("top")
                )
        ).subAggregation(
                AggregationBuilders.terms("term2").field("status").subAggregation(
                        AggregationBuilders.topHits("top")
                )
        );
        SearchResponse response = client.prepareSearch("sregion").setTypes("sregion")
                .addAggregation(global)
                .get();
        Global sregion = response.getAggregations().get("sregion");

        Terms term2 = sregion.getAggregations().get("term2");
        for (Terms.Bucket bucket : term2.getBuckets()) {
            System.out.println(bucket.getKey());
            System.out.println(bucket.getDocCount());
            TopHits top = bucket.getAggregations().get("top");
            for (SearchHit hit : top.getHits()) {
                System.out.println(hit.getSourceAsString());
            }
        }
    }


    @Test
    public void groupTest() throws UnknownHostException {
        IndexAggGroup group = new IndexAggGroup();
        group.addAggField("age");
        group.addFuncAgg("age",IndexAggGroup.Func.AVG);
        group.addFuncAgg("age",IndexAggGroup.Func.COUNT);
        group.addFuncAgg("age",IndexAggGroup.Func.MAX);
        group.addFuncAgg("age",IndexAggGroup.Func.MIN);
        group.addFuncAgg("age",IndexAggGroup.Func.SUM);
        SearchResult<Member> query = ESSearchUtil.query(Member.class, group, 1, 30);
        Map<String, Map<Object, Long>> aggResult = query.getAggResult();
        for (Map.Entry<String, Map<Object, Long>> entry : aggResult.entrySet()) {
            System.out.println("field: "+entry.getKey());
            Map<Object, Long> value = entry.getValue();
            for (Map.Entry<Object, Long> map : value.entrySet()) {
                System.out.println("key : "+map.getKey() + "  count: "+map.getValue());
            }
        }

        Map<String, Map<IndexAggGroup.Func, Object>> aggGroup = query.getAggGroup();
        for (Map.Entry<String, Map<IndexAggGroup.Func, Object>> entry : aggGroup.entrySet()) {
            System.out.println("field: "+entry.getKey());
            Map<IndexAggGroup.Func, Object> value = entry.getValue();
            for (Map.Entry<IndexAggGroup.Func, Object> map : value.entrySet()) {
                System.out.println(map.getKey().name() + "  :  "+map.getValue());
            }
        }



    }


}
