package syl.study.elasticsearch;

import org.elasticsearch.action.update.UpdateRequest;
import org.junit.Test;

/**
 * Created by Mtime on 2016/10/13.
 */
public class ElasticUpdateByQueryDemo extends BaseElasticSearchTest {



    @Test
    public void updateByQuery(){
        UpdateRequest request = new UpdateRequest("productor","product","2");

    }



    @Test
    public void updateCinema(){
//        client.prepareBulk().

    }






}
