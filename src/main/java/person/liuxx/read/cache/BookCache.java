package person.liuxx.read.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import person.liuxx.read.book.StorageBook;
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
    LoadingCache<BookDO, Optional<StorageBook>> bookCache;
    // 缓存的最大对象数量
    private final int MAX_BOOK_NUMBER = 5;
    // 不做读写处理后的缓存时间
    private final int CACHE_DURATION = 10;

    public BookCache()
    {
        bookCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_BOOK_NUMBER)
                .expireAfterAccess(CACHE_DURATION, TimeUnit.MINUTES)
                .build(new CacheLoader<BookDO, Optional<StorageBook>>()
                {
                    @Override
                    public Optional<StorageBook> load(BookDO key)
                    {
                        return StorageBook.load(key);
                    }
                });
    }

    public Optional<StorageBook> getStorageBook(BookDO key)
    {
        return bookCache.getUnchecked(key);
    }
}
