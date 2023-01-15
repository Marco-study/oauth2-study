package com.example.oauth2study.auth;

import com.example.oauth2study.user.model.Role;
import com.example.oauth2study.user.model.User;
import com.example.oauth2study.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oauth2User = {}", oAuth2User.getAttributes());

        String nickName = oAuth2User.getAttributes().get("name").toString();
        String email = oAuth2User.getAttributes().get("email").toString();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();

        User user = null;
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            log.info("최초 로그인입니다. 회원가입 진행합니다");
            user = new User(nickName, email, Role.USER);
            userRepository.save(user);
        } else{
            log.info("구글 로그인 기록이 있습니다.");
            user = findUser.get().update(nickName);
        }

        return oAuth2User;
    }
}
