package syl.study.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import syl.study.elasticsearch.client.ESSearchUtil;
import syl.study.elasticsearch.client.ESWriteUtil;
import syl.study.elasticsearch.model.*;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 该实例是以elasticSearch官方的java客户端TransportClient 做的演示
 * 包括： 1.获取连接
 *        2.添加索引
 *        3.根据主键ID获取索引
 *        4.根据其他字段检索索引
 *        5.更新索引
 *        6.删除索引
 * Created by shiyanlei on 2016/10/12.
 * ES Version ： 2.4.0
 */
public class ElasticCURDDemo extends BaseElasticSearchTest {



    /**
     * 添加索引
     * @throws UnknownHostException
     */
    @Test
    public void addIndex() throws UnknownHostException {
        //插入数据到elasticsearch
        Member member = new Member();
        member.setAge(1);
        member.setBirthday(LocalDateTime.now());
        member.setName("张无忌");
        member.setId(2);
        member.setUserId(123459);
        member.setPrice(12.4);
        member.setPric(12.5f);
        List<Points> pointses = new ArrayList<>();
        for (int i=0;i<5;i++){
            Points p = new Points();
            p.setCards(new String[]{"1234","3456","234567"});
            p.setLevel(1+i+1);
            p.setPoint(2);
            p.setUserId(12356);
            pointses.add(p);
        }

        ArrTest arr = new ArrTest();
        arr.setPrefer(new String[]{"swim","eat"});
        arr.setZhangsan("iszhangsan");
        member.setArr(arr);
        member.setPoints(pointses);

        member.setPrefer(new String[]{"swim","eat","sport"});
        ESWriteUtil.addIndex(member);
    }


    /**
     * 根据其他字段检索索引
     * @throws UnknownHostException
     */
    @Test
    public void getIndex() throws UnknownHostException {
        TermQueryBuilder query = QueryBuilders.termQuery("id", "1");
        SearchResponse response = client.prepareSearch("member")
                .setTypes("member")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .execute().actionGet();
        System.out.println(response.toString());
        client.close();

    }

    /**
     * 根据主键ID获取索引
     * @throws UnknownHostException
     */
    @Test
    public void getIndexById() throws UnknownHostException {
        GetResponse response = client.prepareGet("customer", "custom", "1").get();

        System.out.println(FastJsonUtil.bean2Json(response));
        client.close();
    }

    /**
     * 更新索引
     * @throws UnknownHostException
     */
    @Test
    public void updateIndex() throws UnknownHostException {
        Member member = new Member();
        member.setAge(14);
        member.setBirthday(LocalDateTime.now());
//        member.setBir(LocalDate.now());
        member.setName("zhangsan");
        member.setId(11);
        member.setUserId(123456);
        member.setPrice(12.4);
//        member.setPric(12.6f);
        ESWriteUtil.updateIndex(member);

    }

    /**
     * 删除索引
     * @throws UnknownHostException
     */
    @Test
    public void deleteIndex() throws UnknownHostException {
        ESWriteUtil.deleteIndex(Member.class,"1");
    }




    public static void main(String[] args) {
        String s = String.format("%016d", 24242342l);
        System.out.println(s);
    }

    @Test
    public void addIndexList() throws UnknownHostException {
//        List<Member> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setId(2);
            member.setName("Quick brown foxes leap over lazy dogs in summer");
            member.setBirthday(LocalDateTime.now());
            member.setPrice(2.3);
//            member.setPric(2.5f);
            member.setUserId(123456);
//            list.add(member);
//        }
        ESWriteUtil.addIndex(member);
    }


    @Test
    public void updateIndexList() throws UnknownHostException {
        List<Member> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setId(i+1);
            member.setName("张无忌"+i);
            member.setBirthday(LocalDateTime.now());
            member.setPrice(2.4 + i);
            member.setBir(LocalDate.now());
//            member.setPric(2.7f);
            member.setUserId(123456+i);
            list.add(member);
        }
        ESWriteUtil.updateIndexList(list);
    }


    @Test
    public void deleteIndexList() throws UnknownHostException {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int id = i + 1;
            ids.add(""+id);
        }
        ESWriteUtil.deleteIndexList(Member.class,ids);
    }

    @Test
    public void queryById() throws InterruptedException, ExecutionException, UnknownHostException {
        Map<String, Object> m = ESSearchUtil.getById(Member.class, "33");
        System.out.println(FastJsonUtil.bean2Json(m));
    }

    @Test
    public void queryByParam() throws UnknownHostException {
        Map<String,Object> properties = new HashMap<>();
        properties.put("name","张无忌*");
        Map<String,SortOrder> sort = new HashMap<>();
        sort.put("id",SortOrder.DESC);
        Map<String,Map<String,Object>> nest = new HashMap<>();
        Map<String,Object> nests = new HashMap<>();
        nests.put("arr.zhangsan","iszhangsan");
        nest.put("arr",nests);

        String nestedString = "arr.age:[ 3 TO 5 ]";
        String nestedString1 = "arr.age:[ * TO 5 ]";
        String nestedString2 = "arr.age:[ 3 TO * ]";
        String nestedString4 = "arr.age:{ 3 TO 5 }";
        String nestedString5 = "arr.age:{ * TO 5 }";
        String nestedString6 = "arr.age:{ 3 TO * }";

//        String filter = "price:[ 2.3 TO 8.3 ]";



//        SearchResponse query = ESSearchUtil.query(Member.class, properties, sort, null,nest,null, 1, 10);
//        writeSearchResponse(query);
    }


    @Test
    public void addMember() throws UnknownHostException {
        MemberCoreTest member = new MemberCoreTest();
        member.setId(123456l);
        member.setList(Arrays.asList(new Integer[]{2,3,4,5}));
        ESWriteUtil.addIndex(member);
    }




    @Test
    public void add() throws UnknownHostException {
        Kequn k = new Kequn();
        k.setCinemaId("6");
        k.setKid("2");
        k.setUserId("3");
        k.setId(UUID.randomUUID().toString());
        ESWriteUtil.addIndex(k);
    }


    @Test
    public void delete(){
        Kequn k = new Kequn();
        k.setCinemaId("cinemaId");
        k.setKid("kid");
        k.setUserId("userId");
        k.setId("id");
        IndexResponse res = client.prepareIndex("kequn", "kequn", "1c353f62-a045-4248-9407-4fd0ff982des")
                .setSource(FastJsonUtil.bean2Json(k))
                .setOpType(IndexRequest.OpType.CREATE)
                .setVersionType(VersionType.EXTERNAL)
                .setVersion(new Date().getTime())
                .get();
        System.out.println(FastJsonUtil.bean2Json(res));
    }

    @Test
    public void testVersion(){
        GetResponse response = client.prepareGet("kequn", "kequn", "1c353f62-a045-4248-9407-4fd0ff982des").get();
        System.out.println(FastJsonUtil.bean2Json(response));

//        UpdateResponse res = client.prepareUpdate("kequn", "kequn", "1c353f62-a045-4248-9407-4fd0ff982des")
//                .setDoc(response.getSourceAsString())
//                .setVersion(1478256494307l)
//                .get();

    }


    @Test
    public void mget(){
        MultiGetRequest request = new MultiGetRequest();
        MultiGetRequest.Item item = new MultiGetRequest.Item("website","blog","2");
        MultiGetRequest.Item item2 = new MultiGetRequest.Item("website","blog","1");
        request.add(item);
        request.add(item2);
        MultiGetResponse response = client.multiGet(request).actionGet();
        MultiGetItemResponse[] responses = response.getResponses();
        for (MultiGetItemResponse res : responses) {
            GetResponse rs = res.getResponse();
            System.out.println(FastJsonUtil.bean2Json(rs));
            String resource = rs.getSourceAsString();
            System.out.println(resource);
        }
    }
}
