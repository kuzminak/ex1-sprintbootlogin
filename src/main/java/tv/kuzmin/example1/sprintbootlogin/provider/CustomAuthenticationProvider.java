package tv.kuzmin.example1.sprintbootlogin.provider;


import java.util.ArrayList;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import tv.kuzmin.userauthcontrolapi.RemoteAuthService;
import tv.kuzmin.userauthcontrolapi.UserInfo;


@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private HessianProxyFactoryBean authService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        return doRemoteAuth(authentication);
    }


    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    private UsernamePasswordAuthenticationToken doRemoteAuth(Authentication authentication)
    {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try
        {
            UserInfo userInfo = ((RemoteAuthService) authService.getObject()).login(name, password);
            if (Objects.nonNull(userInfo))
            {
                return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
            }
            return null;
        }
        catch (RemoteAccessException exception)
        {
            log.error("Error while remote auth. Username {}, Exception {}", name, exception.getMessage());
        }

        return null;
    }
}
