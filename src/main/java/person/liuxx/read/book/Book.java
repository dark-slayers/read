package person.liuxx.read.book;

import java.nio.file.Path;
import java.util.List;

/**
 * 书籍对象
 * 
 * @author 刘湘湘
 * @version 1.10.0<br>
 *          创建时间：2017年6月26日 上午11:24:18
 * @since 1.10.0
 */
public interface Book
{
    /**
     * 书籍名称
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月7日 上午11:16:08
     * @since 1.0.0
     * @return
     */
    String getName();

    /**
     * 获取目录列表，要求有顺序
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月7日 下午1:56:46
     * @since 1.0.0
     * @return
     */
    List<String> getTitles();

    /**
     * 获取章节列表，要求有顺序
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月7日 下午1:56:12
     * @since 1.0.0
     * @return
     */
    List<Chapter> getChapters();

    /**
     * 使用索引序号获取指定章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月7日 下午2:35:40
     * @since 1.0.0
     * @param index
     * @return
     */
    Chapter getChapter(int index);

    /**
     * 将对象保存至本地目标文件夹
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月8日 上午11:48:35
     * @since 1.0.0
     * @param dir
     *            目标文件夹
     * @return 保存后的路径的字符串表示
     */
    String save(Path dir);
}
