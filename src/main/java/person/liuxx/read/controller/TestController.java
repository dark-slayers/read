package person.liuxx.read.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.StorageBook;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.service.BookService;
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
    private BookService bookService;

    @RequestMapping("/test")
    public List<String> greeting()
    {
        return save();
    }

    List<String> load()
    {
        Optional<BookDO> optional = bookService.findUseName("张三丰异界游");
        Optional<StorageBook> bookOption = bookService.read(optional.orElse(null));
        List<String> list = bookOption.map(b -> bookTest(b)).orElse(new ArrayList<>());
        return list;
    }

    List<String> save()
    {
        StorageBook book = BookFactory.parseDir(Paths.get("F:\\Book\\000007\\000007"), "张三丰异界游");
        bookService.save(book);
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
