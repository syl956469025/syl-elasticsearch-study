package syl.study.elasticsearch.client;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import syl.study.elasticsearch.model.BaseEntity;

import java.net.UnknownHostException;

/**
 * Created by Mtime on 2016/10/17.
 */
public class ESUtil implements AutoCloseable  {

    TransportClient client;

    public <T extends BaseEntity> void addIndex(T t) throws UnknownHostException {
        client = TClient.getClient();
        IndexRequest request = new IndexRequest();
        request.create(true);
        request.opType(IndexRequest.OpType.CREATE);
        PutMappingRequest requests = new PutMappingRequest();







    }


    @Override
    public void close() throws Exception {
        if (client !=null) client.close();
    }
}
