package syl.study.elasticsearch.elasticmeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Mtime on 2016/11/8.
 */
public abstract class IndexAgg {

    List<IndexAgg> aggregations;

    String name;

    String type;

    String field;



    protected IndexAgg(String name, String type, String field) {
        this.name = name;
        this.type = type;
        this.field = field;
    }

    public IndexAgg subIndexAgg(IndexAgg agg){
        if (aggregations == null){
            aggregations = new ArrayList<>();
        }
        aggregations.add(agg);
        return this;
    }

    public boolean isLeaf(){
        if (aggregations == null) {
            return true;
        } else {
            if (aggregations.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void action(Consumer<IndexAgg> action){
        for (IndexAgg aggregation : aggregations) {
            action.accept(aggregation);
        }
    }



    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IndexAgg> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<IndexAgg> aggregations) {
        this.aggregations = aggregations;
    }
}
