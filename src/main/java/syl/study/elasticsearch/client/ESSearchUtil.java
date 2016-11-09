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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.SortOrder;
import syl.study.elasticsearch.Util.Mapper;
import syl.study.elasticsearch.Util.StrKit;
import syl.study.elasticsearch.elasticmeta.*;
import syl.study.elasticsearch.enums.AggType;
import syl.study.elasticsearch.model.BaseEntity;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static <T extends BaseEntity> SearchResult<T> query(Class<T> clazz,
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
        AggregationBuilder agg = getAgg(indexAgg);
        if (agg !=null){
            builder.addAggregation(agg);
        }
        builder.setPostFilter(booleanQueryBuilder);
        SearchResponse response = builder.get();
        return getResult(clazz,response,indexAgg);
    }



    public static AggregationBuilder getAgg(IndexAgg indexAgg){
        return ESAggBuilder.getAggs(indexAgg, null);
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
    public static <T extends BaseEntity> SearchResult<T> query(Class<T> clazz,
                                                              Map<String,Object> params,
                                                              Map<String, SortOrder> sort,
                                                              int pageIndex,int pageSize) throws UnknownHostException {
        return query(clazz,params,sort,null,null,null,null,pageIndex,pageSize);
    }


    /**
     * 获取结果
     * @param response
     * @return
     */
    public static <T extends BaseEntity> SearchResult<T> getResult(Class<T> clazz, SearchResponse response, IndexAgg indexAgg){
        SearchResult<T> result = new SearchResult<>();
        //设置查询总条数
        result.setSearchCount(Integer.valueOf(String.valueOf(response.getHits().getTotalHits())));
        //设置fqResult
        SearchHit[] hits = response.getHits().getHits();
        List<T> searchList= new ArrayList<>();
        for (SearchHit doc : hits) {
            T t = FastJsonUtil.json2Bean(doc.getSourceAsString(), clazz);
            searchList.add(t);
        }
        result.setSearchList(searchList);
        //设置aggResult
        if (indexAgg !=null){
            result.setAggResult(aggResult(response, indexAgg,clazz));
        }

        return result;
    }


    private static <T extends BaseEntity> Map<String,Map<Object,Long>> aggResult(SearchResponse response,IndexAgg indexAgg,Class<T> clazz){
        Aggregation agg = response.getAggregations().get(AggType.GLOBAL.name());
        AggResult<T> tAggResult = testDemo(indexAgg, clazz, agg);
        System.out.println(FastJsonUtil.bean2Json(tAggResult));
        return null;
    }

    private static <T extends BaseEntity> AggResult<T> testDemo(IndexAgg indexAgg, Class<T> clazz, Aggregation agg) {
        return getResult(indexAgg, agg, clazz, (result, aggs, type, index) -> {
            if (result == null&& aggs==null && StrKit.isBlank(type) && index == null){
                return null;
            }
            testDemo(indexAgg, clazz, agg);
            return result;
        });


    }


    private static <T extends BaseEntity> AggResult<T> getResult(IndexAgg indexAgg,
                                                                 Aggregation agg,
                                                                 Class<T> clazz,
                                                                 MConsumer<AggResult,AggResult,Aggregation,String,IndexAgg> action){
        String type = indexAgg.getType();
        String field = indexAgg.getField();
        if (type.equals(AggType.GLOBAL.name())){
            AggResult aggResult = AggResult.aggResult(null, null);
            if (indexAgg.isLeaf()){
                return aggResult;
            }
            Global global = (Global)agg;
            indexAgg.action(index ->{
                AggResult subAggResult = action.accept(aggResult, global, type, index);
                aggResult.subResult(subAggResult);
            });
            return aggResult;
        }else if (type.equals(AggType.TOP.name())){
            TopHits top = (TopHits)agg;
            SearchHit[] hits = top.getHits().getHits();
            List<T> list = new ArrayList<>();
            for (SearchHit hit : hits) {
                list.add(FastJsonUtil.json2Bean(hit.getSourceAsString(), clazz));
            }
            return AggResult.aggResult(field,null).addData(field,list);
        }else if (type.equals(AggType.TERMS.name())){
            Terms terms= (Terms)agg;
            List<Terms.Bucket> buckets = terms.getBuckets();
            Map<Object,Long> countMap = new HashMap<>();
            AggResult aggResult = AggResult.aggResult(null, null);
            for (Terms.Bucket bucket : buckets) {
                Object key = bucket.getKey();
                long count = bucket.getDocCount();
                countMap.put(key,count);
                if (!indexAgg.isLeaf()){
                    indexAgg.action(index ->{
                        Aggregation aggregation = bucket.getAggregations().get(index.getName());
                        AggResult subAggResult = action.accept(null, aggregation, index.getType(), index);
                        aggResult.subResult(subAggResult);
                    });
                }
            }
            aggResult.setField(field);
            aggResult.setCount(countMap);
            return aggResult;
        }else{
            throw new RuntimeException("不支持该类型的聚合");
        }
    }
}
