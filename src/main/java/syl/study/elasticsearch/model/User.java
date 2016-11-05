package syl.study.elasticsearch.model;

import java.util.List;

/**
 * Created by Mtime on 2016/11/4.
 */
public class User extends BaseEntity<Integer> {

    private List<Kq> kequns;

    private String[] kequn;

    public String[] getKequn() {
        return kequn;
    }

    public void setKequn(String[] kequn) {
        this.kequn = kequn;
    }

    public List<Kq> getKequns() {
        return kequns;
    }

    public void setKequns(List<Kq> kequns) {
        this.kequns = kequns;
    }
}
