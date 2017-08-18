package person.liuxx.read.exception;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月16日 上午11:16:54
 * @since 1.0.0
 */
public class BookRemoveFailedException extends RuntimeException
{
    private static final long serialVersionUID = -1008650235022002115L;

    public BookRemoveFailedException(String message)
    {
        super(message);
    }

    public BookRemoveFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
