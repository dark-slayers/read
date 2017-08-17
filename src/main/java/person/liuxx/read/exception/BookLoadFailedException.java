package person.liuxx.read.exception;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月16日 上午11:16:54
 * @since 1.0.0
 */
public class BookLoadFailedException extends RuntimeException
{
    private static final long serialVersionUID = -1008650235022002115L;

    public BookLoadFailedException(String message)
    {
        super(message);
    }

    public BookLoadFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
