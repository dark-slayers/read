package person.liuxx.read.book;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import person.liuxx.read.domain.BookDO;
import person.liuxx.read.exception.BookLoadFailedException;
import person.liuxx.read.exception.BookNotFoundException;
import person.liuxx.read.exception.BookSaveFailedException;
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

    public Chapter getChapter(Long bookId, int index)
    {
        Chapter c = new Chapter();
        c.setBookId(bookId);
        c.setIndex(index);
        c.setTitle(getTitles().get(index));
        c.setContent(getStories().get(index));
        return c;
    }

    /**
     * 将book对象保存至指定的path路径，路径内部使用HASH进行三级子目录拆分，返回文件被保存后的存储路径
     * <p>
     * 参数path为null抛出NullPointerException
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月16日 上午11:03:40
     * @since 1.0.0
     * @param path
     * @return 文件被保存后的实际存储路径
     * @throws IOException
     */
    public Path save(Path path)
    {
        Objects.requireNonNull(path);
        Path targetPath = path.resolve(hashPath(this));
        Path parentPath = targetPath.getParent();
        if (!Files.exists(parentPath))
        {
            try
            {
                Files.createDirectories(parentPath);
            } catch (IOException e)
            {
                throw new BookSaveFailedException("书籍保存失败！", e);
            }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(targetPath));)
        {
            oos.writeObject(this);
        } catch (IOException e)
        {
            throw new BookSaveFailedException("书籍保存失败！", e);
        }
        return targetPath;
    }

    /**
     * 使用文件名的hash码生成一个三层的子路径，每一层为一个十六进制字符
     * <p>
     * 文件名的hash码（十六进制）如果不足三位，补0
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月14日 上午11:37:28
     * @since 1.0.0
     * @param book
     * @return
     */
    private Path hashPath(StorageBook book)
    {
        String bookName = book.getName();
        // 获取文件名的十六进制hash码，转为流，流的元素为每个hash码的字符串表示，字符转为大写
        Stream<String> stream = Pattern.compile("")
                .splitAsStream(Integer.toHexString(bookName.hashCode()))
                .map(String::toUpperCase);
        // 创建当hash码长度不足三位时用来补位的流
        Stream<String> supplyStream = Stream.of("0", "0", "0");
        // 合并两个流，截取前三位，使用系统路径分隔符进行合并
        String subPath = Stream.concat(stream, supplyStream).limit(3).collect(Collectors.joining(
                File.separator));
        return Paths.get(subPath, bookName + ".book");
    }

    /**
     * 使用BookDO中的路径对象，获取保存在本地磁盘的序列化后的StorageBook对象<br>
     * 如果参数为null或者无法将其中的path字符串信息解析为合法路径，返回Optional.empty()
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月16日 上午11:37:43
     * @since 1.0.0
     * @param BookDO
     *            一个包含path字段的书籍对象
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Optional<StorageBook> load(BookDO bookDO)
    {
        if (Objects.isNull(bookDO) || StringUtil.isEmpty(bookDO.getPath()))
        {
            return Optional.empty();
        }
        Path targetPath = Paths.get(bookDO.getPath());
        StorageBook book = null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(targetPath)))
        {
            book = (StorageBook) ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            throw new BookLoadFailedException("书籍加载失败！", e);
        }
        return Optional.ofNullable(book);
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

    /**
     * 在指定位置生成TXT文件，返回表示该TXT文件的服务器响应资源流
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月17日 上午9:54:34
     * @since 1.0.0
     * @param outPath
     * @return
     */
    public ResponseEntity<Resource> createTxt(Path outPath)
    {
        if (Objects.isNull(outPath))
        {
            throw new BookNotFoundException("Book not found");
        }
        try
        {
            Files.deleteIfExists(outPath);
            List<String> lines = new ArrayList<>();
            List<String> titleList = getTitles();
            List<String> storyList = getStories();
            for (int i = 0, max = titleList.size(); i < max; i++)
            {
                lines.add(titleList.get(i));
                lines.add(storyList.get(i));
            }
            Files.write(outPath, lines);
            Resource resource = new UrlResource(outPath.toUri());
            if (resource.exists() || resource.isReadable())
            {
                String contentDisposition = "attachment; filename=\"" + getName() + ".txt\"";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, new String(contentDisposition
                                .getBytes("UTF-8"), "ISO8859-1"))
                        .body(resource);
            } else
            {
                throw new BookNotFoundException("Book not found");
            }
        } catch (IOException e)
        {
            throw new BookNotFoundException("Book not found", e);
        }
    }
}
