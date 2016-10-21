package syl.study.elasticsearch.model;

import java.util.List;

/**
 * Created by Mtime on 2016/10/19.
 */
public class MemberCoreTest extends BaseEntity<Long> {

    String name;

    List<Integer> list;

    List<Points> lists;

    public List<Points> getLists() {
        return lists;
    }

    public void setLists(List<Points> lists) {
        this.lists = lists;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
