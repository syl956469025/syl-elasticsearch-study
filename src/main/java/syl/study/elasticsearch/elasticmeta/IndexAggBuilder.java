package syl.study.elasticsearch.elasticmeta;

/**
 * Created by Mtime on 2016/11/8.
 */
public class IndexAggBuilder {

    public static TermsIndexAgg terms(String field) {
        return new TermsIndexAgg(field);
    }

    public static TopIndexAgg top() {
        return new TopIndexAgg();
    }


}
