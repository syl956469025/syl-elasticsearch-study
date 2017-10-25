package syl.study.elasticsearch.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Mtime on 2016/10/13.
 */
public class TClient{


    private static TransportClient client;

    private static Object _lock = new Object();



    public static TransportClient getClient() throws UnknownHostException {
        if (client == null){
            synchronized (_lock){
                if (client == null){
                    initCluster();
                }
            }
        }
        return client;
    }


    /**
     * 初始化集群
     * @throws UnknownHostException
     */
    private static void initCluster() throws UnknownHostException {
        //如果集群的名称不是elasticsearch， 就需要设置集群的名称
        Settings settings = Settings.builder()
                .put("cluster.name", "elastic-5.6.3")
                .put("client.transport.sniff", true).build();
        TClient.client = new PreBuiltTransportClient(settings);
//        TClient.client = TransportClient.builder().settings(settings).build();
        //设置transport addresses  是通过9300端口进行通讯的,:9310,10.0.0.51:9310
        TClient.client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.55.139"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.55.140"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.55.141"), 9300));
    }
}
