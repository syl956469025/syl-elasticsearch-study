package syl.study.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.junit.Test;

/**
 * Created by Mtime on 2016/10/20.
 */
public class ElasticSearchStudy extends BaseElasticSearchTest {


    @Test
    public void addIndex(){
        CreateIndexResponse response = client.admin()
                .indices().prepareCreate("complex").get();
        json(response);
    }

    @Test
    public void addDocument(){

    }

}
