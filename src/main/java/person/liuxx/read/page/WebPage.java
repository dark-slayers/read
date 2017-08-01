package person.liuxx.read.page;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import person.liuxx.util.log.LogUtil;

/**
 * @author 刘湘湘
 * @version 1.10.0<br>
 *          创建时间：2017年6月5日 下午4:53:48
 * @since 1.10.0
 */
public class WebPage
{
    private Logger log = LogManager.getLogger();
    private final Charset charset;
    private final String source;

    public WebPage(Path path)
    {
        String html = "";
        Charset c = StandardCharsets.UTF_8;
        try
        {
            InputStream in = Files.newInputStream(path);
            html = IOUtils.toString(in);
            c = getCharset(html);
            log.info("设置编码为 : {}", c);
            in = Files.newInputStream(path);
            html = IOUtils.toString(in, c.toString());
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        source = html;
        charset = c;
    }

    private Charset getCharset(String html)
    {
        Charset result = StandardCharsets.UTF_8;
        Pattern p = Pattern.compile("<meta.*?charset=.*?>");
        Matcher m = p.matcher(html.toLowerCase());
        while (m.find())
        {
            String meta = m.group();
            Pattern p2 = Pattern.compile("charset=.*?>");
            Matcher m2 = p2.matcher(meta);
            while (m2.find())
            {
                String searchCharset = m2.group().replaceAll("charset=", "");
                if (searchCharset.startsWith("\"") || searchCharset.startsWith("'"))
                {
                    searchCharset = searchCharset.substring(1);
                }
                String endString = Arrays.stream(searchCharset.split("")).filter(c ->
                {
                    String[] array =
                    { "'", "\"", " ", "/", ">", };
                    Set<String> set = new HashSet<>(Arrays.asList(array));
                    return set.contains(c);
                }).findFirst().get();
                int endIndex = searchCharset.indexOf(endString);
                searchCharset = searchCharset.substring(0, endIndex);
                log.info("从页面中获取的编码格式：{}", searchCharset);
                result = Charset.forName(searchCharset);
            }
        }
        return result;
    }

    public String getSource()
    {
        return source;
    }

    public Charset getCharset()
    {
        return charset;
    }
}
