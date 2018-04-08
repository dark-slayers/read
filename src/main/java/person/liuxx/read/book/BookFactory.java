package person.liuxx.read.book;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import person.liuxx.read.book.impl.JsonBook;
import person.liuxx.read.book.impl.StorageBook;
import person.liuxx.read.page.StoryPage;
import person.liuxx.read.page.TitlePage;
import person.liuxx.util.file.FileUtil;
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

    private static Logger log = LogManager.getLogger();

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
     * @param bookName 文件名
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
        return Paths.get(subPath, bookName + ".json");
    }

    /**
     * 解析本地文件夹，将文件夹中的多个html文件解析为一个Book对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年7月28日 下午2:30:52
     * @since 1.0.0
     * @param path
     * @return
     */
    public static StorageBook parseDir(Path path, String bookName)
    {
        List<String> titleList = new LinkedList<>();
        List<String> stories = new LinkedList<>();
        TitlePage titlePage = new TitlePage(path);
        Map<String, String> map = titlePage.getMap();
        map.keySet().forEach(key ->
        {
            log.info("获取章节名称：{}", key);
            String linkHref = map.get(key);
            log.info("章节《{}》对应的文件路径：{}", key, linkHref);
            Path p = path.resolve(linkHref);
            log.info("解析链接指向的文件：{}", p);
            if (FileUtil.existsFile(p))
            {
                if (p.toFile().length() < 100)
                {
                    log.error("文件<{}>为空！", p);
                } else
                {
                    titleList.add(key);
                    StoryPage page = new StoryPage(p);
                    stories.add(page.getStory());
                }
            } else
            {
                log.error("文件<{}>不存在！", p);
            }
        });
        StorageBook book = new StorageBook(bookName, titleList, stories);
        return book;
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
}
