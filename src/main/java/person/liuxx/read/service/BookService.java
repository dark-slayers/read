package person.liuxx.read.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import person.liuxx.read.BookNotFoundException;
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
    private final static Path DEFAULT_STORAGE_PATH = Paths.get("F:\\Book\\Storage");
    @Autowired
    private BookConfig bookConfig;
    @Autowired
    private BookRepository bookDao;

    /**
     * 将StorageBook对象保存至本地文件，同时将保存路径写入数据库
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月14日 上午10:18:23
     * @since 1.0.0
     * @param book
     * @return
     */
    public BookDO save(StorageBook book)
    {
        Objects.requireNonNull(book);
        BookDO bookDO = new BookDO();
        Path targetPath = DEFAULT_STORAGE_PATH.resolve(hashPath(book));
        if (Objects.nonNull(bookConfig) && !StringUtil.isEmpty(bookConfig.getStoragePath()))
        {
            targetPath = Paths.get(bookConfig.getStoragePath()).resolve(hashPath(book));
        }
        log.info("文件存储路径：{}", targetPath);
        try
        {
            Path parentPath = targetPath.getParent();
            if (!Files.exists(parentPath))
            {
                log.info("文件父路径不存在，创建文件存储路径：{}", parentPath);
                Files.createDirectories(parentPath);
            }
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(targetPath));)
        {
            oos.writeObject(book);
            bookDO.setName(book.getName());
            bookDO.setPath(targetPath.toString());
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        return bookDao.save(bookDO);
    }

    /**
     * 使用文件名的hash码生成一个三层的子路径，每一层为一个十六进制字符
     * <p>
     * 文件名的hash码（十六进制）如果不足三位，补0
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月14日 上午11:37:28
     * @since 1.0.0
     * @param book
     * @return
     */
    private Path hashPath(StorageBook book)
    {
        String bookName = book.getName();
        // 获取文件名的十六进制hash码，转为流，流的元素为每个hash码的字符串表示，字符转为大写
        Stream<String> stream = Pattern.compile("")
                .splitAsStream(Integer.toHexString(bookName.hashCode()))
                .map(String::toUpperCase);
        // 创建当hash码长度不足三位时用来补位的流
        Stream<String> supplyStream = Stream.of("0", "0", "0");
        // 合并两个流，截取前三位，使用系统路径分隔符进行合并
        String subPath = Stream.concat(stream, supplyStream).limit(3).collect(Collectors.joining(
                File.separator));
        return Paths.get(subPath, bookName + ".book");
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
        Optional<BookDO> optional = findUseId(id);
        Optional<ResponseEntity<Resource>> op = optional.map(b ->
        {
            try
            {
                Path outPath = Paths.get(b.getPath()).getParent().resolve(b.getName() + ".txt");
                Files.deleteIfExists(outPath);
                List<String> lines = read(b).map(book ->
                {
                    List<String> list = new ArrayList<>();
                    List<String> titleList = book.getTitles();
                    List<String> storyList = book.getStories();
                    for (int i = 0, max = titleList.size(); i < max; i++)
                    {
                        list.add(titleList.get(i));
                        list.add(storyList.get(i));
                    }
                    return list;
                }).get();
                Files.write(outPath, lines);
                Resource resource = new UrlResource(outPath.toUri());
                if (resource.exists() || resource.isReadable())
                {
                    String contentDisposition = "attachment; filename=\"" + b.getName() + ".txt\"";
                    log.info("设置下载文件的文件名：{}", contentDisposition);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, new String(contentDisposition
                                    .getBytes("UTF-8"), "ISO8859-1"))
                            .body(resource);
                } else
                {
                    throw new BookNotFoundException("Book not found, book id : " + id);
                }
            } catch (Exception e)
            {
                throw new BookNotFoundException("Book not found, book id : " + id, e);
            }
        });
        return op.orElseThrow(() ->
        {
            throw new BookNotFoundException("Book not found, book id : " + id);
        });
    }
}
