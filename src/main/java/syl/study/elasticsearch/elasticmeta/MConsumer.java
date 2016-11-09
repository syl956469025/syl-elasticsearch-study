package syl.study.elasticsearch.elasticmeta;

/**
 * Created by Mtime on 2016/11/9.
 */
@FunctionalInterface
public interface MConsumer<R,T,U,K,A> {



    R accept(T t, U u,K k,A a);


}
