package person.liuxx.read.dto;

import person.liuxx.read.book.BookStorageType;

/**
 * 请求服务器加载本地资源对象时，向服务器发出的参数对象，对象包含了创建书籍有关信息
 * 
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月25日 下午1:54:40
 * @since 1.0.0
 */
public class BookDTO
{
    private String name;
    private String path;
    private BookStorageType type;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public BookStorageType getType()
    {
        return type;
    }

    public void setType(BookStorageType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "BookDTO [name=" + name + ", path=" + path + ", type=" + type + "]";
    }
}
