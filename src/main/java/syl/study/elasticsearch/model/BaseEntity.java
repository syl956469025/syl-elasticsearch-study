package syl.study.elasticsearch.model;

import syl.study.elasticsearch.annotation.ESId;

/**
 * Created by Mtime on 2016/10/17.
 */


public class BaseEntity<PK> {

    /**
     * 索引的主键字段
     */
    @ESId
    PK id;


    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
