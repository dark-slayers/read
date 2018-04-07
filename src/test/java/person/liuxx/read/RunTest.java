package person.liuxx.read;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;

import person.liuxx.read.book.impl.StoreChapter;
import person.liuxx.read.service.impl.BookServiceImpl;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月1日 下午2:14:44
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReadApplication.class)
@WebAppConfiguration
@Transactional
public class RunTest
{
    @Autowired
    BookServiceImpl bookService;

    @Test
    public void testRun()
    {
        StoreChapter logChapter = new StoreChapter();
        logChapter.setBookId(12L);
        logChapter.setIndex(8);
        logChapter.setTitle("AA");
        String s = JSON.toJSONString(logChapter);
        System.out.println(s);
    }
}
