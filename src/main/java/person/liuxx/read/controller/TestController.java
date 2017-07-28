package person.liuxx.read.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.StorageBook;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月27日 下午6:28:24
 * @since 1.0.0
 */
@RestController
public class TestController
{
    @RequestMapping("/test")
    public List<String> greeting()
    {
        List<String> result = new ArrayList<>();
        StorageBook book = BookFactory.parseLocalBook(Paths.get("F:\\Book\\000007\\000007"),
                "张三丰异界游");
        int index = 1;
        result.add(book.getTitles().get(index));
        result.add(book.getStories().get(index));
        index = 2;
        result.add(book.getTitles().get(index));
        result.add(book.getStories().get(index));
        index = 3;
        result.add(book.getTitles().get(index));
        result.add(book.getStories().get(index));
        return result;
    }
}
