package person.liuxx.read.service;

import java.nio.file.Path;
import java.util.List;

import person.liuxx.read.book.Chapter;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月8日 下午2:24:26
 * @since 1.0.0
 */
public interface BookParseService
{
    /**
     * 解析指定目录，将目录解析为章节列表
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月8日 下午2:47:57
     * @since 1.0.0
     * @param path
     * @return
     */
    List<Chapter> parseDir(Path path);
}
