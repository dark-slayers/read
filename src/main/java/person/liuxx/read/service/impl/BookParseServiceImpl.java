package person.liuxx.read.service.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.impl.JsonChapter;
import person.liuxx.read.exception.BookParseException;
import person.liuxx.read.page.LocalTitlePage;
import person.liuxx.read.page.StoryPage;
import person.liuxx.read.service.BookParseService;
import person.liuxx.util.file.DirUtil;
import person.liuxx.util.file.FileUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月8日 下午2:28:42
 * @since 1.0.0
 */
@Service
public class BookParseServiceImpl implements BookParseService
{
    private Logger log = LoggerFactory.getLogger(BookParseServiceImpl.class);

    @Override
    public List<Chapter> parseDir(Path path)
    {
        log.info("解析路径：{}", path);
        if (!DirUtil.existsDir(path))
        {
            return new ArrayList<>();
        }
        LocalTitlePage titlePage = new LocalTitlePage(path);
        Map<String, String> textHrefMap = titlePage.getTextHrefMap();
        Map<String, Chapter> chapterMap = getMap(textHrefMap, path);
        List<Chapter> result = textHrefMap.keySet()
                .stream()
                .map(key -> chapterMap.get(key))
                .collect(Collectors.toList());
        return result;
    }

    private Map<String, Chapter> getMap(final Map<String, String> map, final Path path)
    {
        Map<String, Chapter> result = new ConcurrentHashMap<>();
        map.keySet().stream().filter(filter()).parallel().forEach(key ->
        {
            String linkHref = map.get(key);
            Path pa = path.resolve(linkHref);
            String message = "章节解析异常，章节名称:" + key + "，章节路径:" + pa;
            String story = Optional.ofNullable(pa)
                    .filter(FileUtil::existsFile)
                    .filter(p -> p.toFile().length() > 100)
                    .map(StoryPage::new)
                    .map(s -> s.getStory())
                    .<BookParseException> orElseThrow(() ->
                    {
                        throw new BookParseException(message);
                    });
            result.put(key, new JsonChapter(key, story));
        });
        return result;
    }

    private Predicate<String> filter()
    {
        String[] array =
        { "分卷阅读" };
        Set<String> set = new HashSet<>(Arrays.asList(array));
        return new Predicate<String>()
        {
            @Override
            public boolean test(String t)
            {
                return !set.contains(t.split("_")[0]);
            }
        };
    }
}
