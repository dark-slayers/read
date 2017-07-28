package person.liuxx.read.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.StorageBook;
import person.liuxx.read.service.BookService;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月27日 下午6:28:24
 * @since 1.0.0
 */
@RestController
public class TestController
{
    @Autowired
    BookService bookService;

    @RequestMapping("/test")
    public List<String> greeting()
    {
        return load();
    }

    List<String> load()
    {
        StorageBook book = bookService.read("张三丰异界游");
        return bookTest(book);
    }

    List<String> parser()
    {
        StorageBook book = BookFactory.parseDir(Paths.get("F:\\Book\\000007\\000007"), "张三丰异界游");
        return bookTest(book);
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
