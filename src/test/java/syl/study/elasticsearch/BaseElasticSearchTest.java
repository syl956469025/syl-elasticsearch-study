package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.junit.After;
import org.junit.Before;
import syl.study.elasticsearch.client.TClient;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Mtime on 2016/10/13.
 */
public class BaseElasticSearchTest {

    protected TransportClient client;


    /**
     * 获取elasticSearch 连接
     * @throws UnknownHostException
     */
    @Before
    public void getClient() throws UnknownHostException {
        client = TClient.getClient();
    }



    protected static void writeSearchResponse(SearchResponse response) {
        SearchHit[] searchHitsByPrepareSearch = response.getHits().hits();
        //获取结果集打印
        for (SearchHit searchHit : searchHitsByPrepareSearch) {
            System.out.println(searchHit.getSourceAsString());
        }
    }


    /**
     * SearchResponse结果集转Map
     * 获取某指标的值set去重打印
     *
     * @param response response
     * @param filed    获取指标的字段名称
     */
    protected static void writeSearchResponseToMap(SearchResponse response, String filed) {
        if (null == response) {
            System.out.println("~~~~~~~~~ response is null ~~~~~~~~~");
            return;
        }
        SearchHit[] searchHitsByPrepareSearch = response.getHits().hits();
        Set<String> stringSet = new TreeSet<>();
        //获取结果集打印
        for (SearchHit searchHit : searchHitsByPrepareSearch) {
            String s = searchHit.getSourceAsString();
            Map map = FastJsonUtil.json2Bean(s,HashMap.class);
/*
            set.stream().filter(s1 -> s1.equals(filed)).forEach(s1 -> {
                long aLong = Long.parseLong((map.get(s1) + ""));
                stringSet.add(DATETIME_FORMATTER.format(new Date(aLong * 1000L)));
            });
*/
        }
        //stringSet.forEach(System.out::println);
    }

    /**
     * 输出json
     * @param obj
     */
    public static void json(Object obj){
        System.out.println(FastJsonUtil.bean2Json(obj));
    }



    @After
    public void close(){
        client.close();
    }
}
