package person.liuxx.read.service;

import java.util.Optional;

import person.liuxx.read.book.Chapter;
import person.liuxx.read.dto.ChapterDTO;
import person.liuxx.util.service.reponse.EmptySuccedResponse;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月7日 下午2:56:14
 * @since 1.0.0
 */
public interface ChapterService
{
    /**
     * 删除指定书籍ID中的指定索引的章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 上午10:30:16
     * @since 1.0.0
     * @param bookId
     *            书籍ID
     * @param chapterIndex
     *            章节索引
     * @return 如果删除成功，返回无内容的成功响应
     */
    Optional<EmptySuccedResponse> remove(Long bookId, int chapterIndex);

    /**
     * 使用书籍ID和章节索引获取章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 上午10:29:20
     * @since 1.0.0
     * @param bookId
     *            书籍ID
     * @param chapterIndex
     *            章节索引
     * @return 查询到的章节
     */
    Optional<Chapter> getChapter(Long bookId, int chapterIndex);

    /**
     * 添加新章节，参数中的index表示插入成功后的章节索引号<br>
     * 相当于在目前数值为index索引的章节前面插入一个新的章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 下午3:49:10
     * @since 1.0.0
     * @return
     */
    Optional<EmptySuccedResponse> saveChapter(ChapterDTO chapter);

    /**
     * 使用参数信息，更新指定章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 上午11:23:35
     * @since 1.0.0
     * @param chapter
     * @return
     */
    Optional<EmptySuccedResponse> updateChapter(ChapterDTO chapter);
}
