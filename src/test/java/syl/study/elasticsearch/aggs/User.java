package syl.study.elasticsearch.aggs;

import java.time.LocalDateTime;

/**
 * @author 史彦磊
 * @create 2017-05-17 18:19.
 */
public class User {

    private String zhangsan;

    private String name = "";

    private LocalDateTime birthday;


    public String getZhangsan() {
        return zhangsan;
    }

    public void setZhangsan(String zhangsan) {
        System.out.println("diaoyongle set fangfa ");
        this.zhangsan = zhangsan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("diaoyong name set fangfa ");
        this.name = name;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }
}
