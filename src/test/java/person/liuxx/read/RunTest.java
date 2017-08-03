package person.liuxx.read;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import person.liuxx.read.service.BookService;

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
    BookService bookService;

    @Test
    public void testRun()
    {
        // StorageBook book =
        // BookFactory.parseDir(Paths.get("F:\\Book\\000007\\000007"),
        // "张三丰异界游");
        // bookService.save(book);
    }
}
