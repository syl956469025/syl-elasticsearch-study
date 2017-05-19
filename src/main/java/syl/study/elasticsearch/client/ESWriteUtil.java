package syl.study.elasticsearch.client;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.mapper.StrictDynamicMappingException;
import syl.study.elasticsearch.Util.Mapper;
import syl.study.elasticsearch.Util.StrKit;
import syl.study.elasticsearch.elasticmeta.ElasticIndex;
import syl.study.elasticsearch.model.BaseEntity;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mtime on 2016/10/17.
 */
public class ESWriteUtil {



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

    /**
     * 删除单个索引
     * @param clazz
     * @param id
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void deleteIndex(Class<T> clazz ,String id)throws UnknownHostException{
        if (clazz == null || StrKit.isBlank(id)){
            throw new RuntimeException("删除的索引对象不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        client.prepareDelete(index.getIndexName(),
                            index.getIndexType(),
                            id).get();
    }

    /**
     * 批量删除索引
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void deleteIndexList(Class<T> clazz,List<String> ids)throws UnknownHostException{
        if (clazz == null || ids.isEmpty()){
            throw new RuntimeException("批量删除索引的列表不可以为空");
        }
        Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
        client = TClient.getClient();
        BulkRequestBuilder builder = client.prepareBulk();
        ElasticIndex index =info.getIndex();
        ids.forEach(id ->{
            DeleteRequestBuilder indexBuilder = client.prepareDelete(index.getIndexName(),
                    index.getIndexType(),
                    id);
            builder.add(indexBuilder);
        });
        BulkResponse response = builder.setRefresh(true).get();
        System.out.println(FastJsonUtil.bean2Json(response));
        processException(response);
    }


    /**
     * 批量添加索引
     * @param tlist
     * @param <T>
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> void addIndexList(List<T> tlist) throws UnknownHostException, ExecutionException, InterruptedException {
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
            throws UnknownHostException, IndexNotFoundException,ExecutionException, InterruptedException  {
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
//        IndicesExistsRequest req = new IndicesExistsRequest(index.getIndexName());
//        IndicesExistsResponse resp = client.admin().indices().exists(req).get();
//        if (!resp.isExists()){
//            throw new IndexNotFoundException("索引不存在");
//        }
        BulkRequestBuilder builder = client.prepareBulk();
        tlist.forEach(obj ->{
            IndexRequestBuilder indexBuilder = client.prepareIndex(index.getIndexName(),
                    index.getIndexType(),
                    String.valueOf(obj.getId()));
            indexBuilder.setSource(FastJsonUtil.bean2Json(obj));
            builder.add(indexBuilder);
        });
        BulkResponse response = builder.get();
        processException(response);
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
        processException(response);
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
     * 如果发现没有该索引则新建
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
        String source = FastJsonUtil.bean2Json(t);
        IndexRequest indexRequest = new IndexRequest(index.getIndexName(),
                index.getIndexType(),
                String.valueOf(t.getId()))
                .source(source);
        UpdateRequest updateRequest = new UpdateRequest(index.getIndexName(),
                index.getIndexType(),
                String.valueOf(t.getId()))
                .doc(source)
                .upsert(indexRequest);
        UpdateResponse response = client.update(updateRequest).actionGet();
    }

    /**
     * 添加索引core
     * @param info
     * @throws UnknownHostException
     */
    private static void addCore(Mapper.EntityInfo info) throws UnknownHostException {
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        Settings settings = Settings.settingsBuilder()
                .put("number_of_shards",3)
                .put("number_of_replicas",1)
                .put("max_result_window",12223)
                .build();
        CreateIndexRequestBuilder builder = client.admin().indices()
                .prepareCreate(index.getIndexName())
                .setSettings(settings)
                .addMapping(index.getIndexType(), FastJsonUtil.bean2Json(info.getMappings()));
//        if (info.getAlias()!=null){
//            builder.addAlias(info.getAlias());
//        }
        builder.get();
    }

    /**
     * 添加新加字段的mapping
     * @param info
     * @throws UnknownHostException
     */
    private static void addFieldMapping(Mapper.EntityInfo info) throws UnknownHostException {
        client = TClient.getClient();
        ElasticIndex index = info.getIndex();
        PutMappingRequest req = new PutMappingRequest(index.getIndexName());
        req.type(index.getIndexType());
        String mapping = FastJsonUtil.bean2Json(info.getMappings());
        System.out.println(mapping);
        req.source(mapping);
        PutMappingResponse putMappingResponse = client.admin().indices().putMapping(req).actionGet();
    }

    /**
     * 处理异常
     * @param response
     */
    private static void processException(BulkResponse response){
        if(response.hasFailures()){
            for(BulkItemResponse res : response){
                String failureMessage = res.getFailureMessage();
                if (failureMessage.indexOf("IndexNotFoundException") != -1){
                    throw new IndexNotFoundException("不存在该索引");
                }else if (failureMessage.indexOf("StrictDynamicMappingException") != -1){
                    throw new StrictDynamicMappingException("","");
                }else {
                    throw new RuntimeException(res.getFailure().getCause());
                }
            }
        }
    }

}
