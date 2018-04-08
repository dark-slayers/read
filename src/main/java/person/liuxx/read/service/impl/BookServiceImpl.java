package person.liuxx.read.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.Book;
import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.impl.StorageBook;
import person.liuxx.read.book.impl.StoreChapter;
import person.liuxx.read.cache.BookCache;
import person.liuxx.read.config.BookConfig;
import person.liuxx.read.dao.BookRepository;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.dto.BookDTO;
import person.liuxx.read.service.BookParseService;
import person.liuxx.read.service.BookService;
import person.liuxx.util.base.StringUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午4:56:01
 * @since 1.0.0
 */
@Service
public class BookServiceImpl implements BookService
{
    private Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final static Path DEFAULT_STORAGE_PATH = Paths.get("F:\\Book\\Storage");
    @Autowired
    private BookConfig bookConfig;
    @Autowired
    private BookRepository bookDao;
    @Autowired
    private BookCache bookCache;
    @Autowired
    private BookParseService bookParseService;

    private Optional<StorageBook> loadStorageBookById(Long id)
    {
        return bookCache.getStorageBook(id);
    }

    private Optional<BookDO> getBookDOById(Long id)
    {
        return bookCache.getBookDOById(id);
    }

    /**
     * 章节对象转为书籍对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月22日 上午10:24:30
     * @since 1.0.0
     * @param chapter
     * @return
     */
    private Optional<StorageBook> getStorageBook(StoreChapter chapter)
    {
        return Optional.ofNullable(chapter)
                .filter(c -> c.getBookId() > 0 && c.getIndex() >= 0)
                .flatMap(c -> loadStorageBookById(chapter.getBookId()));
    }

    /**
     * 使用参数的ID，获取本地书籍资源，使用本地书籍资源生成txt文件<br>
     * 返回txt文件资源流
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月15日 下午2:34:40
     * @since 1.0.0
     * @param id
     *            需要获取的书籍id
     * @return
     */
    public Optional<ResponseEntity<Resource>> getTxtFile(Long id)
    {
        log.info("下载id为{}的书籍的TXT文件...", id);
        Optional<BookDO> bookOptional = getBookDOById(id);
        Optional<ResponseEntity<Resource>> result = bookOptional.map(b ->
        {
            log.info("从数据库中查询到id为{}的书籍记录", id);
            return Paths.get(b.getPath()).getParent().resolve(b.getName() + ".txt");
        }).flatMap(p ->
        {
            log.info("从数据库记录的书籍信息的路径{}中加载书籍对象", p);
            return loadStorageBookById(id).map(b -> b.createTxt(p));
        });
        return result;
    }

    @Override
    public Optional<BookDO> getBook(String name)
    {
        log.info("使用书籍名称《{}》查询对应的书籍", name);
        return bookDao.findByName(name);
    }

    @Override
    public Optional<List<String>> listBookTitle(Long bookId)
    {
        log.info("使用书籍id{}获取书籍目录列表", bookId);
        Optional<StorageBook> bookOption = loadStorageBookById(bookId);
        Optional<List<String>> list = bookOption.map(b -> b.getTitles());
        return list;
    }

    @Override
    public Optional<BookDO> loadDir(BookDTO book)
    {
        log.info("需要添加的book信息：{}", book);
        // 如果无法从配置文件中获取存储路径，使用默认路径作为存储路径
        Path targetPath = Optional.ofNullable(bookConfig)
                .map(c -> c.getStoragePath())
                .filter(p -> !StringUtil.isEmpty(p))
                .map(p -> Paths.get(p))
                .orElse(DEFAULT_STORAGE_PATH);
        log.info("文件存储路径：{}", targetPath);
        List<Chapter> list = bookParseService.parseDir(Paths.get(book.getPath()));
        Book b = BookFactory.createBook(book.getName(), list);
        String path = b.save(targetPath);
        BookDO bookDO = new BookDO();
        bookDO.setPath(path);
        bookDO.setName(b.getName());
        BookDO saveBookDO = bookDao.save(bookDO);
        return Optional.ofNullable(saveBookDO);
    }

    @Override
    public Optional<Chapter> getChapter(Long bookId, int chapterIndex)
    {
        log.info("查询书籍id为{}，章节索引为{}的章节", bookId, chapterIndex);
        Optional<StorageBook> bookOption = loadStorageBookById(bookId);
        Optional<Chapter> chapter = bookOption.map(b -> b.getChapter(bookId, chapterIndex));
        return chapter;
    }

    @Override
    public Optional<StoreChapter> saveChapter(StoreChapter chapter)
    {
        log.info("请求添加章节:{}", chapter.logInfo());
        Optional<StorageBook> bookOptional = getStorageBook(chapter);
        Optional<StoreChapter> result = bookOptional.flatMap(b ->
        {
            Optional<BookDO> bookDO = getBookDOById(chapter.getBookId());
            Optional<StoreChapter> cp = bookDO.map(bDO ->
            {
                b.getTitles().add(chapter.getIndex(), chapter.getTitleName());
                b.getStories().add(chapter.getIndex(), chapter.getContent());
                b.update(bookDO.orElse(null));
                return chapter;
            });
            return cp;
        });
        return result;
    }

    @Override
    public Optional<StoreChapter> removeChapter(Long bookId, int chapterIndex)
    {
        log.info("请求删除id为{}的书籍中，索引为{}的章节", bookId, chapterIndex);
        Optional<BookDO> optional = getBookDOById(bookId);
        Optional<StorageBook> bookOptional = loadStorageBookById(bookId);
        Optional<StoreChapter> result = bookOptional.filter(b ->
        {
            return b.getTitles().size() > chapterIndex && b.getStories().size() > chapterIndex;
        }).map(b ->
        {
            StoreChapter chapter = b.getChapter(bookId, chapterIndex);
            b.getTitles().remove(chapterIndex);
            b.getStories().remove(chapterIndex);
            b.update(optional.orElse(null));
            return chapter;
        });
        return result;
    }

    @Override
    public Optional<StoreChapter> updateChapter(StoreChapter chapter)
    {
        log.info("请求更新章节:{}", chapter.logInfo());
        Optional<StorageBook> bookOptional = getStorageBook(chapter);
        Optional<StoreChapter> result = bookOptional.flatMap(b ->
        {
            Optional<BookDO> bookDO = getBookDOById(chapter.getBookId());
            Optional<StoreChapter> cp = bookDO.map(bDO ->
            {
                b.getTitles().set(chapter.getIndex(), chapter.getTitleName());
                b.getStories().set(chapter.getIndex(), chapter.getContent());
                b.update(bookDO.orElse(null));
                return chapter;
            });
            return cp;
        });
        return result;
    }
}
