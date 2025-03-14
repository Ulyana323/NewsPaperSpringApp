package ru.khav.NewsPaper.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.khav.NewsPaper.services.AuthorizeService;

import javax.servlet.Filter;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {


    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .antMatchers("/news/showComm").permitAll()
                .antMatchers("/news/show/**").permitAll()
                .antMatchers("/news/countComm").permitAll()
                .antMatchers("/auth/**").permitAll()
               .and()
                .cors(configurer->{
                    //источник конфигураций корс
                    UrlBasedCorsConfigurationSource corsConfigurationSource=new UrlBasedCorsConfigurationSource();
                    //куда сохраняются настройки корс
                    CorsConfiguration globalCorsConfiguration=new CorsConfiguration();
                    // Разрешаются CORS-запрoca c
                    globalCorsConfiguration.addAllowedOrigin("http://localhost:8080");
                    globalCorsConfiguration.addAllowedOrigin("http://localhost:8081");//на этом порте клиент
                    //с нестандартными заголовками
                    //globalCorsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
                    globalCorsConfiguration.setAllowedHeaders(Arrays.asList("*")); // Разрешенные заголовки
                    //с передачей учётных данных
                    globalCorsConfiguration.setAllowCredentials(true);
                    //с методами GET, POST, PUT, PATCH и DELETE
                    globalCorsConfiguration.setAllowedMethods(Stream.of(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.PATCH.name(),
                            HttpMethod.DELETE.name()).collect(Collectors.toList())
                    );
                    // JavaScript может обращаться к заголовку ответа
                    globalCorsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
                    // Браузер может кешировать настройки CORS на 10 секунд
                    globalCorsConfiguration.setMaxAge(Duration.ofSeconds(10));

                    // Использование конфигурации CORS для всех запросов
                    corsConfigurationSource.registerCorsConfiguration("/**", globalCorsConfiguration);

                    configurer.configurationSource(corsConfigurationSource);

                })
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler((request, response, e) ->
                                        //Логгировние ошибок доступа
                                        LOGGER.error(e.getMessage(), e))
                                .authenticationEntryPoint((request, response, e) ->
                                        //Логгировние ошибок аутентификации
                                        LOGGER.error(e.getMessage(), e)));

        return http.build();
    }


    @Bean
    BCryptPasswordEncoder getPasswordEncoder()
    {

        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
