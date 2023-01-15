package com.example.oauth2study.auth;

import com.example.oauth2study.auth.model.CustomUserPrincipal;
import com.example.oauth2study.user.model.User;
import com.example.oauth2study.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = customUserPrincipal.getUser();

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        user.assignRefreshToken(refreshToken);
        userRepository.saveAndFlush(user);


    }
}
