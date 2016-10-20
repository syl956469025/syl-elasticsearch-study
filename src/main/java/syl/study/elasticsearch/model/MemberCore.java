package syl.study.elasticsearch.model;

/**
 * Created by Mtime on 2016/10/19.
 */
public class MemberCore extends BaseEntity<Long> {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
