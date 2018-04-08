package person.liuxx.read.book.impl;

import person.liuxx.read.book.Chapter;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月8日 上午11:13:23
 * @since 1.0.0
 */
public class BaseChapter implements Chapter
{
    private final String titleName;
    private final String content;

    public BaseChapter(String titleName, String content)
    {
        this.titleName = titleName;
        this.content = content;
    }

    @Override
    public String getTitleName()
    {
        return titleName;
    }

    @Override
    public String getContent()
    {
        return content;
    }
}
