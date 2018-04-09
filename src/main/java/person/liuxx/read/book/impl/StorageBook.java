package person.liuxx.read.book.impl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import person.liuxx.read.domain.BookDO;
import person.liuxx.read.exception.BookUpdateFailedException;
import person.liuxx.util.base.StringUtil;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午2:12:46
 * @since 1.0.0
 */
public class StorageBook implements Serializable
{
    private static final long serialVersionUID = -1680376523158503740L;
    private String name;
    private List<String> titles;
    private List<String> stories;

    public StorageBook()
    {
    }

    public StorageBook(String name, List<String> titles, List<String> stories)
    {
        this.name = name;
        this.titles = titles;
        this.stories = stories;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getTitles()
    {
        return titles;
    }

    public void setTitles(List<String> titles)
    {
        this.titles = titles;
    }

    public List<String> getStories()
    {
        return stories;
    }

    public void setStories(List<String> stories)
    {
        this.stories = stories;
    }

    public StoreChapter getChapter(Long bookId, int index)
    {
        StoreChapter c = new StoreChapter();
        c.setBookId(bookId);
        c.setIndex(index);
        c.setTitle(getTitles().get(index));
        c.setContent(getStories().get(index));
        return c;
    }

    /**
     * 使用bookDO的信息，更新本次存储的书籍文件，会重新写入文件
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月16日 下午5:22:58
     * @since 1.0.0
     * @param bookDO
     * @return
     * @throws IOException
     */
    public Path update(BookDO bookDO)
    {
        if (Objects.isNull(bookDO) || StringUtil.isEmpty(bookDO.getPath()))
        {
            throw new BookUpdateFailedException("书籍更新失败，书籍信息：" + bookDO);
        }
        Path path = Paths.get(bookDO.getPath());
        try
        {
            Files.delete(path);
        } catch (IOException e)
        {
            throw new BookUpdateFailedException("书籍更新失败，书籍信息：" + bookDO);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path));)
        {
            oos.writeObject(this);
        } catch (IOException e)
        {
            throw new BookUpdateFailedException("书籍更新失败，书籍信息：" + bookDO);
        }
        return path;
    }
}
