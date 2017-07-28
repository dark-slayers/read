package person.liuxx.read.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import person.liuxx.read.book.StorageBook;
import person.liuxx.read.config.BookConfig;
import person.liuxx.util.log.LogUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午4:56:01
 * @since 1.0.0
 */
@Service
public class BookService
{
    private Logger log = LogManager.getLogger();
    @Autowired
    BookConfig bookConfig;

    public void save(StorageBook book)
    {
        log.debug("从配置文件获取文件存储文件夹：{}", bookConfig.getStoragePath());
        String targetPath = bookConfig.getStoragePath() + File.separator + book.getName() + ".book";
        log.debug("文件存储路径：{}", targetPath);
        try (FileOutputStream fos = new FileOutputStream(targetPath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);)
        {
            oos.writeObject(book);
        } catch (IOException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
    }

    public StorageBook read(String bookName)
    {
        log.debug("从配置文件获取文件存储文件夹：{}", bookConfig.getStoragePath());
        String targetPath = bookConfig.getStoragePath() + File.separator + bookName + ".book";
        log.debug("文件存储路径：{}", targetPath);
        StorageBook book = null;
        try (FileInputStream fis = new FileInputStream(targetPath);
                ObjectInputStream ois = new ObjectInputStream(fis);)
        {
            book = (StorageBook) ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            log.error(LogUtil.errorInfo(e));
        }
        return book;
    }
}
