package person.liuxx.read.dto;

import com.alibaba.fastjson.JSON;

import person.liuxx.read.book.Chapter;
import person.liuxx.read.book.impl.JsonChapter;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月9日 上午10:50:39
 * @since 1.0.0
 */
public class ChapterDTO
{
    private Long bookId;
    private int index;
    private String titleName;
    private String content;

    public Long getBookId()
    {
        return bookId;
    }

    public void setBookId(Long bookId)
    {
        this.bookId = bookId;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getTitleName()
    {
        return titleName;
    }

    public void setTitleName(String titleName)
    {
        this.titleName = titleName;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String logInfo()
    {
        ChapterDTO logChapter = new ChapterDTO();
        logChapter.setBookId(bookId);
        logChapter.setIndex(index);
        logChapter.setTitleName(titleName);
        return JSON.toJSONString(logChapter);
    }

    public Chapter mapToChapter()
    {
        return new JsonChapter(titleName, content);
    }

    @Override
    public String toString()
    {
        return "ChapterDTO [bookId=" + bookId + ", index=" + index + ", titleName=" + titleName
                + ", content=" + content + "]";
    }
}
