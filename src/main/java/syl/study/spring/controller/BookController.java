package syl.study.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import syl.study.spring.elasticsearch.BookRepository;
import syl.study.spring.entiry.Book;

import java.util.UUID;

/**
 * Book控制层
 *
 * @author 史彦磊
 * @create 2017-08-10 19:23.
 */

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @RequestMapping("/createBook")
    public String createBook(){
        Book book = new Book();
        book.setName("zhangsan");
        book.setId(UUID.randomUUID().toString());
        this.elasticsearchTemplate.createIndex(Book.class);
        this.elasticsearchTemplate.putMapping(Book.class);
        this.bookRepository.save(book);
        return "success";
    }
}
