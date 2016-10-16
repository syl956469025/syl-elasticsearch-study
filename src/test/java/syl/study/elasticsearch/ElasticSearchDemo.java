package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import syl.study.utils.FastJsonUtil;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticSearchDemo extends BaseElasticSearchTest {



    @Test
    public void searchTest(){
        SearchResponse response = client.prepareSearch("complex")
                .setTypes("child")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setPostFilter(termQuery())
                .setQuery(booleanQuery())
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        writeSearchResponse(response);
    }


    @Test
    public void searchNestedTest(){
        SearchResponse response = client.prepareSearch("complex")
                .setTypes("child")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(nestedQuery())
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        System.out.println(FastJsonUtil.bean2Json(response));
        writeSearchResponse(response);
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
                .should(QueryBuilders.termQuery("username","张无忌"));
    }


    public QueryBuilder termQuery(){
        return QueryBuilders.termQuery("username","张无忌");
    }


    public QueryBuilder fuzzyQuery() {
        return QueryBuilders.fuzzyQuery("username", "张无忌");
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
        return QueryBuilders.queryStringQuery("+kimchy -elasticsearch");
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
        return QueryBuilders.wildcardQuery("name", "zhang*");
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
                .must(QueryBuilders.matchQuery("member.name", "张无忌"))
                .must(QueryBuilders.matchQuery("member.age", 12));


        return QueryBuilders.nestedQuery("member",boolQuery);
    }


}
