package person.liuxx.read.net;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.config.AuthSchemes;
import org.apache.hc.client5.http.config.CookieSpecs;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.sync.methods.HttpGet;
import org.apache.hc.core5.http.io.ResponseHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import person.liuxx.read.service.impl.BookParseServiceImpl;
import person.liuxx.util.log.LogUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年8月26日 下午2:51:30
 * @since 1.0.0
 */
public class GetWebPage
{
    private static Logger log = LoggerFactory.getLogger(BookParseServiceImpl.class);
    private static String root = "https://www.haxds.com";
    private static final long TIME_OUT = 15;
    public static void main(String[] args)
    {
        String url = "https://www.haxds.com/files/article/html/37/37802/index.html";
        Optional<String> body = simpleGet(url);
        List<String> lines = new ArrayList<>();
        body.ifPresent(s ->
        {
            Document doc = Jsoup.parse(s);
            String title=doc.getElementsByTag("title").first().text();
            Elements dls = doc.getElementsByClass("chapterlist");
            Element dl = dls.first();
            Elements as = dl.getElementsByTag("a");
            for (Element a : as)
            {
                String text = aToText(a);
                lines.add(text);
            }
            try
            {
                Files.write(Paths.get("H:/b/"+title+".txt"), lines);
            } catch (Exception e)
            {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        });
        
    }

    static String aToText(Element a)
    {
        String title = a.text();
        String text = simpleGet(root + a.attr("href")).map(b -> getText(b)).get();
        return title + "\n" + text;
    }

    public static String getText(String body)
    {
        Document doc = Jsoup.parse(body);
        Element textDiv = doc.getElementById("BookText");
        return textDiv.text().replaceAll("<\\s*?br\\s*?/??\\s*?>", "\n").replaceAll(
                "<\\s*?p\\s*?/??\\s*?>", "\n");
    }

    public static Optional<String> simpleGet(String url)
    {
        try (CloseableHttpClient httpclient = HttpClients.createDefault())
        {
            final HttpGet httpget = new HttpGet(url);
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,
                            AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                    .build();
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .setSocketTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .setConnectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .setConnectionRequestTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
            httpget.setConfig(requestConfig);
            httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
                    + " AppleWebKit/537.36 (KHTML, like Gecko)"
                    + " Chrome/60.0.3112.78 Safari/537.36");
            log.info("Executing request {} -> {}", httpget.getMethod(), httpget.getUri());
            final ResponseHandler<Optional<String>> responseHandler = new SimpleResponseHandler();
            Optional<String> responseBody = httpclient.execute(httpget, responseHandler);
            return responseBody;
        } catch (IOException | URISyntaxException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        return Optional.empty();
    }
}
