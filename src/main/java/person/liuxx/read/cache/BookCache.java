package person.liuxx.read.cache;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import person.liuxx.read.book.impl.StorageBook;
import person.liuxx.read.dao.BookRepository;
import person.liuxx.read.domain.BookDO;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月17日 下午4:43:14
 * @since 1.0.0
 */
@Component
public class BookCache
{
    private Logger log = LogManager.getLogger();
    @Autowired
    private BookRepository bookDao;
    LoadingCache<BookDO, Optional<StorageBook>> bookCache;
    LoadingCache<Long, Optional<BookDO>> bookIndexCache;
    // 缓存的最大对象数量
    private final int MAX_BOOK_NUMBER = 5;
    // 不做读写处理后的缓存时间
    private final int CACHE_DURATION = 10;

    public BookCache()
    {
        bookIndexCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_BOOK_NUMBER)
                .expireAfterAccess(CACHE_DURATION, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, Optional<BookDO>>()
                {
                    @Override
                    public Optional<BookDO> load(Long key)
                    {
                        log.info("从数据库查询bookDO:{}", key);
                        return bookDao.findById(key);
                    }
                });
        bookCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_BOOK_NUMBER)
                .expireAfterAccess(CACHE_DURATION, TimeUnit.MINUTES)
                .build(new CacheLoader<BookDO, Optional<StorageBook>>()
                {
                    @Override
                    public Optional<StorageBook> load(BookDO key)
                    {
                        log.info("从本地加载book:{}", key);
                        return StorageBook.load(key);
                    }
                });
    }

    public Optional<StorageBook> getStorageBook(Long id)
    {
        if (Objects.isNull(id))
        {
            log.info("无效的ID参数：{}", id);
            return Optional.empty();
        }
        Optional<BookDO> bookOptional = getBookDOById(id);
        Optional<StorageBook> result = bookOptional.flatMap(k -> bookCache.getUnchecked(k));
        return result;
    }

    public Optional<BookDO> getBookDOById(Long id)
    {
        return bookIndexCache.getUnchecked(id);
    }
}
