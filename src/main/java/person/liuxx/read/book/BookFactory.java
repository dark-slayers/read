package person.liuxx.read.book;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import person.liuxx.read.page.StoryPage;
import person.liuxx.read.page.TitlePage;
import person.liuxx.util.file.FileUtil;

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
    public static StorageBook parseLocalBook(Path path, String bookName)
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
}
