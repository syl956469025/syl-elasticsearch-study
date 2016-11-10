package syl.study.elasticsearch;

import org.junit.Test;
import syl.study.elasticsearch.client.ESSearchUtil;
import syl.study.elasticsearch.client.ESWriteUtil;
import syl.study.elasticsearch.elasticmeta.SearchResult;
import syl.study.elasticsearch.model.Kq;
import syl.study.elasticsearch.model.User;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/11/4.
 */
public class NestedDemo extends BaseElasticSearchTest {



    @Test
    public void testDemo() throws UnknownHostException {
        User user = new User();
        user.setId(3);
        List<Kq> kqs = new ArrayList<>();
        Kq k = new Kq();
        k.setId("a");
        k.setStatus(1);
        kqs.add(k);

        Kq k1 = new Kq();
        k1.setId("b");
        k1.setStatus(2);
        kqs.add(k1);

        user.setKequns(kqs);
        user.setKequn(new String[]{"dd","cc"});
        ESWriteUtil.addIndex(user);
    }


    @Test
    public void search() throws UnknownHostException {
        String nestString = "-kequns.id:b";
        Map<String,String> nest = new HashMap<>();
        nest.put("kequns",nestString);

        Map<String,Map<String,Object>> map = new HashMap<>();
        Map<String,Object> m = new HashMap<>();
        m.put("kequns.id","a");
        m.put("kequns.id","b");
        map.put("kequns",m);

//        String query = "kequn:aa AND -kequn:cc";

        SearchResult<User> result = ESSearchUtil.query(User.class, null, null, null, null, nest, null,null, 1, 4);
        List<User> users = result.getSearchList();
        if (users != null && !users.isEmpty()){
            for (User u : users) {
                System.out.println(u.getId());
            }
        }else{
            System.out.println("空结果");
        }


    }

}
