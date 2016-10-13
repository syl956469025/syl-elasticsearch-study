package syl.study.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.After;
import org.junit.Before;
import syl.study.elasticsearch.client.TClient;

import java.net.UnknownHostException;

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



    @After
    public void close(){
        client.close();
    }
}
