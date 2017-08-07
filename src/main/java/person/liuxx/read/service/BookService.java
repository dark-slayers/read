package person.liuxx.read.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.StorageBook;
import person.liuxx.read.config.BookConfig;
import person.liuxx.read.dao.BookRepository;
import person.liuxx.read.domain.BookDO;
import person.liuxx.util.base.StringUtil;
import person.liuxx.util.log.LogUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午4:56:01
 * @since 1.0.0
 */
@Service
public class BookService
{
    private Logger log = LogManager.getLogger();
    private final static String DEFAULT_STORAGE_PATH = "F:\\Book\\Storage";
    @Autowired
    private BookConfig bookConfig;
    @Autowired
    private BookRepository bookDao;

    public BookDO save(StorageBook book)
    {
        Objects.requireNonNull(book);
        BookDO bookDO = new BookDO();
        String targetPath = DEFAULT_STORAGE_PATH;
        if (Objects.nonNull(bookConfig) && !StringUtil.isEmpty(bookConfig.getStoragePath()))
        {
            targetPath = bookConfig.getStoragePath() + File.separator + book.getName() + ".book";
        }
        log.info("文件存储路径：{}", targetPath);
        try (FileOutputStream fos = new FileOutputStream(targetPath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);)
        {
            oos.writeObject(book);
            bookDO.setName(book.getName());
            bookDO.setPath(targetPath);
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        return bookDao.save(bookDO);
    }

    /**
     * 使用书籍名称从数据库中查询书籍对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年7月31日 下午2:33:10
     * @since 1.0.0
     * @param bookName
     *            书籍名称
     * @return
     */
    public Optional<BookDO> findUseName(String bookName)
    {
        log.debug("使用书籍名称《{}》查询数据库，获取文件对象...", bookName);
        Optional<BookDO> bookOptional = bookDao.findByName(bookName);
        log.debug("使用书籍名称《{}》查询数据库结果：{}", bookName, bookOptional);
        return bookOptional;
    }

    public Optional<BookDO> findUseId(Long bookId)
    {
        log.info("查询数据库，获取文件存储路径...");
        BookDO bookDO = bookDao.findOne(bookId);
        return Optional.ofNullable(bookDO);
    }

    public Optional<StorageBook> read(BookDO bookDO)
    {
        if (Objects.isNull(bookDO) || StringUtil.isEmpty(bookDO.getPath()))
        {
            return Optional.empty();
        }
        String targetPath = bookDO.getPath();
        log.info("文件存储路径：{}", targetPath);
        StorageBook book = null;
        try (FileInputStream fis = new FileInputStream(targetPath);
                ObjectInputStream ois = new ObjectInputStream(fis);)
        {
            book = (StorageBook) ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        return Optional.ofNullable(book);
    }
}
