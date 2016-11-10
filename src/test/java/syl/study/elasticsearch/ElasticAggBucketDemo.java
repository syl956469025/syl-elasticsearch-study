package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.global.GlobalBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.missing.MissingBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNested;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.junit.Test;
import syl.study.elasticsearch.client.ESSearchUtil;
import syl.study.elasticsearch.elasticmeta.IndexAgg;
import syl.study.elasticsearch.elasticmeta.IndexAggBuilder;
import syl.study.elasticsearch.elasticmeta.SearchResult;
import syl.study.elasticsearch.model.Member;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Mtime on 2016/11/3.
 */
public class ElasticAggBucketDemo extends BaseElasticSearchTest {


    /**
     * 把查询结果放到一个bucket中来统计 总数
     * global aggregation 只能放在最外层，如果把它当做其他聚合的子聚合，那是没有意义的
     */
    @Test
    public void globalAgg(){
        GlobalBuilder global = AggregationBuilders.global("global").subAggregation(
                AggregationBuilders.terms("year").field("year").subAggregation(
                        AggregationBuilders.terms("name").field("nameEN").subAggregation(
                                AggregationBuilders.topHits("top").setSize(Integer.MAX_VALUE)
                        )
                )
        );
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setQuery(QueryBuilders.queryStringQuery(""))
                .addAggregation(global)
                .get();
//        writeSearchResponse(response);
        Global agg = response.getAggregations().get("global");
        System.out.println(agg.getDocCount());
        Terms year = agg.getAggregations().get("year");
        List<Terms.Bucket> buckets = year.getBuckets();
        buckets.forEach(b ->{
            Object key = b.getKey();
            long docCount = b.getDocCount();
//            System.out.println("key: "+key +"  count: "+docCount);
            Terms name = b.getAggregations().get("name");
            List<Terms.Bucket> bb = name.getBuckets();
            for (Terms.Bucket bucket : bb) {
                Object k = bucket.getKey();
                long count = bucket.getDocCount();
                System.out.println("key1 : " + key +"  key2: "+ k  + " count: "+ count);
                TopHits top = bucket.getAggregations().get("top");
                SearchHit[] hits = top.getHits().getHits();
                for (SearchHit hit : hits) {
                    System.out.println(hit.getSourceAsString());
                }
            }
        });
    }

    @Test
    public void testTerm(){


    }








    /**
     * 定义一个bucket 来统计
     */
    @Test
    public void filterAgg(){
        FilterAggregationBuilder filter = AggregationBuilders.filter("agg")
                .filter(QueryBuilders.termQuery("year", "2016"));
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .addAggregation(filter)
                .get();
        writeSearchResponse(response);
        Filter agg = response.getAggregations().get("agg");
        long count = agg.getDocCount();
        System.out.println(count);


    }

    /**
     * 定义多个bucket 从不同维度来统计
     */
    @Test
    public void filtersAgg(){
        FiltersAggregationBuilder filters = AggregationBuilders.filters("agg")
                .filter("2016", QueryBuilders.termQuery("year", "2016"))
                .filter("2015", QueryBuilders.termQuery("year", "2015"));
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .addAggregation(filters)
                .get();
        writeSearchResponse(response);
        Filters agg = response.getAggregations().get("agg");
        List<? extends Filters.Bucket> buckets = agg.getBuckets();
        buckets.forEach(b ->{
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
        });
    }


    /**
     * 计算 没有该field的或者 value值为NULL的  数量
     */
    @Test
    public void missingAgg(){
        MissingBuilder miss = AggregationBuilders.missing("miss").field("year");
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .addAggregation(miss)
                .get();
        writeSearchResponse(response);
        Missing agg = response.getAggregations().get("miss");
        System.out.println(agg.getDocCount());
    }


    /**
     * 统计所有的子类的个数
     */
    @Test
    public void nestedAgg(){
        NestedBuilder nest = AggregationBuilders.nested("agg").path("points");
        SearchResponse response = client.prepareSearch("member")
                .setTypes("member")
                .addAggregation(nest)
                .get();
        writeSearchResponse(response);
        Nested agg = response.getAggregations().get("agg");
        long count = agg.getDocCount();
        System.out.println(count);

    }


    /**
     * 1. 求 所有子类的个数
     * 2. 在所有子类中 计算level最小的值
     */
    @Test
    public void nestedAgg2(){
        NestedBuilder nest = AggregationBuilders.nested("agg").path("points");
        nest.subAggregation(
            AggregationBuilders.min("min").field("points.level")
        );
        SearchResponse response = client.prepareSearch("member")
                .setTypes("member")
//                .setQuery(QueryBuilders.queryStringQuery("id:1"))
                .addAggregation(nest)
                .get();
        writeSearchResponse(response);
        Nested agg = response.getAggregations().get("agg");
        System.out.println(agg.getDocCount());
        Min min = agg.getAggregations().get("min");
        System.out.println(min.getValue());
    }


    /**
     *
     */
    @Test
    public void reverseNestedAgg(){
        NestedBuilder nest = AggregationBuilders.nested("agg").path("points")
                .subAggregation(
                        AggregationBuilders.terms("term").field("points.level")
                                .subAggregation(
                                        AggregationBuilders.reverseNested("reverse")
                                )
                );
        SearchResponse response = client.prepareSearch("member")
                .setTypes("member")
                .addAggregation(nest)
                .get();
        writeSearchResponse(response);
        Nested agg = response.getAggregations().get("agg");
        System.out.println("总子count : "+agg.getDocCount());

        Terms term = agg.getAggregations().get("term");
        for (Terms.Bucket b : term.getBuckets()) {
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);

            ReverseNested reverse = b.getAggregations().get("reverse");
            long c = reverse.getDocCount();
            System.out.println("reverse count : "+c);
        }

    }





    /**
     * 根据某个字段进行分组 相当于solr中的facet
     */
    @Test
    public void termAgg(){
        TermsBuilder term = AggregationBuilders.terms("agg").field("areaId").size(100).shardSize(200);

        SearchResponse response = client.prepareSearch("ticketorderinfo")
                .setTypes("ticketorderinfo")
                .addAggregation(term)
                .setFrom(0)
                .setSize(10)
                .get();
        writeSearchResponse(response);
        Terms agg = response.getAggregations().get("agg");
        agg.getBuckets().forEach(b ->{
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
        });
    }

    /**
     * terms 默认有10个buckets
     * 可以通过order来根据每个bucket中文档的数量 来排序
     */
    @Test
    public void orderAgg(){
        TermsBuilder order = AggregationBuilders.terms("agg").field("year")
                .order(Terms.Order.term(false));
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .addAggregation(order)
                .get();
        writeSearchResponse(response);
        Terms agg = response.getAggregations().get("agg");
        agg.getBuckets().forEach(b ->{
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
        });

    }


    /**
     * 根据每个桶中 id的平均值得大小进行倒序排序
     */
    @Test
    public void orderSubAgg(){
        TermsBuilder order = AggregationBuilders.terms("agg").field("year")
                .order(Terms.Order.aggregation("avg", false))
                .subAggregation(AggregationBuilders.avg("avg").field("id"));
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .addAggregation(order)
                .get();
        writeSearchResponse(response);
        Terms agg = response.getAggregations().get("agg");
        for (Terms.Bucket b : agg.getBuckets()) {
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
            Avg avg = b.getAggregations().get("avg");
            System.out.println("avg : "+avg.getValue());
        }

    }


    /**
     * 范围聚合
     * 范围是 [ from TO to }  前 闭 后 开
     */
    @Test
    public void rangeAgg(){
        RangeBuilder range = AggregationBuilders.range("agg").field("id")
                .addRange(27812, 1112233);
        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .addAggregation(range)
                .setFrom(0)
                .setSize(122)
                .get();
        writeSearchResponse(response);
        Range agg = response.getAggregations().get("agg");

        for (Range.Bucket b : agg.getBuckets()) {
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
        }

    }


    /**
     * 日期范围的查询
     *
     * 范围是 [ from TO to }  前 闭 后 开
     */
    @Test
    public void dateRangeAgg(){
        DateRangeBuilder dateRange = AggregationBuilders.dateRange("agg").field("releaseDate")
                .addRange("2016-09-02", "2016-10-01");

        SearchResponse response = client.prepareSearch("smovie")
                .setTypes("smovie")
                .addAggregation(dateRange)
                .get();
        writeSearchResponse(response);
        Range agg = response.getAggregations().get("agg");

        for (Range.Bucket b : agg.getBuckets()) {
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
        }
    }


    @Test
    public void testData(){
        TermsBuilder term = AggregationBuilders.terms("agg").field("cinemaId").subAggregation(
                AggregationBuilders.topHits("top").setSize(1).setTrackScores(true)
        );
        SearchResponse response = client.prepareSearch("kequn")
                .setTypes("kequn")
                .setQuery(QueryBuilders.queryStringQuery("kid:2"))
                .addAggregation(term)
                .get();
        Terms agg = response.getAggregations().get("agg");
        for (Terms.Bucket b : agg.getBuckets()) {
            Object key = b.getKey();
            long count = b.getDocCount();
            System.out.println("key: "+key +"  count: "+count);
            TopHits top = b.getAggregations().get("top");
            for (SearchHit hit : top.getHits()) {
                System.out.println(FastJsonUtil.bean2Json(hit));
            }
        }
    }


    @Test
    public void testData1() throws UnknownHostException {
        IndexAgg indexAgg = IndexAggBuilder.terms("year")
                .subIndexAgg(IndexAggBuilder.terms("nameEN")
                        .subIndexAgg(IndexAggBuilder.top()));

        SearchResult<Member> result = ESSearchUtil.query(Member.class, null, null, null, null, null, indexAgg,null, 1, 30);
    }

    @Test
    public void testShortMsg(){

    }




}
