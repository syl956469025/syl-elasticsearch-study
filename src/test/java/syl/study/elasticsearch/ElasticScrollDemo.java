package syl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.junit.Test;

/**
 * Created by Mtime on 2016/11/7.
 */
public class ElasticScrollDemo extends BaseElasticSearchTest {


    @Test
    public void testScroll(){
        SearchResponse response =  client.prepareSearch("sgovregion")
                .setTypes("sgovregion")
                .setScroll(new TimeValue(600000))
                .setSize(100)
                .get();

        String oldid = response.getScrollId();
        String newId="";
        SearchResponse res;
        do {
                res = client.prepareSearchScroll(oldid).setScroll(new TimeValue(600000)).get();
            newId = res.getScrollId();
            if (oldid.equals(newId)){
                System.out.println("两次的id是一样的");
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("两次的id是不一样的");
            }

        }while (res.getHits().getHits().length!=0);


    }

    @Test
    public void testSlicesScroll(){
        SearchResponse response =  client.prepareSearch("sgovregion")
                .setTypes("sgovregion")
                .setScroll(new TimeValue(600000))
                .setSize(100)
                .get();

        String oldid = response.getScrollId();
        String newId="";
        SearchResponse res;
        do {
            res = client.prepareSearchScroll(oldid).setScroll(new TimeValue(600000)).get();
            newId = res.getScrollId();
            if (oldid.equals(newId)){
                System.out.println("两次的id是一样的");
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("两次的id是不一样的");
            }

        }while (res.getHits().getHits().length!=0);


    }



}
