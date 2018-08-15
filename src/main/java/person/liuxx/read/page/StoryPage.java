package person.liuxx.read.page;

import java.nio.file.Path;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年6月30日 下午3:26:37
 * @since 1.0.0
 */
public class StoryPage extends WebPage
{
    private final String story;
    final static String NEWLINE = "#@@#";

    public StoryPage(Path path)
    {
        super(path);
        String htmlText = changeHtmlNewline(getSource());
        int startIndex = htmlText.indexOf("<!--BookContent Start-->");
        int endIndex = htmlText.indexOf("<!--BookContent End-->");
        String defalutText = "";
        if (startIndex >= 0 && endIndex >= 0)
        {
            defalutText = htmlText.substring(startIndex, endIndex).replaceAll(NEWLINE, "\n");
        }
        Document doc = Jsoup.parse(htmlText);
        Optional<String> storyOptional = Optional.ofNullable(doc)
                .map(d -> d.getElementById("content"))
                .map(c -> c.text());
        story = storyOptional.map(c -> c.replaceAll(NEWLINE, "\n")).orElse(defalutText);
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
