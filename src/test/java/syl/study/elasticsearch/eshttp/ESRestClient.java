package syl.study.elasticsearch.eshttp;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * ES 5.6.3 REST api 客户端
 *
 * @author 史彦磊
 * @create 2017-10-27 15:52.
 */
public class ESRestClient {



    public static RestHighLevelClient getClient(){
        RestClient restClient = RestClient.builder(
                new HttpHost("192.168.55.139", 9200, "http"),
                new HttpHost("192.168.55.140", 9200, "http"),
                new HttpHost("192.168.55.141", 9200, "http")).build();
        return new RestHighLevelClient(restClient);
    }


}
