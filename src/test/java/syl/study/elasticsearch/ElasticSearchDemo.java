package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.junit.Test;
import syl.study.elasticsearch.client.ESSearchUtil;
import syl.study.elasticsearch.elasticmeta.SearchResult;
import syl.study.elasticsearch.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticSearchDemo extends BaseElasticSearchTest {

    private final static String index = "smovie";
    private final static String type = "smovie";



    @Test
    public void searchTest(){
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setPostFilter(rangeQuery())
                .setQuery(booleanQuery())
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        writeSearchResponse(response);
    }


    @Test
    public void searchNestedTest(){
        SearchResponse response = client.prepareSearch("user")
                .setTypes("user")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(nestedQuery())
                .setFrom(0)
                .setSize(10)
                .get();
//        System.out.println(FastJsonUtil.bean2Json(response));
//        writeSearchResponse(response);
        SearchResult<User> result = ESSearchUtil.getResult(User.class, response, null);
        List<User> res = result.getSearchList();
        if (res !=null && !res.isEmpty()){
            for (User user : res) {
                System.out.println(user.getId());
            }
        }else{
            System.out.println("为空");
        }
    }

    /**
     * nestedQuery
     *
     * 用于对象嵌套查询，
     *
     * @return
     */
    public QueryBuilder nestedQuery(){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
//                .must(QueryBuilders.matchQuery("kequns.id", "a"))
            .mustNot(QueryBuilders.matchQuery("kequns.id", "1"));

        return QueryBuilders.nestedQuery("kequns",boolQuery);
    }

    /**
     * matchQuery 单个匹配 模糊匹配
     * @return
     */
    public QueryBuilder matchQuery(){
        return QueryBuilders.matchQuery("name","张无忌");
    }

    /**
     * 匹配多个字段  模糊匹配
     * @return
     */
    public QueryBuilder multiMatchQuery(){
        return QueryBuilders.multiMatchQuery("张无忌","username","nickname");
    }


    public QueryBuilder booleanQuery(){
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("nameCN","铁拳"))
                .filter(QueryBuilders.termQuery("year","2015"));
    }


    public QueryBuilder termQuery(){
        return QueryBuilders.termQuery("nameCN","铁拳");
    }

    public QueryBuilder termsQuery(){
        return QueryBuilders.termsQuery("nameCN","铁拳","湖底魔兽");
    }


    public QueryBuilder fuzzyQuery() {
        return QueryBuilders.fuzzyQuery("name", "张无忌");
    }


    /**
     * matchall query
     * 查询匹配所有文件。
     *
     * @return QueryBuilder
     */
    public QueryBuilder matchAllQuery() {
        return QueryBuilders.matchAllQuery();
    }


    /**
     * prefix query
     * 包含与查询相匹配的文档指定的前缀。
     *
     * @return QueryBuilder
     */
    public QueryBuilder prefixQuery() {
        return QueryBuilders.prefixQuery("nickname", "张无");
    }


    /**
     * TODO NotSolved
     * p
     * querystring query
     * 　　查询解析查询字符串,并运行它。有两种模式,这种经营。
     * 第一,当没有添加字段(使用{ @link QueryStringQueryBuilder #字段(String)},将运行查询一次,非字段前缀
     * 　　将使用{ @link QueryStringQueryBuilder # defaultField(字符串)}。
     * 第二,当一个或多个字段
     * 　　(使用{ @link QueryStringQueryBuilder #字段(字符串)}),将运行提供的解析查询字段,并结合
     * 　　他们使用DisMax或者一个普通的布尔查询(参见{ @link QueryStringQueryBuilder # useDisMax(布尔)})。
     *
     * @return QueryBuilder
     */
    public QueryBuilder queryString() {
        return QueryBuilders.queryStringQuery("name:张无忌9 OR name:张无忌6");
    }

    /**
     * wildcard query
     * 　　实现了通配符搜索查询。支持通配符*
     * 　　匹配任何字符序列(包括空), ? ,
     * 　　匹配任何单个的字符。注意该查询可以缓慢,因为它
     * 　　许多方面需要遍历。为了防止WildcardQueries极其缓慢。
     * 　　一个通配符词不应该从一个通配符*或?。
     *
     * @return QueryBuilder
     */
    public QueryBuilder wildcardQuery() {
        return QueryBuilders.wildcardQuery("name", "张无忌*");
    }



    public QueryBuilder rangeQuery(){
        return QueryBuilders.rangeQuery("id").from(200000).to(500000);
    }


    /**
     * 指定要返回的字段
     */
    @Test
    public void fetchSourceDemo(){
        SearchResponse response = client.prepareSearch("user")
                .setTypes("user")
                .setQuery(QueryBuilders.queryStringQuery("id:2"))
                .setFetchSource("id", "kequn")
                .get();
        writeSearchResponse(response);
    }


    @Test
    public void testNestQuery(){
        SearchResponse response = client.prepareSearch("esinfo")
                .setTypes("esinfo")
                .setQuery(nestedQuerys())
                .get();
        writeSearchResponse(response);
    }

    private QueryBuilder nestedQuerys(){
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        bool.must(QueryBuilders.termQuery("id",45));
        bool.must(QueryBuilders.nestedQuery("scope",QueryBuilders.matchQuery("scope.id",171)));
        return bool;
    }

    @Test
    public void testSearch(){
        Map<String,Object> param = new HashMap<>();
//        param.put("prefers",new String[]{"ctx._source.reexchangeCinemaCode"});
        param.put("prefers",123);
        SearchResponse response = client.prepareSearch("voucherandsellorderines")
                .setTypes("voucherandsellorderines")

                .setPostFilter(QueryBuilders.queryStringQuery("voucherState:5"))
                .setQuery(QueryBuilders.scriptQuery(
                        new Script("doc['cinemaInnerCode'].value == doc['reexchangeCinemaCode'].value", ScriptService.ScriptType.INLINE,"groovy",null)))
                .get();
        writeSearchResponse(response);
    }



}
