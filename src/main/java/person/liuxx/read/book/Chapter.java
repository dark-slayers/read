package person.liuxx.read.book;

import com.alibaba.fastjson.JSON;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月31日 下午4:31:22
 * @since 1.0.0
 */
public class Chapter
{
    private Long bookId;
    private int index;
    private String title;
    private String content;

    public Chapter()
    {
    }

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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * 生成一个空白章节，章节的标题和内容都为空白字符串，书籍ID和章节索引都为-1
     * 
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月18日 上午10:25:38
     * @since 1.0.0
     * @return
     */
    public static Chapter empty()
    {
        Chapter emptyChapter = new Chapter();
        emptyChapter.setBookId(-1L);
        emptyChapter.setIndex(-1);
        emptyChapter.setTitle("");
        emptyChapter.setContent("");
        return emptyChapter;
    }

    /** 生成用于记录日志的JSON字符串，无content字段
    * @author  刘湘湘 
    * @version 1.0.0<br>创建时间：2017年8月18日 下午4:55:45
    * @since 1.0.0 
    * @return
    */
    public String logInfo()
    {
        Chapter logChapter = new Chapter();
        logChapter.setBookId(bookId);
        logChapter.setIndex(index);
        logChapter.setTitle(title);
        
        return JSON.toJSONString(logChapter);
    }
}
