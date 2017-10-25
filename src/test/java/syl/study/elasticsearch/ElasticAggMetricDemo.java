package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import syl.study.utils.FastJsonUtil;

/**
 * Created by Mtime on 2016/11/2.
 */
public class ElasticAggMetricDemo extends BaseElasticSearchTest {


    /**
     * 在查询的结果里取最小值
     *
     * 取最小值
     */
    @Test
    public void minAgg(){
        MinAggregationBuilder min =
                AggregationBuilders
                        .min("agg")
                        .field("id");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.queryStringQuery("-id:27812"))
                .addAggregation(min).get();
        Min agg = response.getAggregations().get("agg");
        double value = agg.getValue();
        System.out.println(value);
        writeSearchResponse(response);
    }


    /**
     * 查询符合条件的最大值
     */
    @Test
    public void maxAgg(){
        MaxAggregationBuilder max = AggregationBuilders.max("agg").field("id");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.queryStringQuery("-id:1112233"))
                .addAggregation(max)
                .setFrom(4)
                .setSize(10)
                .get();
        writeSearchResponse(response);
        Max agg = response.getAggregations().get("agg");
        System.out.println();
        System.out.println(agg.getValue());

    }

    /**
     * 查询符合条件的 再求和
     */
    @Test
    public void sumAgg(){
        SumAggregationBuilder sum = AggregationBuilders.sum("agg").field("id");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.queryStringQuery("id:(215120 OR 228345)"))
                .addAggregation(sum)
                .get();
        writeSearchResponse(response);

        Sum agg = response.getAggregations().get("agg");
        System.out.println(agg.getValue());
    }


    /**
     * 查询符合条件的 求平均值
     */
    @Test
    public void avgAgg(){
        AvgAggregationBuilder avg = AggregationBuilders.avg("agg").field("id");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
//                .setQuery(QueryBuilders.queryStringQuery(""))
                .addAggregation(avg)
                .get();
        writeSearchResponse(response);
        Avg agg = response.getAggregations().get("agg");
        System.out.println(agg.getValue());

    }


    /**
     * 查询符合条件的的数量
     */
    @Test
    public void countAgg(){
        ValueCountAggregationBuilder count = AggregationBuilders.count("agg").field("id");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.queryStringQuery("-id:221773"))
                .addAggregation(count)
                .get();
        writeSearchResponse(response);
        ValueCount agg = response.getAggregations().get("agg");
        System.out.println(agg.getValue());
        System.out.println(FastJsonUtil.bean2Json(agg));
    }

    /**
     * 查询符合条件的的数量
     */
    @Test
    public void countMemberAgg(){
        ValueCountAggregationBuilder count = AggregationBuilders.count("agg").field("mobile");
        SearchResponse response = client.prepareSearch("membercore")
                .setTypes("membercore")
//                .setSearchType(SearchType.QUERY_AND_FETCH)
//                .setQuery(QueryBuilders.queryStringQuery("-id:221773"))
                .addAggregation(count)
                .get();
        writeSearchResponse(response);
        ValueCount agg = response.getAggregations().get("agg");
        System.out.println(agg.getValue());
        System.out.println(FastJsonUtil.bean2Json(agg));
    }


    /**
     * 以百分比统计
     * 默认  1, 5, 25, 50, 75, 95, 99
     * 意思是：统计 1%的数据落在了某个值得的范围内，也就是1%的值小于某个值 ，依次类推
     */
    @Test
    public void percentAgg(){
        PercentilesAggregationBuilder percent = AggregationBuilders.percentiles("agg").field("price");
        SearchResponse response = client.prepareSearch("cars")
                .setTypes("transactions")
                .setSearchType(SearchType.QUERY_AND_FETCH)
//                .setQuery(QueryBuilders.queryStringQuery(""))
                .addAggregation(percent)
                .setSize(0)
                .get();
        writeSearchResponse(response);

        Percentiles agg = response.getAggregations().get("agg");
        for (Percentile per : agg) {
            double p = per.getPercent();
            double value = per.getValue();
            System.out.println("percent : "+ p);
            System.out.println("value : "+ value);
        }
    }


    /**
     * 指定值，来计算落在该值内的数据所占百分比
     */
    @Test
    public void percentRankAgg(){
//        PercentileRanksAggregationBuilder rank =
//                AggregationBuilders.percentileRanks("agg").field("id")
//                        .percentiles(221773, 1112233);
//        SearchResponse response = client.prepareSearch("smovie")
//                .setTypes("smovie")
//                .setSearchType(SearchType.QUERY_AND_FETCH)
//                .addAggregation(rank)
//                .get();
//        writeSearchResponse(response);
//        PercentileRanks agg = response.getAggregations().get("agg");
//        for (Percentile per : agg) {
//            double p = per.getPercent();
//            double value = per.getValue();
//            System.out.println("percent : "+ p);
//            System.out.println("value : "+ value);
//        }
    }


    /**
     * 根据某个字段去重后统计数量
     */
    @Test
    public void cardinalityAgg(){
        CardinalityAggregationBuilder cardinality = AggregationBuilders.cardinality("agg").field("year");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
//                .setQuery(QueryBuilders.queryStringQuery(""))
                .addAggregation(cardinality)
                .get();
        writeSearchResponse(response);
        Cardinality agg = response.getAggregations().get("agg");
        long value = agg.getValue();
        System.out.println(value);
        System.out.println(FastJsonUtil.bean2Json(agg));


    }


    /**
     * 获取前几个值
     */
    @Test
    public void topHitAgg(){
//        TermsBuilder term = AggregationBuilders.terms("agg").field("year")
//                .subAggregation(
        TopHitsAggregationBuilder tophit = AggregationBuilders.topHits("top")
                .sort(SortBuilders.fieldSort("year").order(SortOrder.DESC));
//                );
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .addAggregation(tophit)
                .get();
//        writeSearchResponse(response);
        TopHits top = response.getAggregations().get("top");
        SearchHits hits = top.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

//        Terms agg = response.getAggregations().get("agg");
//        List<Terms.Bucket> buckets = agg.getBuckets();
//        for (Terms.Bucket bucket:buckets) {
//            Object key = bucket.getKey();
//            long count = bucket.getDocCount();
//            System.out.println("key: " + key + "  count: " + count);
//
//            TopHits top = bucket.getAggregations().get("top");
//            SearchHits hits = top.getHits();
//            for (SearchHit hit : hits) {
//                System.out.println(hit.getSourceAsString());
//            }
//        }

    }




    /**
     * 求符合条件的统计值
     * 包括 求和 ，最大，最小，平均，数量
     *
     */
    @Test
    public void StatsAgg(){
        StatsAggregationBuilder stats = AggregationBuilders.stats("agg").field("id");

        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
//                .setQuery(QueryBuilders.queryStringQuery(""))
                .addAggregation(stats)
                .get();
        writeSearchResponse(response);

        Stats agg = response.getAggregations().get("agg");
        System.out.println(" 平均值 : "+agg.getAvg());
        System.out.println(" 最大值 : "+agg.getMax());
        System.out.println(" 最小值 : "+agg.getMin());
        System.out.println(" 求和值 : "+agg.getSum());
        System.out.println(" 数量 : "+agg.getCount());

    }








}
