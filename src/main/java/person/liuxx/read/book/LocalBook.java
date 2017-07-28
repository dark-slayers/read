package person.liuxx.read.book;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import person.liuxx.read.page.StoryPage;
import person.liuxx.read.page.WebPage;
import person.liuxx.util.base.StringUtil;
import person.liuxx.util.file.FileUtil;

/**
 * 本地书籍对象
 * 
 * @author 刘湘湘
 * @version 1.10.0<br>
 *          创建时间：2017年6月26日 上午11:23:44
 * @since 1.10.0
 */
public class LocalBook implements Book
{
    private Logger log = LogManager.getLogger();
    private List<String> titleList;
    private Map<String, String> stories;
    private String bookName;

    public LocalBook(Path path)
    {
        Path listPath = path.resolve("List.htm");
        log.info("获取本地书籍的目录页：{}", listPath);
        WebPage titleListPage = new WebPage(listPath);
        Document doc = Jsoup.parse(titleListPage.getSource());
        Element content = doc.getElementById("t_body");
        Elements links = content.getElementsByTag("a");
        titleList = new LinkedList<>();
        stories = new HashMap<>();
        boolean hasBookName = false;
        for (Element link : links)
        {
            String linkText = formatTitle(link.text());
            if (Objects.equals("分卷阅读", linkText))
            {
                continue;
            }
            if (!hasBookName)
            {
                bookName = linkText;
                log.info("获取书籍名称：{}", bookName);
                hasBookName = true;
            } else
            {
                if (titleList.contains(linkText))
                {
                    log.error("重复章节：{}", linkText);
                }
                log.info("获取章节名称：{}", linkText);
                titleList.add(linkText);
                String linkHref = link.attr("href");
                log.info("章节《{}》对应的文件路径：{}", linkText, linkHref);
                Path p = path.resolve(linkHref);
                log.info("解析链接指向的文件：{}", p);
                if (FileUtil.existsFile(p))
                {
                    if (p.toFile().length() < 100)
                    {
                        log.error("文件<{}>为空！", p);
                    } else
                    {
                        StoryPage page = new StoryPage(p);
                        stories.put(linkText, page.getStory());
                    }
                } else
                {
                    log.error("文件<{}>不存在！", p);
                }
            }
        }
    }

    private String formatTitle(String title)
    {
        String result = Stream.of(title.split("")).filter(t -> !StringUtil.isBlank(t)).collect(
                Collectors.joining());
        return result;
    }

    public List<String> getTitleList()
    {
        return titleList;
    }

    public Map<String, String> getStories()
    {
        return stories;
    }
}
