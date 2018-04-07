package person.liuxx.read.controller;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.impl.StorageBook;
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
    public String greeting()
    {
        StorageBook book = BookFactory.parseDir(Paths.get("F:/Book/000006"), "123");
        return "Test Over !\n" + book.getTitles();
    }

}
