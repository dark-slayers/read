package person.liuxx.read.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.liuxx.read.domain.BookDO;
import person.liuxx.read.service.BookService;
import person.liuxx.util.file.FileUtil;
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
    private Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test/db")
    public List<String> greeting()
    {
        log.info("test db !");
        List<String> result = new ArrayList<>();
        List<BookDO> bookList = bookService.listBooks();
        for (BookDO b : bookList)
        {
            String info = b.getName();
            if (!FileUtil.existsFile(Paths.get(b.getPath())))
            {
                info = info + ":文件不存在!";
                result.add(info);
            }
        }
        return result;
    }
}
