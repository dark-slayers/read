package person.liuxx.read.service;

import java.util.List;
import java.util.Optional;

import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.impl.StoreChapter;
import person.liuxx.read.domain.BookDO;
import person.liuxx.read.dto.BookDTO;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月17日 上午10:34:48
 * @since 1.0.0
 */
public interface BookService
{
    /**
     * 使用书籍名称，获取书籍对象。
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午10:41:34
     * @since 1.0.0
     * @param name
     * @return
     */
    Optional<BookDO> getBook(String name);

    /**
     * 获取指定ID的书籍的目录列表
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午10:49:57
     * @since 1.0.0
     * @param bookId
     * @return
     */
    Optional<List<String>> listBookTitle(Long bookId);

    /**
     * 加载本地磁盘的指定文件夹，将此文件夹解析为指定书籍名称的书籍
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午10:50:02
     * @since 1.0.0
     * @param book
     *            书籍信息对象，不能为null，name字段为书籍名称，path字段为需要解析的文件夹路径
     * @return 如果解析成功，返回存入数据库的书籍信息，该信息中包含id和书籍的文件路径
     */
    Optional<BookDO> loadDir(BookDTO book);

    /**
     * 添加新章节，参数中的index表示插入成功后的章节索引号<br>
     * 相当于在目前数值为index索引的章节前面插入一个新的章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 下午3:49:10
     * @since 1.0.0
     * @return
     */
    Optional<StoreChapter> saveChapter(StoreChapter chapter);

    /**
     * 删除指定书籍的指定章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午11:20:49
     * @since 1.0.0
     * @param bookId
     *            书籍ID
     * @param chapterIndex
     *            章节索引
     * @return 被删除的章节信息，如果删除失败，返回一个空白章节（书籍id和索引id都为-1）<br>
     *         如果更新本地文件时发生异常，抛出BookSaveFailedException
     */
    Optional<StoreChapter> removeChapter(Long bookId, int chapterIndex);

    /**
     * 使用参数信息，更新指定章节
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午11:23:35
     * @since 1.0.0
     * @param chapter
     * @return
     */
    Optional<StoreChapter> updateChapter(StoreChapter chapter);

    /**
     * 使用书籍ID和章节索引index获取章节对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午11:21:25
     * @since 1.0.0
     * @param bookId
     * @param chapterIndex
     * @return
     */
    Optional<Chapter> getChapter(Long bookId, int chapterIndex);
}
