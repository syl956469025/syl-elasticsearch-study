package syl.study.elasticsearch;

import org.junit.Test;
import syl.study.elasticsearch.Util.Mapper;
import syl.study.elasticsearch.model.IndexMapping;
import syl.study.utils.FastJsonUtil;

/**
 * Created by Mtime on 2016/10/17.
 */
public class ElasticMappingDemo extends BaseElasticSearchTest {



    @Test
    public void putMapping(){
        Mapper.EntityInfo info = Mapper.getEntityInfo(IndexMapping.class);
        String map = FastJsonUtil.bean2Json(info.getMappings());
        System.out.println(map);
        client.admin().indices().prepareCreate(info.getIndex().getIndexName())
                .addMapping(info.getIndex().getIndexType(), map).get();



    }



}
