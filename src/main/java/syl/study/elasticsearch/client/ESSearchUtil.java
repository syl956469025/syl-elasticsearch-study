package syl.study.elasticsearch.client;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.GlobalBuilder;
import org.elasticsearch.search.sort.SortOrder;
import syl.study.elasticsearch.Util.Mapper;
import syl.study.elasticsearch.Util.StrKit;
import syl.study.elasticsearch.elasticmeta.ElasticIndex;
import syl.study.elasticsearch.model.BaseEntity;
import syl.study.elasticsearch.model.IndexAgg;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mtime on 2016/10/18.
 */
public class ESSearchUtil {
    static TransportClient client;

    /**
     * 根据id查询
     * @param clazz
     * @param id
     * @param <T>
     * @return
     * @throws UnknownHostException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static <T extends BaseEntity> Map<String, Object> getById(Class<T> clazz,String id)
                        throws UnknownHostException {
        if (clazz == null || StrKit.isBlank(id)){
            throw new RuntimeException("查询的id不可以为空");
        }
        client = TClient.getClient();
        Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
        ElasticIndex index = info.getIndex();
        GetRequest req = new GetRequest(index.getIndexName(), index.getIndexType(), id);
        GetResponse response = client.get(req).actionGet();
        return response.getSource();
    }

    /**
     * 根据条件分页查询
     * @param clazz
     * @param params
     * @param sort
     * @param filterQuery
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> SearchResponse query(Class<T> clazz,
                                                                Map<String,Object> params,
                                                                Map<String, SortOrder> sort,
                                                                String filterQuery,
                                                                Map<String,Map<String,Object>> nestParams,
                                                                Map<String,String> nestFilterString,
                                                                IndexAgg indexAgg,
                                                                int pageIndex,int pageSize) throws UnknownHostException {
        client = TClient.getClient();
        Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
        ElasticIndex index = info.getIndex();
        SearchRequestBuilder builder = client.prepareSearch(index.getIndexName())
                .setTypes(index.getIndexType())
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setFrom((pageIndex - 1) * pageSize)
                .setSize(pageSize);

        //查询条件
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        if (params !=null && !params.isEmpty()){
            String query = queryStr(params);
            booleanQueryBuilder.must(QueryBuilders.queryStringQuery(query));
        }
        //查询条件
        if (!StrKit.isBlank(filterQuery)){
            booleanQueryBuilder.must(QueryBuilders.queryStringQuery(filterQuery));
        }
        //nested 查询条件
        if (nestParams !=null && !nestParams.isEmpty()){
            nestParams.entrySet().forEach(nest ->{
                String query = queryStr(nest.getValue());
                NestedQueryBuilder nestedQueryBuilder =
                        QueryBuilders.nestedQuery(nest.getKey(), QueryBuilders.queryStringQuery(query));
                booleanQueryBuilder.must(nestedQueryBuilder);
            });
        }
        //nested 查询条件
        if (nestFilterString !=null && !nestFilterString.isEmpty()){
            nestFilterString.entrySet().forEach(nest ->{
                NestedQueryBuilder nestedQueryBuilder =
                        QueryBuilders.nestedQuery(nest.getKey(), QueryBuilders.queryStringQuery(nest.getValue()));
                booleanQueryBuilder.must(nestedQueryBuilder);
            });
        }
        //排序
        if (sort !=null && !sort.isEmpty()){
            sort(builder ,sort);
        }
        GlobalBuilder agg = getAgg(indexAgg);
        if (agg !=null){
            builder.addAggregation(agg);
        }
        builder.setPostFilter(booleanQueryBuilder);
        SearchResponse response = builder.get();
        return response;
    }



    private static GlobalBuilder getAgg(IndexAgg indexAgg){
        if (indexAgg == null){
            return null;
        }
        GlobalBuilder global = AggregationBuilders.global("global");


        Set<String> fields = indexAgg.getAggregation();
//        Map<String, IndexAgg.RangeAgg<Number>[]> range = indexAgg.getRangeAgg();
        if (!fields.isEmpty()){
            for (String f : fields) {
                global.subAggregation(AggregationBuilders.terms(f).field(f));
            }
        }
//        if (!range.isEmpty()){
//            for (Map.Entry<String, IndexAgg.RangeAgg<Number>[]> entry : range.entrySet()) {
//                String key = entry.getKey();
//                IndexAgg.RangeAgg<Number>[] value = entry.getValue();
//                RangeBuilder rangeBuilder = AggregationBuilders.range(key).field(key);
//                for (IndexAgg.RangeAgg<Number> agg : value) {
//                    Number start = agg.getStart();
//                    Number end = agg.getEnd();
//                    if (start == null){
//                        rangeBuilder.addUnboundedTo(end.doubleValue());
//                    }else if (end == null){
//                        rangeBuilder.addUnboundedFrom(start.doubleValue());
//                    }else {
//                        rangeBuilder.addRange(start.doubleValue(),end.doubleValue());
//                    }
//                }
//                global.subAggregation(rangeBuilder);
//            }
//        }

        return global;
    }



    //排序
    private static void sort(SearchRequestBuilder builder , Map<String, SortOrder> sort){
        sort.entrySet().forEach(s ->{
            builder.addSort(s.getKey(),s.getValue());
        });
    }

    /**
     * 把Map参数转换为ES查询的字符串
     * @param params
     * @return
     */
    private static String queryStr(Map<String,Object> params){
        StringBuilder qs = new StringBuilder();
        params.entrySet().forEach(m ->{
            Object o = m.getValue();
            String key = m.getKey();
            if (StrKit.isBlank(o)) return;
            if(o.getClass().isArray()){
                Object[] values =(Object[])o;
                if (values.length == 0) return;
                qs.append("( ");
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) qs.append(" OR ");
                    qs.append(key + ":" + values[i]);
                }
                qs.append(")");
            } else {
                if (qs.length() != 0){
                    qs.append(" AND ");
                }
                qs.append(key+":"+o);
            }
        });
        return qs.toString();
    }

    /**
     * 根据条件分页查询
     * @param clazz
     * @param params
     * @param sort
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     * @throws UnknownHostException
     */
    public static <T extends BaseEntity> SearchResponse query(Class<T> clazz,
                                                              Map<String,Object> params,
                                                              Map<String, SortOrder> sort,
                                                              int pageIndex,int pageSize) throws UnknownHostException {
        return query(clazz,params,sort,null,null,null,null,pageIndex,pageSize);
    }




}
