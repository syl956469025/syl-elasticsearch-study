package syl.study.elasticsearch.eshttp;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import syl.study.elasticsearch.model.Member;
import syl.study.elasticsearch.model.Points;
import syl.study.utils.FastJsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增删改查 实例
 *
 * @author 史彦磊
 * @create 2017-10-27 15:55.
 */
public class ESHttpCURDDemo {


    /**
     * 测试 同步/异步 添加Doc
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testAddDoc() throws IOException, InterruptedException {
        IndexRequest request = new IndexRequest("member","member","3");
        Member member = new Member();
        member.setId(3);
        member.setAge(3);
        member.setBirthday(LocalDateTime.now());
        member.setPrice(2.6);
        member.setUserId(123L);
        member.setName("httpzhangsan");
        List<Points> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Points point = new Points();
            point.setUserId(i);
            point.setId(123L);
            point.setPoint(i);
            list.add(point);
        }
        member.setPoints(list);
        request.source(FastJsonUtil.bean2Json(member), XContentType.JSON);
        request.opType(DocWriteRequest.OpType.CREATE);
//        ESRestClient.getClient().indexAsync(request, new ActionListener<IndexResponse>() {
//            @Override
//            public void onResponse(IndexResponse res) {
//                System.out.println("添加成功");
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                System.out.println("添加失败");
//                throw new RuntimeException(e);
//            }
//        });
        IndexResponse index = ESRestClient.getClient().index(request);
        System.out.println(FastJsonUtil.bean2Json(index));
//        Thread.sleep(3000);
    }


    /**
     * 测试根据id获取文档
     */
    @Test
    public void getDoc() throws IOException, InterruptedException {
        GetRequest request = new GetRequest("member","member","1");
        request.fetchSourceContext(new FetchSourceContext(false));
        GetResponse res = ESRestClient.getClient().get(request);
//        String json = res.getSourceAsString();
//        System.out.println(json);
//        BytesReference ref = res.getSourceAsBytesRef();
//        System.out.println(FastJsonUtil.bean2Json(ref));
        System.out.println(FastJsonUtil.bean2Json(res));

        ESRestClient.getClient().getAsync(request, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse res) {
                System.out.println(FastJsonUtil.bean2Json(res));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        Thread.sleep(3000);
    }


    /**
     * 根据id删除文档
     */
    @Test
    public void deleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("member","member","3");
        DeleteResponse delete = ESRestClient.getClient().delete(request);
        System.out.println(FastJsonUtil.bean2Json(delete));
    }


    /**
     * 通过painless脚本 根据id 更新文档
     */
    @Test
    public void updateDocByScript() throws IOException {
        UpdateRequest request = new UpdateRequest("member","member","7");
        Map<String, Object> prams = new HashMap<>();
        prams.put("count", 4);
        request.scriptedUpsert(true);
        request.script(new Script(ScriptType.INLINE, "painless", "ctx._source.age += params.count", prams));
        UpdateResponse update = ESRestClient.getClient().update(request);
        System.out.println(FastJsonUtil.bean2Json(update));
    }


    @Test
    public void updateDocByData() throws IOException {
        UpdateRequest request = new UpdateRequest("member","member","2");
        Member member = new Member();
        member.setId(3);
        member.setAge(6);
        request.doc(FastJsonUtil.bean2Json(member),XContentType.JSON);
        ESRestClient.getClient().update(request);
    }

    @Test
    public void upertDoc() throws IOException {
        Member member = new Member();
        member.setId(4);
        member.setAge(3);
        member.setBirthday(LocalDateTime.now());
        member.setPrice(2.6);
        member.setUserId(123L);
//        member.setName("upsert");
        List<Points> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Points point = new Points();
            point.setUserId(i);
            point.setId(123L);
            point.setPoint(i);
            list.add(point);
        }
        member.setPoints(list);
        String s = FastJsonUtil.bean2Json(member);
        UpdateRequest request = new UpdateRequest("member","member","9");
//        request.upsert(s,XContentType.JSON);
        request.doc(s,XContentType.JSON);
        request.docAsUpsert(true);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        request.retryOnConflict(3);
//        request.fetchSource(true);
        //检查是否需要更新索引，如果设置detect_noop为false，那么不管这个字段是否发生了改变，都会重新索引，
        request.detectNoop(true);
        UpdateResponse update = ESRestClient.getClient().update(request);
        System.out.println(FastJsonUtil.bean2Json(update));
    }

    @Test
    public void testBulkAddDoc() throws IOException {
        Member member = new Member();
        member.setId(4);
        member.setAge(3);
        member.setBirthday(LocalDateTime.now());
        member.setPrice(2.6);
        member.setUserId(124L);
        String s = FastJsonUtil.bean2Json(member);
        BulkRequest request = new BulkRequest();
        IndexRequest index = new IndexRequest("member", "member", "13");
        index.source(s,XContentType.JSON);
//        IndexRequest index1 = new IndexRequest("member", "member", "14");
//        index1.source(s,XContentType.JSON);
//        IndexRequest index2 = new IndexRequest("member", "member", "12");
//        index2.source(s,XContentType.JSON);
        request.add(index);
//        request.add(index1);
//        request.add(index2);
        BulkResponse res = ESRestClient.getClient().bulk(request);
        System.out.println(FastJsonUtil.bean2Json(res));
    }

    @Test
    public void testBulkUpdateDoc() throws IOException {
        Member member = new Member();
        member.setId(90);
        member.setAge(3);
        member.setBirthday(LocalDateTime.now());
        member.setPrice(2.6);
        member.setUserId(123L);
        String s = FastJsonUtil.bean2Json(member);
        BulkRequest request = new BulkRequest();
        UpdateRequest index = new UpdateRequest("member", "member", "13");
        index.doc(s,XContentType.JSON);
        UpdateRequest index1 = new UpdateRequest("member", "member", "14");
        index1.doc(s,XContentType.JSON);
        UpdateRequest index2 = new UpdateRequest("member", "member", "12");
        index2.doc(s,XContentType.JSON);
        request.add(index);
        request.add(index1);
        request.add(index2);
        BulkResponse res = ESRestClient.getClient().bulk(request);
        for (BulkItemResponse item : res.getItems()) {
            System.out.println(FastJsonUtil.bean2Json(item));
        }
    }


    @Test
    public void testBulkDelDoc() throws IOException {
        Member member = new Member();
        member.setId(90);
        member.setAge(3);
        member.setBirthday(LocalDateTime.now());
        member.setPrice(2.6);
        member.setUserId(123L);
        String s = FastJsonUtil.bean2Json(member);
        BulkRequest request = new BulkRequest();
        DeleteRequest index = new DeleteRequest("member", "member", "13");
        DeleteRequest index1 = new DeleteRequest("member", "member", "14");
        DeleteRequest index2 = new DeleteRequest("member", "member", "12");
        request.add(index);
        request.add(index1);
        request.add(index2);
        BulkResponse res = ESRestClient.getClient().bulk(request);
        for (BulkItemResponse item : res.getItems()) {
            System.out.println(FastJsonUtil.bean2Json(item));
        }
    }




}
