package person.liuxx.read.book.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import person.liuxx.read.book.Book;
import person.liuxx.read.book.Chapter;
import person.liuxx.util.service.exception.SaveException;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月8日 上午11:04:07
 * @since 1.0.0
 */
public class JsonBook implements Book
{
    private String name;
    private List<JsonChapter> chapters;

    public JsonBook()
    {
    }

    /**
     * @param bookName
     * @param stories
     */
    public JsonBook(String bookName, List<JsonChapter> chapters)
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
    public Chapter getChapter(int index)
    {
        return chapters.get(index);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setChapters(List<JsonChapter> chapters)
    {
        this.chapters = chapters;
    }

    @Override
    public Book insertChapter(int index, Chapter chapter)
    {
        chapters.add(index, (JsonChapter) chapter);
        return this;
    }

    @Override
    public Book updateChapter(int index, Chapter chapter)
    {
        chapters.set(index, (JsonChapter) chapter);
        return this;
    }

    @Override
    public void removeChapter(int index)
    {
        chapters.remove(index);
    }

    @Override
    public Optional<ResponseEntity<Resource>> getTxtResource(Path path)
    {
        try
        {
            Files.deleteIfExists(path);
            List<String> lines = chapters.stream()
                    .map(c -> String.join("\n", c.getTitleName(), c.getContent()))
                    .collect(Collectors.toList());
            Files.write(path, lines);
            Resource resource = new UrlResource(path.toUri());
            return Optional.of(resource).filter(r -> r.exists() || r.isReadable()).map(
                    r -> resourceToEntity(r));
        } catch (IOException e)
        {
            throw new SaveException("生成书籍TXT文件失败!", e);
        }
    }

    private ResponseEntity<Resource> resourceToEntity(Resource r)
    {
        String contentDisposition = "attachment; filename=\"" + getName() + ".txt\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, reCode(contentDisposition))
                .body(r);
    }

    private String reCode(String contentDisposition)
    {
        try
        {
            return new String(contentDisposition.getBytes("UTF-8"), "ISO8859-1");
        } catch (UnsupportedEncodingException e)
        {
            throw new SaveException("生成书籍TXT文件失败!", e);
        }
    }

    @Override
    public Stream<Chapter> chapterStream()
    {
        return chapters.stream().map(c -> c);
    }
}
