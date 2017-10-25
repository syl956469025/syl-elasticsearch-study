package syl.study.elasticsearch;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.junit.Test;
import syl.study.elasticsearch.aggs.MemberIndex;
import syl.study.elasticsearch.aggs.Utils;
import syl.study.elasticsearch.client.TClient;
import syl.study.elasticsearch.model.ActivityPointRecord;
import syl.study.elasticsearch.model.TicketPoint;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticUpdateDemo extends BaseElasticSearchTest{



    @Test
    public void testCPU() throws InterruptedException {
        /*while (true){
        try {*/
        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        for(int j = 0;j<100000000;j++){
            threadPool.execute(new Task(j+""));
        }

        Thread.sleep(100000000000L);


        /*} catch (Exception e) {
            e.printStackTrace();
        }
        }*/
    }

    class Task extends Thread{
        String threadName;


        public Task(String threadName){
            this.threadName = threadName;
        }


        public void run(){
            List<MemberIndex> list = new ArrayList<>();
            BulkRequestBuilder builder = client.prepareBulk();
            IndexRequestBuilder indexBuilder1 = client.prepareIndex("memberindex", "memberindex");
            for (int i = 0; i < 4000; i++) {
                MemberIndex member = new MemberIndex();
                member.setId(i+"_"+threadName+"1");
                member.setBirthday(LocalDate.now());
                member.setMobile("18310667310");
                member.setUpdateTime(LocalDateTime.now());
                member.setIsSyncToOld(1);
                member.setId(UUID.randomUUID().toString());
                member.setMobile("sdfsdfsdf");
                member.setAddress1("zhangsaosidazhanglai");
                member.setAddress2("sdfsdfsdf");
                member.setAddress3("sdfsdfsdfs");
                member.setAddress4("sdfsdfsdfsfs");
                member.setArrivalType(1);
                member.setZipCode("sdfsdf");
                member.setCardCount(10);
                member.setUserName("张三李四王五赵六");
                member.setTcPassword(UUID.randomUUID().toString());
                member.setRegistCinemaId("000");
                member.setRegistType(3);
                member.setWeibo("weibo");
                member.setRecruitEmployeeNo("123");
                member.setRecruitEmployeeName("zhangwi0");
                member.setQq("5664466665");
                member.setPoints(99666);
                member.setOperatorName("zhangslaisfoekluoi");
                member.setPhone("123123123");
                member.setOldMemberId(987456L);
                member.setOftenCode("213234");
                member.setOccupation(5);
                member.setStatus(5);
                member.setMemberNo(UUID.randomUUID().toString());
                member.setCards(new String[]{UUID.randomUUID().toString(),UUID.randomUUID().toString()});
                list.add(member);
            }


        }
    }





    /**
     * 通过 groovy 脚本更新索引
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updateByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("customer").type("custom").id("2");
        Map<String, Object> param = new HashMap<>();
        param.put("newName", "zhangsan");
        request.script(new Script(ScriptType.INLINE,"ctx._source.name=newName","", param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 通过groovy 脚本更新索引的数组类型的字段
     * 往数组中添加字段
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updateListFieldByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("productor").type("product").id("1");
        Map<String, Object> param = new HashMap<>();
        param.put("anotherName", "zhangsan");
//        request.script(new Script("ctx._source.name += anotherName", ScriptService.ScriptType.INLINE,"groovy",param));
        request.script(new Script(ScriptType.INLINE,"ctx._source.name -= anotherName", "groovy",param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));   
    }


    @Test
    public void updateListObjectByScript() throws ExecutionException, InterruptedException, UnknownHostException {
        ActivityPointRecord record = new ActivityPointRecord("C110","001",LocalDate.now());
        TicketPoint ticketPoint = new TicketPoint();
        ticketPoint.setPoint(10);
        ticketPoint.setTicketNo("001_3");
        record.getTicketPoints().add(ticketPoint);
        TransportClient client = TClient.getClient();
        Map<String, Object> param = new HashMap<>();
        param.put("ticketPoints", FastJsonUtil.bean2Map(ticketPoint));
        IndexRequest index = new IndexRequest("activitypointrecord","activitypointrecord",record.getId());
        index.source(Utils.toJson(record));
        UpdateRequest request = new UpdateRequest("activitypointrecord","activitypointrecord",record.getId());
        request.script(new Script(ScriptType.INLINE,"ctx._source.ticketPoints += ticketPoints", "groovy", param))
                .upsert(index)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));

    }


    /**
     * 通过脚本添加新字段
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void addNewFieldByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("productor").type("product").id("4");
        Map<String,Object> param = new HashMap<>();
//        param.put("prefers",new String[]{"swim","eat","run","sing"});
        request.script(new Script(ScriptType.INLINE,"ctx._source.prefer=prefers","groovy",param));
        param.put("username","张无忌4");
        param.put("nickname","张无忌4");
        request.script(new Script(ScriptType.INLINE,"ctx._source.username=username","groovy",param));
        request.script(new Script(ScriptType.INLINE,"ctx._source.nickname=nickname","groovy",param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 使用groovy 脚本删除一个字段
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void addRemoveFieldByScript() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index("productor").type("product").id("1");
        Map<String,Object> param = new HashMap<>();
        param.put("removeField","prefer");
        request.script(new Script(ScriptType.INLINE,"ctx._source.remove(removeField)", "groovy",param));
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }


    /**
     * 使用groovy 脚本 upsert 索引
     * 必须在集群每个节点的elasticsearch.yml配置中添加配置：
     * script.engine.groovy.inline.update: true
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updsertByScript() throws ExecutionException, InterruptedException {
        IndexRequest index = new IndexRequest("productor","product","2");
        index.source("id","2","name","zhaoliu","age",54);
        UpdateRequest update = new UpdateRequest("productor","product","2");
        update.doc("name","zhaoqi").upsert(index);
        UpdateResponse response = client.update(update).get();
        System.out.println(FastJsonUtil.bean2Json(response));

    }


    /**
     * 使用doc 更新索引
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void updateByDoc() throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest("productor","product","2");
        request.doc("birthday", LocalDateTime.now());
        UpdateResponse response = client.update(request).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }



}
