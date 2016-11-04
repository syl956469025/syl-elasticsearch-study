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
public class IndexAgg {


    /**
     * 聚合字段
     * 不包含范围聚合字段
     */
    private Set<String> aggregation;

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
    private Map<String,RangeAgg<Number>[]> rangeAgg;

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
    private Map<String,Func[]> funcAgg;


    public IndexAgg() {
        this.aggQuery = new HashMap<>();
        this.aggregation = new HashSet<>();
        this.rangeAgg = new HashMap<>();
        this.dateRangeAgg = new HashMap<>();
        this.funcAgg = new HashMap<>();
    }


    public void addAggField(String fieldName){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        this.aggregation.add(fieldName);
    }

    public void addAggQuery(String fieldName, String ... fieldValues){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        if (StringUtils.isEmpty(fieldValues)){
            throw new RuntimeException("需要聚合的字段值不可以为空");
        }
        this.aggQuery.put(fieldName, fieldValues);
    }

    public void addFuncAgg(String fieldName , Func ... func){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        if (func == null){
            throw new RuntimeException("需要聚合的函数不可以为空");
        }
        this.funcAgg.put(fieldName,func);
    }

    /**
     * 范围聚合
     * Map<String,RangeAgg[]>
     *     key:field
     *     value: 范围是 [ from TO to }  前 闭 后 开
     */
    public void addRangeAgg(String fieldName , Number start , Number end){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        if (start == null && end == null){
            throw new RuntimeException("数值范围 开始 和 结束 不可以同时为空");
        }
        this.rangeAgg.put(fieldName,new RangeAgg[]{new RangeAgg(start,end)});
    }

    /**
     * 日期范围聚合
     * Map<String,RangeAgg[]>
     *     key:field
     *     value: 范围是 [ from TO to }  前 闭 后 开
     */
    public void addDateRangeAgg(String fieldName , Temporal start , Temporal end){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("需要聚合的字段不可以为空");
        }
        if (start == null && end == null){
            throw new RuntimeException("时间范围 开始 和 结束 不可以同时为空");
        }
        this.dateRangeAgg.put(fieldName,new RangeAgg[]{new RangeAgg(start,end)});
    }








    public Set<String> getAggregation() {
        return aggregation;
    }

    public void setAggregation(Set<String> aggregation) {
        this.aggregation = aggregation;
    }

    public Map<String, String[]> getAggQuery() {
        return aggQuery;
    }

    public void setAggQuery(Map<String, String[]> aggQuery) {
        this.aggQuery = aggQuery;
    }

    public Map<String, RangeAgg<Number>[]> getRangeAgg() {
        return rangeAgg;
    }

    public void setRangeAgg(Map<String, RangeAgg<Number>[]> rangeAgg) {
        this.rangeAgg = rangeAgg;
    }

    public Map<String, RangeAgg<Temporal>[]> getDateRangeAgg() {
        return dateRangeAgg;
    }

    public void setDateRangeAgg(Map<String, RangeAgg<Temporal>[]> dateRangeAgg) {
        this.dateRangeAgg = dateRangeAgg;
    }

    public Map<String, Func[]> getFuncAgg() {
        return funcAgg;
    }

    public void setFuncAgg(Map<String, Func[]> funcAgg) {
        this.funcAgg = funcAgg;
    }

    /**
     * 范围是 [ from TO to }  前 闭 后 开
     */
    public static class RangeAgg<T>{

        T start;
        T end;


        public RangeAgg(T start, T end) {
            this.start = start;
            this.end = end;
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
    }


    /**
     * 聚合函数枚举
     */
    public enum Func{

        /**
         * 求最小值
         */
        MIN,

        /**
         * 求最大值
         */
        MAX,

        /**
         * 求平均值
         */
        AVG,

        /**
         * 求和
         */
        SUM,

        /**
         * 求数量
         */
        COUNT;






    }


}
