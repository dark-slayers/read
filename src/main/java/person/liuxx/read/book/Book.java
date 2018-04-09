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

    /**
     * 将章节插入指定位置，原来位置以及原来位置之后的的章节后移
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 上午11:34:58
     * @since 1.0.0
     * @param index
     *            目标位置
     * @param chapter
     *            章节
     * @return 修改后的Book对象
     */
    Book insertChapter(int index, Chapter chapter);

    /**
     * 更新指定位置的章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 下午3:20:27
     * @since 1.0.0
     * @param index
     *            目标位置
     * @param chapter
     *            章节
     * @return 修改后的Book对象
     */
    Book updateChapter(int index, Chapter chapter);

    /**
     * 删除指定位置的章节，指定位置以及后面的章节前移
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 下午3:20:31
     * @since 1.0.0
     * @param index
     *            目标位置
     * 
     * @return 修改后的Book对象
     */
    Book removeChapter(int index);
}
