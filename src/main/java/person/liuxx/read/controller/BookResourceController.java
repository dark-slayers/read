package person.liuxx.read.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import person.liuxx.read.book.StorageBook;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.service.BookService;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月14日 下午4:50:49
 * @since 1.0.0
 */
@Controller
@RequestMapping("/book")
@Api(value = "书籍资源对象控制器")
public class BookResourceController
{
    private Logger log = LogManager.getLogger();
    @Autowired
    private BookService bookService;

    @ApiOperation(value = "下载书籍", notes = "根据id来下载txt书籍")
    @ApiImplicitParam(name = "id", value = "书籍id", required = true, dataType = "Long")
    @RequestMapping(value = "/txt/{id}", method = RequestMethod.GET)
    public List<String> txt(@PathVariable Long id)
    {
        log.info("下载id为{}的书籍...", id);
        Optional<BookDO> optional = bookService.findUseId(id);
        Optional<StorageBook> bookOption = bookService.read(optional.orElse(null));
        List<String> list = bookOption.map(b -> b.getTitles()).orElse(new ArrayList<>());
        return list;
    }
}
