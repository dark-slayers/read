package person.liuxx.read.page;

import java.nio.file.Path;

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

	public StoryPage(Path path)
	{
		super(path);
		Document doc = Jsoup.parse(getSource());
		Element content = doc.getElementById("content");
		story = content.text();
	}

	public String getStory()
	{
		return story;
	}
}
