package com.example.oauth2study.auth;

import com.example.oauth2study.auth.model.CustomUserPrincipal;
import com.example.oauth2study.user.model.ProviderType;
import com.example.oauth2study.user.model.Role;
import com.example.oauth2study.user.model.User;
import com.example.oauth2study.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    public CustomOauth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oauth2User = {}", oAuth2User.getAttributes());
        // TODO: 2023-01-16 너무 비대한 로직을 지니고 있어서 나중에 private 메소드로 분리 해야 할 듯
        String name = oAuth2User.getAttributes().get("name").toString();
        String email = oAuth2User.getAttributes().get("email").toString();
        String password = passwordEncoder.encode(email);
        ProviderType provider = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        String providerId = oAuth2User.getName();
        User user = null;
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            log.info("최초 로그인입니다. 회원가입 진행합니다");
            user = User.builder()
                    .password(password)
                    .role(Role.USER)
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else{
            log.info("구글 로그인 기록이 있습니다.");
            user = findUser.get().update(name);
        }

        return new CustomUserPrincipal(user, oAuth2User.getAttributes());

    }
}
