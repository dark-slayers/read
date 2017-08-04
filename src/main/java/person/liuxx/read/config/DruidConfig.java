package person.liuxx.read.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * @author 刘湘湘
 * @version 1.0.0<br>
 *          创建时间：2017年8月4日 下午1:48:57
 * @since 1.0.0
 */
@Configuration
public class DruidConfig
{
    @Bean
    public ServletRegistrationBean servletRegistrationBean()
    {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean()
    {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new WebStatFilter());
        return registrationBean;
    }
}
