package tv.kuzmin.example1.sprintbootlogin.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import tv.kuzmin.userauthcontrolapi.RemoteAuthService;


@Configuration
public class RemoteAuthServiceConfig
{
    @Value("${remote-auth-service.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${remote-auth-service.timeout.read:3000}")
    private int readTimeout;

    @Bean
    public HessianProxyFactoryBean authService(@Value("${remote-auth-service.url}") String url)
    {
        return hessianProxyFactoryBean(url);
    }


    public HessianProxyFactoryBean hessianProxyFactoryBean(String url)
    {
        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
        factory.setServiceUrl(url);
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        factory.setServiceInterface(RemoteAuthService.class);
        return factory;
    }
}
