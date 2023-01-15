package com.example.oauth2study.config;

import com.example.oauth2study.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 쓰지 않고 jwt 방식으로 하겠다.
                .and()
                    .csrf().disable() //jwt 방식을 쓰면 불필요함
                    .formLogin().disable() //jwt 로그인을하기때문에 안씀
                    .httpBasic().disable() //jwt 로그인을하기때문에 안씀
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/**").hasAnyAuthority(Role.USER.getKey())
                    .antMatchers("/api/**/admin/**").hasAnyAuthority(Role.ADMIN.getKey())
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorization") //로그인페이지를 받기위한 서버의 엔드포인트 설정
                .and()
                    .redirectionEndpoint()
                    .baseUri("/*/oauth2/code/*");
        return http.build();
    }
}
