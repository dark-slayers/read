package person.liuxx.read.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月28日 下午4:21:22
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "book")
public class BookConfig
{
    private String storagePath;

    public String getStoragePath()
    {
        return storagePath;
    }

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
    }
}
