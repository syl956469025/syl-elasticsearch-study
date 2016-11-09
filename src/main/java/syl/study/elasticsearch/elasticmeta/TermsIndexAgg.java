package syl.study.elasticsearch.elasticmeta;

import syl.study.elasticsearch.enums.AggType;

/**
 * Created by Mtime on 2016/11/8.
 */
public class TermsIndexAgg extends IndexAgg {


    public TermsIndexAgg(String field){
        super(field,AggType.TERMS.name(),field);
    }



}
