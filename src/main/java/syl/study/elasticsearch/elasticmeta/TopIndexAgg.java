package syl.study.elasticsearch.elasticmeta;

import syl.study.elasticsearch.enums.AggType;

/**
 * Created by Mtime on 2016/11/8.
 */
public class TopIndexAgg extends IndexAgg {;


    public TopIndexAgg(){
        super(AggType.TOP.name(),AggType.TOP.name(),null);
    }



}
