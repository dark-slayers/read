package person.liuxx.read.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月31日 上午11:59:03
 * @since 1.0.0
 */
@Entity
@Table(name = "book")
public class BookDO
{
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String name;
    private String path;

    public BookDO()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    @Override
    public String toString()
    {
        return "BookDO [id=" + id + ", name=" + name + ", path=" + path + "]";
    }
    
}
