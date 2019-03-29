package person.liuxx.read.service;

import java.util.List;
import java.util.Optional;

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
     * 解析本地磁盘的指定文件夹，将此文件夹解析为指定书籍名称的书籍，然后保存此书籍
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午10:50:02
     * @since 1.0.0
     * @param book
     *            书籍信息对象，不能为null，name字段为书籍名称，path字段为需要解析的文件夹路径
     * @return 如果解析成功，返回存入数据库的书籍信息，该信息中包含id和书籍的文件路径
     */
    Optional<BookDO> parseAndSave(BookDTO book);

    /**
     * 获取全部的书籍列表
     * 
     * @author 刘湘湘
     * @since 2019年3月29日 下午1:25:24
     * @return 书籍列表,list
     */
    List<BookDO> listBooks();
}
