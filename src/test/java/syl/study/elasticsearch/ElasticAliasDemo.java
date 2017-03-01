package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

import java.net.UnknownHostException;

/**
 * ES 别名使用示例
 *
 * @author 史彦磊
 * @create 2017-01-10 12:50.
 */
public class ElasticAliasDemo extends BaseElasticSearchTest  {


    @Test
    public void testAlias() throws UnknownHostException {
        SearchResponse res = this.client.prepareSearch("members")
                .setTypes("membercore")
                .setQuery(QueryBuilders.queryStringQuery("id:D1039000000000001318"))
                .get();
        for (SearchHit hit : res.getHits().getHits()) {
            String src = hit.getSourceAsString();
            System.out.println(src);
        }
    }



}
