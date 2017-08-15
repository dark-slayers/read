package person.liuxx.read;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月15日 上午11:29:53
 * @since 1.0.0
 */
public class BookNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 2616645300994107031L;

    public BookNotFoundException(String message)
    {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
