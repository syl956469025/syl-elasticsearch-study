package syl.study.elasticsearch;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.Test;
import syl.study.elasticsearch.model.Member;
import syl.study.utils.FastJsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticParentChildDemo extends BaseElasticSearchTest {


    @Test
    public void addComplexObj() throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<Member> memberList = new ArrayList<>();
        for(int i=0;i<5;i++){
            Member member = new Member();
//            member.setAge(21+i);
            member.setBirthday(LocalDateTime.now());
            member.setName("张无忌"+i);
            member.setUserId(123456+i);
//            member.setPrefer(new String[]{"swim","eat","run"});
            memberList.add(member);
        }
        map.put("memberlist",memberList);
        map.put("title","Nest eggs");
        map.put("body","Making your money work...");
        map.put("tags",new String[]{"cash", "shares"});






        IndexRequestBuilder request = client.prepareIndex("complex", "child", "5");
        IndexResponse response = request.setSource(FastJsonUtil.bean2Json(map)).get();
        System.out.println(FastJsonUtil.bean2Json(response));
    }

}
