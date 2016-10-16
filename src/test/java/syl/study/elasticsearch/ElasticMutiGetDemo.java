package syl.study.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticMutiGetDemo extends BaseElasticSearchTest {


    /**
     * 获取多个index的结果
     */
    @Test
    public void mutiGetTest() {
        MultiGetResponse response = client.prepareMultiGet()
                .add("productor", "product", "1")
                .add("productor", "product", "2")
                .add("customer", "custom", "1").get();
        response.forEach(res ->{
            GetResponse re1 = res.getResponse();
            System.out.println(re1.getSourceAsString());
        });

    }


}
