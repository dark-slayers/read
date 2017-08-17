package person.liuxx.read.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import person.liuxx.read.domain.BookDO;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月31日 下午1:30:24
 * @since 1.0.0
 */
public interface BookRepository extends JpaRepository<BookDO, Long>
{
    /**
     * 使用书籍名称从数据库中查询书籍对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午10:46:02
     * @since 1.0.0
     * @param name
     *            书籍名称
     * @return 一个包含BookDO对象的Optional容器，可能为空容器(Optional.empty())
     */
    Optional<BookDO> findByName(String name);

    /**
     * 使用书籍ID查询书籍对象
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午10:46:53
     * @since 1.0.0
     * @param bookId
     *            书籍ID
     * @return 一个包含BookDO对象的Optional容器，可能为空容器(Optional.empty())
     */
    Optional<BookDO> findById(Long bookId);
    
}
