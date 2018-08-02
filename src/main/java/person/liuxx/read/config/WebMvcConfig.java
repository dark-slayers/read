package person.liuxx.read.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年7月26日 下午4:38:54
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/dev/**").addResourceLocations(
                "file:D:/DK/GitProject/read-page/dist/");
        registry.addResourceHandler("/page/**").addResourceLocations("file:./page/");
        super.addResourceHandlers(registry);
    }
}