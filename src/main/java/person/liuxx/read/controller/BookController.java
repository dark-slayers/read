package person.liuxx.read.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import person.liuxx.read.book.StorageBook;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.service.BookService;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月31日 下午2:24:49
 * @since 1.0.0
 */
@RestController
@RequestMapping("/book")
public class BookController
{
    @Autowired
    private BookService bookService;

    @ApiOperation(value = "获取书籍目录列表信息", notes = "根据id来获取获取书籍目录列表信息")
    @ApiImplicitParam(name = "id", value = "书籍id", required = true, dataType = "Long")
    @RequestMapping(value = "/titles/{id}", method = RequestMethod.GET)
    public List<String> titleList(@PathVariable Long id)
    {
        Optional<BookDO> optional = bookService.findUseId(id);
        Optional<StorageBook> bookOption = bookService.read(optional.orElse(null));
        List<String> list = bookOption.map(b -> b.getTitles()).orElse(new ArrayList<>());
        return list;
    }
}
