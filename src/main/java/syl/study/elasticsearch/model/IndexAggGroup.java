package syl.study.elasticsearch.model;

import syl.study.utils.StringUtils;

import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mtime on 2016/11/3.
 */
public class IndexAggGroup {


    /**
     * 聚合字段
     * 不包含范围聚合字段
     */
    private Set<String> faectField;

    /**
     * 聚合条件
     */
    private Map<String, String[]> aggQuery;

    /**
     * 范围聚合
     * Map<String,RangeAgg[]>
     *     key:field
     *     value: 范围是 [ from TO to }  前 闭 后 开
     */
    private Map<String,RangeAgg<Number>[]> numRangeAgg;

    /**
     * 日期范围聚合
     * Map<String,RangeAgg[]>
     *     key:field
     *     value: 范围是 [ from TO to }  前 闭 后 开
     */
    private Map<String,RangeAgg<Temporal>[]> dateRangeAgg;

    /**
     * 对某个字段 执行聚合函数
     * 包括 ：求和  求最大值   求最小值  求平均值  求数量
     */
    private Map<String,Func[]> groupAgg;


    public IndexAggGroup() {
        this.aggQuery = new HashMap<>();
        this.faectField = new HashSet<>();
        this.numRangeAgg = new HashMap<>();
        this.dateRangeAgg = new HashMap<>();
        this.groupAgg = new HashMap<>();
    }


    public void addAggField(String fieldName){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        this.faectField.add(fieldName);
    }

//    public void addAggQuery(String fieldName, String ... fieldValues){
//        if (StringUtils.isEmpty(fieldName)){
//            throw new RuntimeException("需要聚合的字段不可以为空");
//        }
//        if (StringUtils.isEmpty(fieldValues)){
//            throw new RuntimeException("需要聚合的字段值不可以为空");
//        }
//        this.aggQuery.put(fieldName, fieldValues);
//    }

    public void addFuncAgg(String fieldName , Func ... func){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        if (func == null){
            throw new RuntimeException("需要聚合的函数不可以为空");
        }
        for (Func f : func) {
            this.groupAgg.put(fieldName+f.name(),func);
        }

    }

    /**
     * 范围聚合
     * Map<String,RangeAgg[]>
     *     key:field
     *     value: 范围是 [ from TO to }  前 闭 后 开
     */
//    public void addRangeAgg(String fieldName , Number start , Number end,String key){
//        if (StringUtils.isEmpty(fieldName)){
//            throw new RuntimeException("需要聚合的字段不可以为空");
//        }
//        if (start == null && end == null){
//            throw new RuntimeException("数值范围 开始 和 结束 不可以同时为空");
//        }
//        this.numRangeAgg.put(fieldName,new RangeAgg[]{new RangeAgg(start,end,key)});
//    }

    /**
     * 日期范围聚合
     * Map<String,RangeAgg[]>
     *     key:field
     *     value: 范围是 [ from TO to }  前 闭 后 开
     */
//    public void addDateRangeAgg(String fieldName , Temporal start , Temporal end , String key){
//        if (StringUtils.isEmpty(fieldName)){
//            throw new RuntimeException("需要聚合的字段不可以为空");
//        }
//        if (start == null && end == null){
//            throw new RuntimeException("时间范围 开始 和 结束 不可以同时为空");
//        }
//        this.dateRangeAgg.put(fieldName,new RangeAgg[]{new RangeAgg(start,end , key)});
//    }








    public Set<String> getFaectField() {
        return faectField;
    }

    public void setFaectField(Set<String> faectField) {
        this.faectField = faectField;
    }

    public Map<String, String[]> getAggQuery() {
        return aggQuery;
    }


    public Map<String, RangeAgg<Number>[]> getNumRangeAgg() {
        return numRangeAgg;
    }


    public Map<String, RangeAgg<Temporal>[]> getDateRangeAgg() {
        return dateRangeAgg;
    }


    public Map<String, Func[]> getGroupAgg() {
        return groupAgg;
    }

    public void setGroupAgg(Map<String, Func[]> groupAgg) {
        this.groupAgg = groupAgg;
    }

    /**
     * 范围是 [ from TO to }  前 闭 后 开
     */
    public static class RangeAgg<T>{
        String key;

        T start;
        T end;


        public RangeAgg(T start, T end , String key) {
            this.start = start;
            this.end = end;
            this.key = key;
        }

        public T getStart() {
            return start;
        }

        public void setStart(T start) {
            this.start = start;
        }

        public T getEnd() {
            return end;
        }

        public void setEnd(T end) {
            this.end = end;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }


    /**
     * 聚合函数枚举
     */
    public enum Func{

        /**
         * 求最小值
         */
        MIN("min"),

        /**
         * 求最大值
         */
        MAX("max"),

        /**
         * 求平均值
         */
        AVG("avg"),

        /**
         * 求和
         */
        SUM("sum"),

        /**
         * 求数量
         */
        COUNT("count");

        private String name;

        Func(String name) {
            this.name = name;
        }
    }


}
