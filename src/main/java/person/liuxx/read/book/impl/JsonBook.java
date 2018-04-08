package person.liuxx.read.book.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import person.liuxx.read.book.Book;
import person.liuxx.read.book.BookFactory;
import person.liuxx.read.book.Chapter;
import person.liuxx.read.exception.BookSaveFailedException;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月8日 上午11:04:07
 * @since 1.0.0
 */
public class JsonBook implements Book
{
    private String name;
    private List<Chapter> chapters;

    /**
     * @param bookName
     * @param stories
     */
    public JsonBook(String bookName, List<Chapter> chapters)
    {
        this.name = bookName;
        this.chapters = chapters;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public List<String> getTitles()
    {
        return chapters.stream().map(c -> c.getTitleName()).collect(Collectors.toList());
    }

    @Override
    public List<Chapter> getChapters()
    {
        return chapters;
    }

    @Override
    public Chapter getChapter(int index)
    {
        return chapters.get(index);
    }

    @Override
    public String save(Path dir)
    {
        Path subPath = BookFactory.hashPath(name);
        Path targetPath = dir.resolve(subPath);
        Path parentPath = targetPath.getParent();
        String text = JSON.toJSONString(this);
        List<String> list = new ArrayList<>();
        list.add(text);
        try
        {
            if (!Files.exists(parentPath))
            {
                Files.createDirectories(parentPath);
            }
            Files.write(targetPath, list);
        } catch (IOException e)
        {
            throw new BookSaveFailedException("书籍保存失败！", e);
        }
        return targetPath.toString();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setChapters(List<Chapter> chapters)
    {
        this.chapters = chapters;
    }
}
