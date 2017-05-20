package com.realsnake.sample.config.sec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <pre>
 * Class Name : SecurityConfig.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 12. 2.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 12. 2.
 * @version 1.0
 */
@EnableWebSecurity
// @PreAuthorize("isAnonymous()") / @PreAuthorize("hasAuthority('ROLE_ADMIN')"), @Secured("ROLE_USER") 사용가능하게
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserDetailsService authService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.authService).passwordEncoder(this.passwordEncoder());
    }

    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private RestAuthenticationFilter restAuthenticationFilter;

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
            return new RestUnauthorizedHandler();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            /* @formatter:off */
            http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/api/**").authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
//                    .antMatchers("/api/health").permitAll()
//                    .antMatchers("/api/v1/login").permitAll()
                    .antMatchers("/api/v1/auth").permitAll()
                    .antMatchers("/api/v1/user/double-check/**").permitAll()
                    .antMatchers("/api/v1/auth/mobile-auth-num").permitAll()
                    .antMatchers("/api/v1/user/search/apt").permitAll()
                    .antMatchers("/api/v1/user/join").permitAll()
                    .anyRequest().authenticated()
                .and()
                .logout()
                .and()
                .formLogin().disable()
                .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint()) // 인증 실패시 Redirect(302) 대신 401 Status만 리턴
                .and()
                .addFilterBefore(this.restAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            ;
            /* @formatter:on */
        }
    }

    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Bean
        public AuthSuccessHandler authSuccessHandler() {
            return new AuthSuccessHandler();
        }

        // @Resource(name = "oauthFilter")
        // private Filter oauthFilter;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            /* @formatter:off */
            http
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedPage("/") // TODO: 별도 접근 권한 오류 페이지 생성
                .and()
                // .addFilterBefore(this.oauthFilter, BasicAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/login*").anonymous()
                    .antMatchers("/user/join*").anonymous()
                    .antMatchers("/user/double-check/**").permitAll()
                    .antMatchers("/oauth/**").permitAll()
                    .antMatchers("/sample/**").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/common/**").permitAll() // 안하면 이미지 파일 호출 못함
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/user/**").hasAnyAuthority("ROLL_ADMIN", "ROLL_USER")
                    .anyRequest().authenticated()
                .and()
                // loginPage의 URL을 지정하지 않으면 스프링 시큐리티의 기본 로그인 화면(/login)으로 이동
                .formLogin()
                    .loginProcessingUrl("/loginProcessing")
                    .loginPage("/login")
                    .defaultSuccessUrl("/main", false)
                    .successHandler(this.authSuccessHandler())
                    .failureUrl("/login?error")
                .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "SESSIONID", "remember-me")
                .and()
                .rememberMe()
                    .key("remember-me-key")
                    .rememberMeParameter("remember-me")
                    .tokenValiditySeconds(86400 * 7) // 일주일
                    .rememberMeCookieName("remember-me-cookie")
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                    .invalidSessionUrl("/login?invalid") // 세션이 끊겼을 시
                    .maximumSessions(1).expiredUrl("/login?expired") // 중복 로그인 시
            ;
            /* @formatter:on */
        }

        // 스프링 시큐리티 필터가 무시된다.
        @Override
        public void configure(WebSecurity web) throws Exception {
            /* @formatter:off */
            web.ignoring().antMatchers(
                "/webjars/**"
                , "/css/**"
                , "/error/**"
                , "/images/**"
                , "/js/**"
                , "/theme/**"
                , "/actuators/**"
                , "/swagger*/**"
                , "/v2/**"
            );
            /* @formatter:on */
        }
    }

}
