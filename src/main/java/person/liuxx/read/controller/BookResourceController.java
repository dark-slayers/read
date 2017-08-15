package person.liuxx.read.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import person.liuxx.read.BookNotFoundException;
import person.liuxx.read.service.impl.BookServiceImpl;
import person.liuxx.util.service.reponse.ErrorResponse;

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
    private BookServiceImpl bookService;

    @ApiOperation(value = "下载书籍", notes = "根据id来下载txt书籍")
    @ApiImplicitParam(name = "id", value = "书籍id", required = true, dataType = "Long")
    @GetMapping("/txt/{id}")
    @ResponseBody
    public ResponseEntity<Resource> txt(@PathVariable Long id)
    {
        log.info("下载id为{}的书籍...", id);
        return bookService.createTxtFile(id);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ErrorResponse exceptionHandler()
    {
        ErrorResponse resp = new ErrorResponse(404, 40401, "访问的书籍资源不存在", "访问的书籍资源不存在", "more info");
        return resp;
    }
}
