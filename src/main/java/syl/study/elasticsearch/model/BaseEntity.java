package syl.study.elasticsearch.model;

/**
 * Created by Mtime on 2016/10/17.
 */



public abstract class BaseEntity<PK> {


    PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}

