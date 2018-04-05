package person.liuxx.read.page;

import java.nio.file.Path;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年6月30日 下午3:26:37
 * @since 1.0.0
 */
public class StoryPage extends WebPage
{
    private String story;
    static final String NEWLINE = "#@@#";

    public StoryPage(Path path)
    {
        super(path);
        Document doc = Jsoup.parse(changeHtmlNewline(getSource()));
        Element content = doc.getElementById("content");
        if (Objects.nonNull(content))
        {
            story = content.text().replaceAll(NEWLINE, "\n");
        }else{
            story ="章节缺失！";
        }
    }

    public String getStory()
    {
        return story;
    }

    static String changeHtmlNewline(String html)
    {
        return html.replaceAll("<\\s*?br\\s*?/??\\s*?>", NEWLINE).replaceAll(
                "<\\s*?p\\s*?/??\\s*?>", NEWLINE);
    }
}
