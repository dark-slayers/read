package person.liuxx.read.dao;

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
    BookDO findByName(String name);
}
