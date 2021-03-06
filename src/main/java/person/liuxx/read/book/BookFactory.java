package person.liuxx.read.book;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import person.liuxx.read.book.impl.JsonBook;
import person.liuxx.read.book.impl.JsonChapter;
import person.liuxx.read.entity.BookDO;
import person.liuxx.util.base.StringUtil;
import person.liuxx.util.log.LogUtil;
import person.liuxx.util.service.exception.SaveException;

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
        List<JsonChapter> list = stories.stream().map(c -> (JsonChapter) c).collect(Collectors
                .toList());
        JsonBook book = new JsonBook(bookName, list);
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
    public static Book parseTxt(Path path, String name)
    {
        List<JsonChapter> stories = new LinkedList<>();
        try
        {
            String story = Files.lines(path).collect(Collectors.joining("\n"));
            stories.add(new JsonChapter(name, story));
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        JsonBook book = new JsonBook(name, stories);
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
        log.info("开始加载BOOK：{}", bookDO);
        if (Objects.isNull(bookDO) || StringUtil.isEmpty(bookDO.getPath()))
        {
            return Optional.empty();
        }
        Path targetPath = Paths.get(bookDO.getPath());
        JsonBook book = null;
        try
        {
            String text = Files.lines(targetPath).collect(Collectors.joining());
            Type type = new TypeReference<List<JsonChapter>>()
            {
            }.getType();
            List<JsonChapter> list = JSON.parseObject(text, type);
            book = new JsonBook(bookDO.getName(), list);
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        log.info("加载BOOK结束：{}", bookDO);
        return Optional.ofNullable(book);
    }

    /**
     * 将Book对象序列化后保存至本地文件夹，本地文件夹内部使用hash进行子文件夹分割。
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月10日 下午4:59:10
     * @since 1.0.0
     * @param book
     *            Book对象
     * @param dir
     *            目标目录
     * @return 保存后的实际位置的全路径
     */
    public static Optional<Path> saveToDir(Book book, Path dir)
    {
        Path subPath = BookFactory.hashPath(book.getName());
        Path targetPath = dir.resolve(subPath);
        return save(book, targetPath);
    }

    /**
     * 将Book对象序列化后保存至指定目标位置
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月10日 下午5:00:04
     * @since 1.0.0
     * @param book
     *            Book对象
     * @param targetPath
     *            目标位置
     * @return 目标位置
     */
    public static Optional<Path> save(Book book, Path targetPath)
    {
        Path parentPath = targetPath.getParent();
        List<JsonChapter> chapters = book.chapterStream()
                .filter(c -> Objects.nonNull(c))
                .map(c -> (JsonChapter) c)
                .collect(Collectors.toList());
        String text = JSON.toJSONString(chapters);
        List<String> list = new ArrayList<>();
        list.add(text);
        try
        {
            if (!Files.exists(parentPath))
            {
                Files.createDirectories(parentPath);
            }
            Files.write(targetPath, list);
        } catch (IOException e)
        {
            throw new SaveException("书籍保存失败！", e);
        }
        return Optional.of(targetPath);
    }
}
