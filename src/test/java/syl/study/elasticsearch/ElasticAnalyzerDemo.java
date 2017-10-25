package syl.study.elasticsearch;

import org.junit.Test;

/**
 * 分词
 *
 * @author 史彦磊
 * @create 2017-02-20 14:44.
 */
public class ElasticAnalyzerDemo extends BaseElasticSearchTest{



    @Test
    public void analyzerTest(){
        String str = "18370667310 E123456789 abcd http:www.baidu.com 2017-02-01 01:01:01";
        String[] split = str.split(" ");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }


}
