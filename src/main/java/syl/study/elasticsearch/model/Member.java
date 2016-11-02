package syl.study.elasticsearch.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Mtime on 2016/10/12.
 */
public class Member extends BaseEntity<Integer> {


    private long userId;

    private String name;

    private int age;

    private LocalDateTime birthday;

    private Double price;

    private float pric;

    private LocalDate bir;

    List<Points> points;

    List<Integer> list;


    ArrTest arr;

    public ArrTest getArr() {
        return arr;
    }

    public void setArr(ArrTest arr) {
        this.arr = arr;
    }

    public List<Points> getPoints() {
        return points;
    }

    public void setPoints(List<Points> points) {
        this.points = points;
    }

    public LocalDate getBir() {
        return bir;
    }

    public void setBir(LocalDate bir) {
        this.bir = bir;
    }

    public float getPric() {
        return pric;
    }

    public void setPric(float pric) {
        this.pric = pric;
    }

        private String[] prefer;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String[] getPrefer() {
        return prefer;
    }

    public void setPrefer(String[] prefer) {
        this.prefer = prefer;
    }
}
