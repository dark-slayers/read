package person.liuxx.read.manager.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.Book;
import person.liuxx.read.cache.BookCache;
import person.liuxx.read.manager.BookManager;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月9日 上午11:23:31
 * @since 1.0.0
 */
@Service
public class BookManagerImpl implements BookManager
{
    @Autowired
    private BookCache bookCache;

    @Override
    public Optional<Book> getBookById(long id)
    {
        return bookCache.getStorageBook(id);
    }
}
