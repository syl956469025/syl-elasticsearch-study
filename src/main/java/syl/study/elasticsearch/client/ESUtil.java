package syl.study.elasticsearch.client;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.mapper.StrictDynamicMappingException;
import syl.study.elasticsearch.Util.Mapper;
import syl.study.elasticsearch.elasticmeta.ElasticIndex;
import syl.study.elasticsearch.model.BaseEntity;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Mtime on 2016/10/17.
 */
public class ESUtil implements AutoCloseable  {



    static TransportClient client;

    /**
     * 添加单个索引数据
     * @param t
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void addIndex(T t) throws UnknownHostException {
        if (t == null){
            throw new RuntimeException("添加的索引对象不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(t.getClass());
        try {
            add(t,info);
        }catch (IndexNotFoundException e){
            System.out.println("去创建索引");
            addCore(info);
            add(t,info);
        }catch (StrictDynamicMappingException e){
            System.out.println("去建mapping");
            addFieldMapping(info);
            add(t,info);
        }
    }

    /**
     * 更新索引数据
     * @param t
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void updateIndex(T t) throws UnknownHostException{
        if (t == null){
            throw new RuntimeException("更新的索引对象不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(t.getClass());
        try {
            update(t,info);
        }catch (IndexNotFoundException e){
            System.out.println("去创建索引");
            addCore(info);
            update(t,info);
        }catch (StrictDynamicMappingException e){
            System.out.println("去建mapping");
            addFieldMapping(info);
            update(t,info);
        }
    }

    public static <T extends BaseEntity> void deleteIndex(T t)throws UnknownHostException{
        if (t == null || t.getId()== null){
            throw new RuntimeException("删除的索引对象不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(t.getClass());
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        client.prepareDelete(index.getIndexName(),
                            index.getIndexType(),
                            String.valueOf(t.getId())).get();
    }

    /**
     * 批量删除索引
     * @param tlist
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void deleteIndexList(List<T> tlist)throws UnknownHostException{
        if (tlist.isEmpty()){
            throw new RuntimeException("批量删除索引的列表不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(tlist.get(0).getClass());
        client = TClient.getClient();
        BulkRequestBuilder builder = client.prepareBulk();
        ElasticIndex index =info.getIndex();
        tlist.forEach(obj ->{
            DeleteRequestBuilder indexBuilder = client.prepareDelete(index.getIndexName(),
                    index.getIndexType(),
                    String.valueOf(obj.getId()));
            builder.add(indexBuilder);
        });
        BulkResponse response = builder.get();
    }


    /**
     * 批量添加索引
     * @param tlist
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void addIndexList(List<T> tlist) throws UnknownHostException{
        if (tlist.isEmpty()){
            throw new RuntimeException("批量添加索引的列表不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(tlist.get(0).getClass());
        try {
            addList(tlist,info);
        }catch (IndexNotFoundException e){
            System.out.println("去创建索引");
            addCore(info);
            addList(tlist,info);
        }catch (StrictDynamicMappingException e){
            System.out.println("去建mapping");
            addFieldMapping(info);
            addList(tlist,info);
        }
    }

    /**
     * 批量更新索引
     * @param tlist
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void updateIndexList(List<T> tlist)throws UnknownHostException{
        if (tlist.isEmpty()){
            throw new RuntimeException("批量更新索引的列表不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(tlist.get(0).getClass());
        try {
            updateList(tlist,info);
        }catch (IndexNotFoundException e){
            System.out.println("去创建索引");
            addCore(info);
            updateList(tlist,info);
        }catch (StrictDynamicMappingException e){
            System.out.println("去建mapping");
            addFieldMapping(info);
            updateList(tlist,info);
        }
    }





    /**
     * 批量添加索引
     * @param tlist
     * @param info
     * @param <T>
     * @return
     * @throws UnknownHostException
     * @throws IndexNotFoundException
     */
    private static <T extends BaseEntity> BulkResponse addList(List<T> tlist, Mapper.EntityInfo info)
                                                            throws UnknownHostException ,IndexNotFoundException{
        client = TClient.getClient();
        BulkRequestBuilder builder = client.prepareBulk();
        ElasticIndex index = info.getIndex();
        tlist.forEach(obj ->{
            IndexRequestBuilder indexBuilder = client.prepareIndex(index.getIndexName(),
                    index.getIndexType(),
                    String.valueOf(obj.getId()));
            indexBuilder.setSource(FastJsonUtil.bean2Json(obj));
            builder.add(indexBuilder);
        });
        BulkResponse response = builder.get();
        return response;
    }

    /**
     * 批量更新索引
     * @param tlist
     * @param info
     * @param <T>
     * @return
     * @throws UnknownHostException
     * @throws IndexNotFoundException
     */
    private static <T extends BaseEntity> BulkResponse updateList(List<T> tlist, Mapper.EntityInfo info)
                                                            throws UnknownHostException ,IndexNotFoundException{
        client = TClient.getClient();
        BulkRequestBuilder builder = client.prepareBulk();
        ElasticIndex index = info.getIndex();
        tlist.forEach(obj ->{
            UpdateRequestBuilder updateBuilder = client.prepareUpdate(index.getIndexName(),
                    index.getIndexType(),
                    String.valueOf(obj.getId()));
            updateBuilder.setDoc(FastJsonUtil.bean2Json(obj));
            builder.add(updateBuilder);
        });
        BulkResponse response = builder.get();
        return response;
    }





    /**
     * 添加索引
     * @param t
     * @param info
     * @param <T>
     * @throws UnknownHostException
     * @throws IndexNotFoundException
     */
    private static <T extends BaseEntity> void add(T t,Mapper.EntityInfo info)
                                throws UnknownHostException ,IndexNotFoundException {
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        client.prepareIndex(index.getIndexName(), index.getIndexType(), String.valueOf(t.getId()))
                .setSource(FastJsonUtil.bean2Json(t)).get();
    }

    /**
     * 更新索引
     * @param t
     * @param info
     * @param <T>
     * @throws UnknownHostException
     * @throws IndexNotFoundException
     */
    private static <T extends BaseEntity> void update(T t,Mapper.EntityInfo info)
                                        throws UnknownHostException ,IndexNotFoundException{
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
            client.prepareUpdate(index.getIndexName(), index.getIndexType(), String.valueOf(t.getId()))
                    .setDoc(FastJsonUtil.bean2Json(t)).get();
    }

    /**
     * 添加索引core
     * @param info
     * @throws UnknownHostException
     */
    private static void addCore(Mapper.EntityInfo info) throws UnknownHostException {
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate(index.getIndexName()).
                addMapping(index.getIndexType(), FastJsonUtil.bean2Json(info.getMappings()));
        if (info.getAlias()!=null){
            builder.addAlias(info.getAlias());
        }
        builder.get();
    }

    /**
     * 添加新加字段的mapping
     * @param info
     * @throws UnknownHostException
     */
    private static void addFieldMapping(Mapper.EntityInfo info)throws UnknownHostException {
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        PutMappingRequest req = new PutMappingRequest(index.getIndexName());
        req.type(index.getIndexType());
        String mapping = FastJsonUtil.bean2Json(info.getMappings());
        System.out.println(mapping);
        req.source(mapping);
        PutMappingResponse response = client.admin().indices().putMapping(req).actionGet();
    }




    @Override
    public void close() throws Exception {
        if (client !=null) client.close();
    }
}
