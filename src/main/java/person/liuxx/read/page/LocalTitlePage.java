package person.liuxx.read.page;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import person.liuxx.util.base.StringUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午3:08:33
 * @since 1.0.0
 */
public class LocalTitlePage
{
    private Logger log = LogManager.getLogger();
    private final WebPage webPage;
    private Map<String, String> textHrefMap;

    public LocalTitlePage(Path path)
    {
        Path listPath = path.resolve("List.htm");
        log.info("获取本地书籍的目录页：{}", listPath);
        this.webPage = new WebPage(listPath);
        textHrefMap = new LinkedHashMap<>();
        Document doc = Jsoup.parse(webPage.getSource());
        Element content = doc.getElementById("t_body");
        Elements links = content.getElementsByTag("a");
        int index = 1;
        for (Element link : links)
        {
            String linkText = formatTitle(link.text(), index);
            String linkHref = link.attr("href");
            textHrefMap.put(linkText, linkHref);
            index++;
        }
    }

    /**
     * 目录文字和链接的对应map,文字为章节目录，map的key有序
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年8月14日 下午8:38:28
     * @since 1.0.0
     * @return
     */
    public Map<String, String> getTextHrefMap()
    {
        return textHrefMap;
    }

    private String formatTitle(String title, int index)
    {
        String result = Stream.of(title.split("")).filter(t -> !StringUtil.isBlank(t)).collect(
                Collectors.joining());
        return result + "_" + index;
    }
}
