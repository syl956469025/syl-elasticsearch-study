package syl.study.spring.elasticsearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import syl.study.spring.entiry.Author;
import syl.study.spring.entiry.Book;

import java.util.UUID;

/**
 * Booktest
 *
 * @author 史彦磊
 * @create 2017-08-10 17:47.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/simple-repository-test.xml")
public class BookTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void createIndex(){
        Book book = new Book();
        book.setName("zhangsan");
        book.setId(UUID.randomUUID().toString());
        this.elasticsearchTemplate.createIndex(Book.class);
        this.elasticsearchTemplate.putMapping(Book.class);
        this.bookRepository.save(book);
    }



}
