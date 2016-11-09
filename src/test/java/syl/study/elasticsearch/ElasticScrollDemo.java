package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

/**
 * Created by Mtime on 2016/11/7.
 */
public class ElasticScrollDemo extends BaseElasticSearchTest {


    @Test
    public void testScroll(){
        SearchResponse response =  client.prepareSearch("sgovregion")
                .setTypes("sgovregion")
                .setScroll(new TimeValue(10000))
                .setSize(100)
                .get();

        String scrollId = response.getScrollId();
        SearchResponse res;
        do {
                res = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(10000)).get();
                SearchHit[] hit = res.getHits().getHits();
                for (SearchHit h : hit) {
                    System.out.println(h.getSourceAsString());
                }
                System.out.println(Thread.currentThread().getName());

        }while (res.getHits().getHits().length!=0);


    }



}
