package person.liuxx.read;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import person.liuxx.read.book.BookFactory;
import person.liuxx.read.domain.BookDO;
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
        BookDO b = new BookDO();
        b.setPath("F:/Book/Storage/8/2/0/AA.json");
        BookFactory.load(b).ifPresent(bb ->
        {
            System.out.println(">>>>>>>>>>>" + bb.getName());
            System.out.println(">>>>>>>>>>>" + bb.getTitles());
        });
        ;
    }
}
