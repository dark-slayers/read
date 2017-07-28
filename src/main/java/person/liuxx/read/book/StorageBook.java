package person.liuxx.read.book;

import java.io.Serializable;
import java.util.List;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午2:12:46
 * @since 1.0.0
 */
public class StorageBook implements Serializable
{
    private static final long serialVersionUID = 1698294675972351331L;
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

    public void save()
    {
    }
}
