package person.liuxx.read.book.impl;

import lombok.Data;
import person.liuxx.read.book.Chapter;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月8日 上午11:13:23
 * @since 1.0.0
 */
@Data
public class JsonChapter implements Chapter
{
    private String titleName;
    private String content;

    public JsonChapter(String titleName, String content)
    {
        this.titleName = titleName.split("_")[0];
        this.content = content;
    }
}
