package person.liuxx.read.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.Book;
import person.liuxx.read.book.Chapter;
import person.liuxx.read.dao.BookRepository;
import person.liuxx.read.dto.ChapterDTO;
import person.liuxx.read.manager.BookManager;
import person.liuxx.read.service.ChapterService;
import person.liuxx.util.service.exception.SaveException;
import person.liuxx.util.service.exception.SearchException;
import person.liuxx.util.service.reponse.EmptySuccedResponse;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月9日 上午10:41:51
 * @since 1.0.0
 */
@Service
public class ChapterServiceImpl implements ChapterService
{
    private Logger log = LoggerFactory.getLogger(ChapterServiceImpl.class);
    private final String METHOD_INSERT = "INSERT";
    private final String METHOD_UPDATE = "UPDATE";
    private final String METHOD_REMOVE = "REMOVE";
    @Autowired
    private BookManager bookManager;
    @Autowired
    private BookRepository bookDao;

    @Override
    public Optional<EmptySuccedResponse> remove(Long bookId, int chapterIndex)
    {
        log.info("请求删除id为{}的书籍中，索引为{}的章节", bookId, chapterIndex);
        return updateBook(bookId, chapterIndex, null, METHOD_REMOVE);
    }

    @Override
    public Optional<Chapter> getChapter(Long bookId, int chapterIndex)
    {
        log.info("查询书籍id为{}，章节索引为{}的章节", bookId, chapterIndex);
        Optional<Book> bookOption = bookManager.getBookById(bookId);
        Optional<Chapter> chapter = bookOption.map(b -> b.getChapter(chapterIndex));
        return chapter;
    }

    @Override
    public Optional<EmptySuccedResponse> saveChapter(ChapterDTO chapter)
    {
        log.info("请求添加章节:{}", chapter.logInfo());
        long id = Optional.ofNullable(chapter).map(c -> c.getBookId()).orElse(-1L);
        return updateBook(id, chapter.getIndex(), chapter.mapToChapter(), METHOD_INSERT);
    }

    @Override
    public Optional<EmptySuccedResponse> updateChapter(ChapterDTO chapter)
    {
        log.info("请求更新章节:{}", chapter.logInfo());
        long id = Optional.ofNullable(chapter).map(c -> c.getBookId()).orElse(-1L);
        return updateBook(id, chapter.getIndex(), chapter.mapToChapter(), METHOD_UPDATE);
    }

    private Optional<EmptySuccedResponse> updateBook(long id, int chapterIndex, Chapter chapter,
            String updateMethod)
    {
        Book book = bookManager.getBookById(id).orElseThrow(() ->
        {
            throw new SearchException("获取书籍对象失败！id:" + id);
        });
        Path path = bookDao.findById(id).map(b -> b.getPath()).map(Paths::get).orElseThrow(() ->
        {
            throw new SaveException("获取保存路径失败！");
        });
        switch (updateMethod)
        {
        case METHOD_INSERT:
            {
                book.insertChapter(chapterIndex, chapter);
                break;
            }
        case METHOD_UPDATE:
            {
                book.updateChapter(chapterIndex, chapter);
                break;
            }
        case METHOD_REMOVE:
            {
                book.removeChapter(chapterIndex);
                break;
            }
        }
        book.save(path);
        return Optional.of(new EmptySuccedResponse());
    }
}
