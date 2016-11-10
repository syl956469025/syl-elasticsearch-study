package syl.study.elasticsearch.elasticmeta;

import syl.study.elasticsearch.model.IndexAggGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/26.
 */
public class SearchResult<T> {

    private Integer searchCount;

    private List<T> searchList;

    private Map<String,Map<Object,Long>> aggResult;

    private Map<String,Map<IndexAggGroup.Func,Object>> aggGroup;

//    private Map<String,Map<IndexAggGroup.RangeAgg<Number>,Object>> aggRangeGroup;
//
//    private Map<String,Map<IndexAggGroup.RangeAgg<Temporal>,Object>> aggdateGroup;

//
//    public Map<String, Map<IndexAggGroup.RangeAgg<Number>, Object>> getAggRangeGroup() {
//        return aggRangeGroup;
//    }
//
//    public void setAggRangeGroup(Map<String, Map<IndexAggGroup.RangeAgg<Number>, Object>> aggRangeGroup) {
//        this.aggRangeGroup = aggRangeGroup;
//    }
//
//    public Map<String, Map<IndexAggGroup.RangeAgg<Temporal>, Object>> getAggdateGroup() {
//        return aggdateGroup;
//    }
//
//    public void setAggdateGroup(Map<String, Map<IndexAggGroup.RangeAgg<Temporal>, Object>> aggdateGroup) {
//        this.aggdateGroup = aggdateGroup;
//    }

    public Map<String, Map<IndexAggGroup.Func, Object>> getAggGroup() {
        return aggGroup;
    }

    public void setAggGroup(Map<String, Map<IndexAggGroup.Func, Object>> aggGroup) {
        this.aggGroup = aggGroup;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }

    public List<T> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<T> searchList) {
        this.searchList = searchList;
    }

    public Map<String, Map<Object, Long>> getAggResult() {
        return aggResult;
    }

    public void setAggResult(Map<String, Map<Object, Long>> aggResult) {
        this.aggResult = aggResult;
    }
}
