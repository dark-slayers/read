package person.liuxx.read.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.StorageBook;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.exception.BookSaveFailedException;
import person.liuxx.read.service.impl.BookServiceImpl;
import person.liuxx.util.log.LogUtil;
import person.liuxx.util.service.reponse.ErrorResponse;

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
    private BookServiceImpl bookService;

    @ApiOperation(value = "获取书籍信息", notes = "根据书籍名称来获取获取书籍信息")
    @ApiImplicitParam(name = "name", value = "书籍名称", required = true, dataType = "String")
    @GetMapping("/info")
    public BookDO name(@RequestParam(value = "name", defaultValue = "CC") String name)
    {
        log.info("使用书籍名称《{}》查询对应的书籍", name);
        BookDO bookDO = bookService.findUseName(name).orElse(new BookDO());
        log.info("服务器响应：{}", bookDO);
        return bookDO;
    }

    @ApiOperation(value = "获取书籍目录列表信息", notes = "根据id来获取获取书籍目录列表信息")
    @ApiImplicitParam(name = "bookId", value = "书籍id", required = true, dataType = "Long")
    @RequestMapping(value = "/titles/{bookId}", method = RequestMethod.GET)
    public List<String> titleList(@PathVariable Long bookId)
    {
        Optional<BookDO> optional = bookService.findUseId(bookId);
        Optional<StorageBook> bookOption = bookService.read(optional.orElse(null));
        List<String> list = bookOption.map(b -> b.getTitles()).orElse(new ArrayList<>());
        return list;
    }

    @ApiOperation(value = "请求服务器添加服务器磁盘存储的书籍", notes = "解析传来的BookDO信息，使用path信息和name信息增加新的book")
    @ApiImplicitParams(
    { @ApiImplicitParam(name = "book", value = "书籍信息实体BookDO", required = true,
            dataType = "BookDO") })
    @PostMapping("path")
    public BookDO load(@RequestBody BookDO book)
    {
        log.info("需要添加的book信息：{}", book);
        StorageBook b = BookFactory.parseDir(Paths.get(book.getPath()), book.getName());
        try
        {
            return bookService.save(b);
        } catch (IOException e)
        {
            throw new BookSaveFailedException("书籍保存失败：" + book, e);
        }
    }

    @ApiOperation(value = "获取章节的信息", notes = "根据书籍id和章节索引编号index获取书籍的指定章节内容")
    @ApiImplicitParams(
    { @ApiImplicitParam(name = "bookId", value = "书籍id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "chapterIndex", value = "章节索引编号", required = true,
                    dataType = "int") })
    @GetMapping("/chapter/{bookId}/{chapterIndex}")
    public Chapter chapter(@PathVariable Long bookId, @PathVariable int chapterIndex)
    {
        Optional<BookDO> optional = bookService.findUseId(bookId);
        Optional<StorageBook> bookOption = bookService.read(optional.orElse(null));
        Chapter chapter = bookOption.map(b -> b.getChapter(bookId, chapterIndex)).orElse(
                new Chapter());
        return chapter;
    }

    @ApiOperation(value = "删除章节", notes = "从指定id书籍中，删除索引编号为index章节")
    @ApiImplicitParams(
    { @ApiImplicitParam(name = "bookId", value = "书籍id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "chapterIndex", value = "章节索引编号", required = true,
                    dataType = "int") })
    @DeleteMapping("/chapter/{bookId}/{chapterIndex}")
    public Chapter deleteChapter(@PathVariable Long bookId, @PathVariable int chapterIndex)
    {
        log.info("请求删除id为{}的书籍中，索引为{}的章节", bookId, chapterIndex);
        return bookService.deleteChapter(bookId, chapterIndex);
    }

    @ApiOperation(value = "更新章节", notes = "根据书籍id和章节索引编号index获取书籍的指定章节内容")
    @ApiImplicitParam(name = "chapter", value = "章节信息", required = true, dataType = "Chapter")
    @PutMapping("/chapter")
    public Chapter updateChapter(@RequestBody Chapter chapter)
    {
        Long bookId = chapter.getBookId();
        Optional<BookDO> optional = bookService.findUseId(bookId);
        Optional<StorageBook> bookOption = bookService.read(optional.orElse(null));
        return chapter;
    }

    @ExceptionHandler(BookSaveFailedException.class)
    public ErrorResponse exceptionHandler(BookSaveFailedException e)
    {
        log.error(LogUtil.errorInfo(e));
        ErrorResponse resp = new ErrorResponse(500, 50001, "书籍保存失败", "失败信息：" + LogUtil.errorInfo(e),
                "more info");
        return resp;
    }
}
