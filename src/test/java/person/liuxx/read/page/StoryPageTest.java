package person.liuxx.read.page;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Objects;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月1日 下午4:10:29
 * @since 1.0.0
 */
public class StoryPageTest
{
    /**
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月1日 下午4:10:29
     * @since 1.0.0
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    /**
     * @author 刘湘湘
     * @version 1.0.0<br>
     *          创建时间：2017年8月1日 下午4:10:29
     * @since 1.0.0
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * {@link person.liuxx.read.page.StoryPage#changeHtmlNewline(java.lang.String)}
     * 的测试方法。
     */
    @Test
    public void testChangeHtmlNewline()
    {
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<br>"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<  br  >"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("< br/>"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<  br/  >"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<br/   >"), StoryPage.NEWLINE));
        assertTrue(!Objects.equal(StoryPage.changeHtmlNewline("<wbr/   >"), StoryPage.NEWLINE));
        assertTrue(!Objects.equal(StoryPage.changeHtmlNewline("<br/s   >"), StoryPage.NEWLINE));
        assertTrue(!Objects.equal(StoryPage.changeHtmlNewline("<brs/   >"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<p>"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<  p  >"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("< p/>"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<  p/  >"), StoryPage.NEWLINE));
        assertTrue(Objects.equal(StoryPage.changeHtmlNewline("<p/   >"), StoryPage.NEWLINE));
        assertTrue(!Objects.equal(StoryPage.changeHtmlNewline("<wp/   >"), StoryPage.NEWLINE));
        assertTrue(!Objects.equal(StoryPage.changeHtmlNewline("<p/s   >"), StoryPage.NEWLINE));
        assertTrue(!Objects.equal(StoryPage.changeHtmlNewline("<ps/   >"), StoryPage.NEWLINE));
    }
}
