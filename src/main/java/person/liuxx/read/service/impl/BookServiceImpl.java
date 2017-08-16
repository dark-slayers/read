package person.liuxx.read.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.StorageBook;
import person.liuxx.read.config.BookConfig;
import person.liuxx.read.dao.BookRepository;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.exception.BookNotFoundException;
import person.liuxx.read.exception.BookSaveFailedException;
import person.liuxx.util.base.StringUtil;
import person.liuxx.util.log.LogUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午4:56:01
 * @since 1.0.0
 */
@Service
public class BookServiceImpl
{
    private Logger log = LogManager.getLogger();
    private final static Path DEFAULT_STORAGE_PATH = Paths.get("F:\\Book\\Storage");
    @Autowired
    private BookConfig bookConfig;
    @Autowired
    private BookRepository bookDao;

    /**
     * 使用书籍名称从数据库中查询书籍对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年7月31日 下午2:33:10
     * @since 1.0.0
     * @param bookName
     *            书籍名称
     * @return 一个包含BookDO对象的Optional容器，可能为空容器(Optional.empty())
     */
    public Optional<BookDO> findUseName(String bookName)
    {
        Optional<BookDO> bookOptional = bookDao.findByName(bookName);
        return bookOptional;
    }

    /**
     * 使用书籍ID查询书籍对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月16日 上午11:49:36
     * @since 1.0.0
     * @param bookId
     *            书籍ID
     * @return 一个包含BookDO对象的Optional容器，可能为空容器(Optional.empty())
     */
    public Optional<BookDO> findUseId(Long bookId)
    {
        BookDO bookDO = bookDao.findOne(bookId);
        return Optional.ofNullable(bookDO);
    }

    /**
     * 将StorageBook对象保存至本地文件，同时将保存路径写入数据库
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月14日 上午10:18:23
     * @since 1.0.0
     * @param book
     * @return
     * @throws IOException
     */
    public BookDO save(StorageBook book) throws IOException
    {
        Objects.requireNonNull(book);
        BookDO bookDO = new BookDO();
        // 如果无法从配置文件中获取存储路径，使用默认路径作为存储路径
        Path targetPath = (Objects.nonNull(bookConfig) && !StringUtil.isEmpty(bookConfig
                .getStoragePath())) ? Paths.get(bookConfig.getStoragePath()) : DEFAULT_STORAGE_PATH;
        log.info("文件存储路径：{}", targetPath);
        bookDO.setPath(book.save(targetPath).toString());
        bookDO.setName(book.getName());
        return bookDao.save(bookDO);
    }

    /**
     * 使用BookDO中的路径对象，获取保存在本地磁盘的序列化后的StorageBook对象<br>
     * 如果参数为null或者无法将其中的path字符串信息解析为合法路径，返回Optional.empty()
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月16日 上午11:50:03
     * @since 1.0.0
     * @param bookDO
     * @return 一个包含StorageBook对象的Optional容器，可能为空容器(Optional.empty())
     */
    public Optional<StorageBook> read(BookDO bookDO)
    {
        if (Objects.isNull(bookDO) || StringUtil.isEmpty(bookDO.getPath()))
        {
            log.warn("无效的bookDO数据：{}", bookDO);
            return Optional.empty();
        }
        Path targetPath = Paths.get(bookDO.getPath());
        log.info("文件存储路径：{}", targetPath);
        try
        {
            return Optional.ofNullable(StorageBook.load(targetPath));
        } catch (ClassNotFoundException | IOException e)
        {
            log.error("尝试从路径{}获取文件失败！", targetPath);
            log.error(LogUtil.errorInfo(e));
            return Optional.empty();
        }
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
    public ResponseEntity<Resource> createTxtFile(Long id)
    {
        Optional<BookDO> bookOptional = findUseId(id);
        Optional<ResponseEntity<Resource>> result = bookOptional.map(b ->
        {
            return Paths.get(b.getPath()).getParent().resolve(b.getName() + ".txt");
        }).flatMap(p ->
        {
            return read(bookOptional.orElse(null)).map(b -> b.createTxt(p));
        });
        return result.orElseThrow(() ->
        {
            throw new BookNotFoundException("Book not found, book id : " + id);
        });
    }

    public Chapter deleteChapter(Long bookId, int chapterIndex)
    {
        Optional<BookDO> optional = findUseId(bookId);
        Optional<StorageBook> bookOptional = read(optional.orElse(null));
        Chapter emptyChapter = new Chapter();
        emptyChapter.setBookId(-1L);
        emptyChapter.setIndex(-1);
        emptyChapter.setTitle("");
        emptyChapter.setContent("");
        Chapter result = bookOptional.filter(b -> b.getTitles().size() > chapterIndex && b
                .getStories().size() > chapterIndex).map(b ->
                {
                    Chapter chapter = b.getChapter(bookId, chapterIndex);
                    b.getTitles().remove(chapterIndex);
                    b.getStories().remove(chapterIndex);
                    try
                    {
                        b.update(optional.orElse(null));
                    } catch (Exception e)
                    {
                        throw new BookSaveFailedException("文件更细失败！", e);
                    }
                    return chapter;
                }).orElse(emptyChapter);
        return result;
    }
}
