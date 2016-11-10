package syl.study.elasticsearch.enums;

/**
 * Created by Mtime on 2016/11/8.
 */
public enum AggType {
    GLOBAL("global"),

    TERMS("terms"),

    TOP("top"),

    RANGE("range"),

    DATERANGE("daterange"),

    FILTER("filter");


    private String name;


    AggType(String name) {
        this.name = name;
    }

}
