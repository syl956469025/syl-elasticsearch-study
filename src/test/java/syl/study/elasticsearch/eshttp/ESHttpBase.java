package syl.study.elasticsearch.eshttp;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * @author 史彦磊
 * @create 2017-11-01 15:29.
 */
public class ESHttpBase {


    public void pointSearchResponse(SearchResponse res){
        SearchHits hits = res.getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            System.out.println(source);
        }
    }


}
