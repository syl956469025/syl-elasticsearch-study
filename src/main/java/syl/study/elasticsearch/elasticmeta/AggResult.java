package syl.study.elasticsearch.elasticmeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/11/9.
 */
public class AggResult<T> {

    private List<AggResult> results;


    private String field;

    private Map<Object,Long> count;

    private Map<String,List<T>> data;


    public AggResult(String field, Map<Object,Long> count) {
        this.field = field;
        this.count = count;
    }

    public AggResult subResult(AggResult result){
        if (results == null){
            results = new ArrayList<>();
        }
        results.add(result);
        return this;
    }

    public AggResult addData(String field,List<T> t){
        if (data == null){
            data = new HashMap<>();
        }
        data.put(field, t);
        return this;
    }


    public static AggResult aggResult(String field, Map<Object,Long> count){
        return new AggResult(field, count);
    }




    public List<AggResult> getResults() {
        return results;
    }

    public void setResults(List<AggResult> results) {
        this.results = results;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Map<Object, Long> getCount() {
        return count;
    }

    public void setCount(Map<Object, Long> count) {
        this.count = count;
    }

    public Map<String, List<T>> getData() {
        return data;
    }

    public void setData(Map<String, List<T>> data) {
        this.data = data;
    }
}
