package person.liuxx.read.controller;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.dto.BookDTO;
import person.liuxx.read.exception.BookLoadFailedException;
import person.liuxx.read.service.BookService;
import person.liuxx.util.base.StringUtil;
import person.liuxx.util.service.exception.SearchException;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月31日 下午2:24:49
 * @since 1.0.0
 */
@RestController
@RequestMapping("/book")
@Api(value = "书籍对象控制器")
public class BookController
{
    private Logger log = LogManager.getLogger();
    @Autowired
    private BookService bookService;

    @ApiOperation(value = "获取书籍信息", notes = "根据书籍名称来获取获取书籍信息")
    @ApiImplicitParam(name = "name", value = "书籍名称", required = true, dataType = "String")
    @GetMapping("/info")
    public BookDO name(@RequestParam(value = "name", defaultValue = "CC") String name)
    {
        return bookService.getBook(name).<SearchException> orElseThrow(() ->
        {
            throw new SearchException("书籍查询失败，书籍名称：" + name);
        });
    }

    @ApiOperation(value = "获取书籍目录列表信息", notes = "根据id来获取获取书籍目录列表信息")
    @ApiImplicitParam(name = "bookId", value = "书籍id", required = true, dataType = "Long")
    @RequestMapping(value = "/titles/{bookId}", method = RequestMethod.GET)
    public List<String> titleList(@PathVariable Long bookId)
    {
        return bookService.listBookTitle(bookId).<SearchException> orElseThrow(() ->
        {
            throw new SearchException("书籍查询失败，书籍id：" + bookId);
        });
    }

    @ApiOperation(value = "请求服务器添加服务器磁盘存储的书籍", notes = "解析传来的BookDTO信息，增加新的book")
    @ApiImplicitParams(
    { @ApiImplicitParam(name = "book", value = "书籍信息实体BookDTO", required = true,
            dataType = "BookDTO") })
    @PostMapping("/path")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDO load(@RequestBody BookDTO book)
    {
        log.info("请求加载书籍：{}", book);
        if (Objects.isNull(book) || StringUtil.isAnyEmpty(book.getName(), book.getPath()))
        {
            throw new IllegalArgumentException("请求参数中，书籍名称和书籍路径都不可以为空！");
        }
        return bookService.parseAndSave(book).<BookLoadFailedException> orElseThrow(() ->
        {
            throw new BookLoadFailedException("加载书籍失败，书籍信息：" + book);
        });
    }
}
