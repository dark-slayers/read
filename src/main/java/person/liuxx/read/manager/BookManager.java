package person.liuxx.read.manager;

import java.util.Optional;

import person.liuxx.read.book.Book;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月9日 上午11:22:18
 * @since 1.0.0
 */
public interface BookManager
{
    /**
     * 使用书籍id，获取书籍对象。
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2018年4月9日 上午11:15:58
     * @since 1.0.0
     * @param id
     *            书籍id
     * @return
     */
    Optional<Book> getBookById(long id);
}
