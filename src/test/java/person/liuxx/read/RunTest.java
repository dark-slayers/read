package person.liuxx.read;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import person.liuxx.read.book.BookStorageTypeEnum;
import person.liuxx.read.dto.BookDTO;
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
        BookDTO b1=new BookDTO();
        b1.setName("Test Book 1");
        b1.setPath("F:\\Book\\000001");
        b1.setType(BookStorageTypeEnum.DIR);
        bookService.parseAndSave(b1);
//        BookDO b = new BookDO();
//        b.setPath("F:/Book/Storage/F/C/8/Test Book 1.json");
//        BookFactory.load(b).ifPresent(bb ->
//        {
//            System.out.println(">>>>>>>>>>>" + bb.getName());
//            System.out.println(">>>>>>>>>>>" + bb.getTitles());
//        });
    }
}
