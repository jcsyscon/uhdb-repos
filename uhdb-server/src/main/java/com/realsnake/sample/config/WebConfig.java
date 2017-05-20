package com.realsnake.sample.config;

import java.util.List;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.realsnake.sample.config.paging.MobilePagingArgumentResolver;
import com.realsnake.sample.config.paging.PagingArgumentResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter implements ErrorPageRegistrar {

    @Bean
    public ApiLoginChecker apiLoginChecker() {
        return new ApiLoginChecker();
    }

    @Bean
    public PagingArgumentResolver pagingArgumentResolver() {
        return new PagingArgumentResolver();
    }

    @Bean
    public MobilePagingArgumentResolver mobilePagingArgumentResolver() {
        return new MobilePagingArgumentResolver();
    }

    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        // resolver.setDefaultLocale(Locale.KOREAN);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages"); // 리소스 locale/messages_{언어코드}.properties
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * RestTemplate 설정, 20161108, 전강욱<br />
     *
     * <pre>
     * 사용 예)
     * &#64;
     * Autowired private RestTemplate restTemplate
     * </pre>
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(3000); // 연결 요청 지연이 3초를 초과하면 Exception
        requestFactory.setConnectTimeout(10000); // 서버에 연결 지연이 10초를 초과하면 Exception
        requestFactory.setReadTimeout(3000); // 데이터 수신 지연이 3초를 초과하면 Exception

        return new RestTemplate(requestFactory);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(this.apiLoginChecker());
        registry.addInterceptor(this.localeChangeInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.pagingArgumentResolver());
        argumentResolvers.add(this.mobilePagingArgumentResolver());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /* @formatter:off */
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("x-auth-token", "x-access-token", "x-requested-with", "authorized", "content-type")
//                .allowCredentials(false)
                .maxAge(3600)
        ;
        /* @formatter:on */
    }

    /**
     * 스핑부트1.4 부터 지원<br />
     * 리소스/static 아래 error 폴더 생성 후 그 아래 html 파일을 두어 사용한다(정적 html 파일만 가능)
     */
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html"));
        registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html"));
        // registry.addErrorPages(new ErrorPage("/error/error.html"));
    }

}
