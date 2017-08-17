package person.liuxx.read.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.liuxx.read.book.StorageBook;
import person.liuxx.read.service.impl.BookServiceImpl;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月27日 下午6:28:24
 * @since 1.0.0
 */
@ApiIgnore
@RestController
public class TestController
{
    @Autowired
    BookServiceImpl bookService;

    @RequestMapping("/test")
    public List<String> greeting()
    {
        return save();
    }

    List<String> save()
    {
        return new ArrayList<>();
    }

    List<String> bookTest(StorageBook book)
    {
        List<String> result = new ArrayList<>();
        int index = 1;
        result.add(book.getTitles().get(index));
        result.add(book.getStories().get(index));
        index = 2;
        result.add(book.getTitles().get(index));
        result.add(book.getStories().get(index));
        return result;
    }
}
