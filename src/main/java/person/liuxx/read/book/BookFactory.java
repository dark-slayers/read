package person.liuxx.read.book;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson.JSON;

import person.liuxx.read.book.impl.JsonBook;
import person.liuxx.read.book.impl.StorageBook;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.exception.BookNotFoundException;
import person.liuxx.util.base.StringUtil;
import person.liuxx.util.log.LogUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午2:20:24
 * @since 1.0.0
 */
public final class BookFactory
{
    private BookFactory()
    {
        throw new AssertionError("工具类禁止实例化！");
    }

    private static Logger log = LoggerFactory.getLogger(BookFactory.class);

    public static Book createBook(String bookName, List<Chapter> stories)
    {
        JsonBook book = new JsonBook(bookName, stories);
        return book;
    }

    /**
     * 使用文件名的hash码生成一个三层的子路径，每一层为一个十六进制字符
     * <p>
     * 文件名的hash码（十六进制）如果不足三位，补0
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月8日 下午2:56:15
     * @since 1.0.0
     * @param bookName
     *            文件名
     * @return
     */
    public static Path hashPath(String bookName)
    {
        // 获取文件名的十六进制hash码，转为流，流的元素为每个hash码的字符串表示，字符转为大写
        Stream<String> stream = Pattern.compile("")
                .splitAsStream(Integer.toHexString(bookName.hashCode()))
                .map(String::toUpperCase);
        // 创建当hash码长度不足三位时用来补位的流
        Stream<String> supplyStream = Stream.of("0", "0", "0");
        // 合并两个流，截取前三位，使用系统路径分隔符进行合并
        String subPath = Stream.concat(stream, supplyStream).limit(3).collect(Collectors.joining(
                File.separator));
        Path result = Paths.get(subPath, bookName + ".json");
        log.debug("hash路径：{}", result);
        return result;
    }

    /**
     * 解析单个txt文件
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月31日 下午3:02:27
     * @since 1.0.0
     * @param path
     * @param name
     * @return
     */
    public static StorageBook parseTxt(Path path, String name)
    {
        List<String> titleList = new LinkedList<>();
        titleList.add(name);
        List<String> stories = new LinkedList<>();
        try
        {
            String story = Files.lines(path).collect(Collectors.joining("\n"));
            stories.add(story);
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        StorageBook book = new StorageBook(name, titleList, stories);
        return book;
    }

    /**
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月8日 下午5:23:50
     * @since 1.0.0
     * @param key
     * @return
     */
    public static Optional<Book> load(BookDO bookDO)
    {
        if (Objects.isNull(bookDO) || StringUtil.isEmpty(bookDO.getPath()))
        {
            return Optional.empty();
        }
        Path targetPath = Paths.get(bookDO.getPath());
        JsonBook book = null;
        try
        {
            String text = Files.lines(targetPath).collect(Collectors.joining());
            book = JSON.parseObject(text, JsonBook.class);
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        return Optional.ofNullable(book);
    }

    public static ResponseEntity<Resource> createTxt(Book book, Path path)
    {
        if (Objects.isNull(path))
        {
            throw new BookNotFoundException("Book not found");
        }
        try
        {
            Files.deleteIfExists(path);
            List<String> lines = book.getChapters()
                    .stream()
                    .map(c -> String.join("\n", c.getTitleName(), c.getContent()))
                    .collect(Collectors.toList());
            Files.write(path, lines);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable())
            {
                String contentDisposition = "attachment; filename=\"" + book.getName() + ".txt\"";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, new String(contentDisposition
                                .getBytes("UTF-8"), "ISO8859-1"))
                        .body(resource);
            } else
            {
                throw new BookNotFoundException("Book not found");
            }
        } catch (IOException e)
        {
            throw new BookNotFoundException("Book not found", e);
        }
    }
}
