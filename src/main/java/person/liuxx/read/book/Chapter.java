package person.liuxx.read.book;

/**
 * 章节对象
 * 
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2018年4月7日 下午1:47:31
 * @since 1.0.0
 */
public interface Chapter
{
    /** 获取标题
    * @author  刘湘湘 
    * @version 1.0.0<br>创建时间：2018年4月7日 下午1:55:07
    * @since 1.0.0 
    * @return
    */
    String getTitleName();

    /** 获取章节内容
    * @author  刘湘湘 
    * @version 1.0.0<br>创建时间：2018年4月7日 下午1:55:15
    * @since 1.0.0 
    * @return
    */
    String getContent();
}
