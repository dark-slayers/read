package person.liuxx.read.service.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.impl.BaseChapter;
import person.liuxx.read.page.StoryPage;
import person.liuxx.read.page.TitlePage;
import person.liuxx.read.service.BookParseService;
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
        List<Chapter> result = new ArrayList<>();
        if (!FileUtil.existsDir(path))
        {
            return result;
        }
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
                    StoryPage page = new StoryPage(p);
                    result.add(new BaseChapter(key, page.getStory()));
                }
            } else
            {
                log.error("文件<{}>不存在！", p);
            }
        });
        return result;
    }
}
