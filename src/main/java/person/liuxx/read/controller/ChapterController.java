package person.liuxx.read.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import person.liuxx.read.dto.ChapterDTO;
import person.liuxx.read.service.ChapterService;
import person.liuxx.util.service.exception.RemoveException;
import person.liuxx.util.service.exception.SaveException;
import person.liuxx.util.service.exception.SearchException;
import person.liuxx.util.service.exception.UpdateException;
import person.liuxx.util.service.reponse.EmptySuccedResponse;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月7日 下午2:59:33
 * @since 1.0.0
 */
@RestController
@RequestMapping("/book")
@Api(value = "章节对象控制器")
public class ChapterController
{
    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "删除章节", notes = "从指定id书籍中，删除索引编号为index章节")
    @ApiImplicitParams(
    { @ApiImplicitParam(name = "bookId", value = "书籍id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "chapterIndex", value = "章节索引编号", required = true,
                    dataType = "int") })
    @DeleteMapping("/{bookId}/chapter/{chapterIndex}")
    public EmptySuccedResponse removeChapter(@PathVariable Long bookId,
            @PathVariable int chapterIndex)
    {
        return chapterService.remove(bookId, chapterIndex).orElseThrow(() ->
        {
            throw new RemoveException(errorMessage("删除章节失败", bookId, chapterIndex));
        });
    }

    @ApiOperation(value = "获取章节的信息", notes = "根据书籍id和章节索引编号index获取书籍的指定章节内容")
    @ApiImplicitParams(
    { @ApiImplicitParam(name = "bookId", value = "书籍id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "chapterIndex", value = "章节索引编号", required = true,
                    dataType = "int") })
    @GetMapping("/{bookId}/chapter/{chapterIndex}")
    public ChapterDTO chapter(@PathVariable Long bookId, @PathVariable int chapterIndex)
    {
        return chapterService.getChapter(bookId, chapterIndex).<SearchException> orElseThrow(() ->
        {
            throw new SearchException(errorMessage("书籍章节获取失败", bookId, chapterIndex));
        });
    }

    @ApiOperation(value = "添加章节", notes = "根据书籍id和章节索引编号index获取书籍的指定章节内容")
    @ApiImplicitParam(name = "chapter", value = "章节信息", required = true, dataType = "Chapter")
    @PostMapping("/chapter")
    public EmptySuccedResponse saveChapter(@RequestBody ChapterDTO chapter)
    {
        return chapterService.saveChapter(chapter).<SaveException> orElseThrow(() ->
        {
            throw new SaveException("书籍章节添加失败，章节信息：" + chapter.logInfo());
        });
    }

    @ApiOperation(value = "更新章节", notes = "根据书籍id和章节索引编号index更新书籍的指定章节内容")
    @ApiImplicitParam(name = "chapter", value = "章节信息", required = true, dataType = "Chapter")
    @PutMapping("/chapter")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmptySuccedResponse updateChapter(@RequestBody ChapterDTO chapter)
    {
        return chapterService.updateChapter(chapter).<UpdateException> orElseThrow(() ->
        {
            throw new UpdateException("书籍章节更新失败，章节信息：" + chapter.logInfo());
        });
    }

    private String errorMessage(String message, Long bookId, int chapterIndex)
    {
        return message + "，书籍id：" + bookId + "，章节索引：" + chapterIndex;
    }
}
