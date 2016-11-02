package syl.study.elasticsearch.elasticmeta;

/**
 * Created by Mtime on 2016/10/18.
 */
public class ElasticIndex {


    public ElasticIndex(){}

    String IndexName;

    String IndexType;

    public String getIndexName() {
        return IndexName;
    }

    public void setIndexName(String indexName) {
        IndexName = indexName;
    }

    public String getIndexType() {
        return IndexType;
    }

    public void setIndexType(String indexType) {
        IndexType = indexType;
    }
}
