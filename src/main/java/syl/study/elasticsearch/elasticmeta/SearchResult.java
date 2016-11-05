package syl.study.elasticsearch.elasticmeta;

import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/26.
 */
public class SearchResult<T> {

    private Integer searchCount;

    private List<T> searchList;

    private Map<String,Map<Object,Long>> aggResult;


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
