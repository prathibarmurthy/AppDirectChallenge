package main.java.appdirect.interview.appdirectchallenge.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User) {
            final String openId = ((User) authentication.getPrincipal()).getUsername();
            try {
                URI uri = new URI(openId);
                httpServletResponse.sendRedirect(String.format("%s://%s/applogout?openid=%s", uri.getScheme(), uri.getHost(), openId));
            } catch (URISyntaxException e) {
                httpServletResponse.sendRedirect(String.format("https://www.appdirect.com/applogout?openid=%s", openId));
            }
        }
    }
}