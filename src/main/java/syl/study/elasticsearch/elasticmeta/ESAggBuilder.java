package syl.study.elasticsearch.elasticmeta;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import syl.study.elasticsearch.enums.AggType;

import java.util.List;

/**
 * Created by Mtime on 2016/11/8.
 */
public class ESAggBuilder {


    /**
     * 获取聚合类型
     * @param type
     * @param name
     * @return
     */
    private static AggregationBuilder combineAggs(String type, String name ,String field , AggregationBuilder builder){
        if (builder == null){
            if (type.equals(AggType.GLOBAL.name())){
                builder = AggregationBuilders.global(name);
            }else{
                builder = AggregationBuilders.global(AggType.GLOBAL.name());
            }
        }else if (type.equals(AggType.TOP.name())){
            builder.subAggregation(AggregationBuilders.topHits(name));
        }else if (type.equals(AggType.TERMS.name())){
            builder.subAggregation(AggregationBuilders.terms(name).field(field));
        }else{
            throw new RuntimeException("不支持该类型的聚合");
        }
        return builder;
    }


    public static AggregationBuilder getAggs(IndexAgg agg,AggregationBuilder builder){
        AggregationBuilder combineBuilder = combineAggs(agg.getType(), agg.getName(), agg.getField(), builder);
        List<IndexAgg> aggregations = agg.getAggregations();
        if (aggregations == null || aggregations.isEmpty()){
            return combineBuilder;
        }else{
            for (IndexAgg aggregation : aggregations){
                combineBuilder = getAggs(aggregation, combineBuilder);
            }
        }
        return combineBuilder;
    }


}
