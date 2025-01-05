package ru.khav.NewsPaper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    JWTFilter jwtFilter(){return new JWTFilter();}



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //запросы, игнорируемые csrf
        Set<String> csrfAllowedMethods = Stream.of("GET", "HEAD", "TRACE", "OPTIONS").collect(Collectors.toSet());
        //для хранения токенов на серверной стороне приложения
        CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                )
                .cors(configurer->{
                    //источник конфигураций корс
                    UrlBasedCorsConfigurationSource corsConfigurationSource=new UrlBasedCorsConfigurationSource();
                    //куда сохраняются настройки корс
                    CorsConfiguration globalCorsConfiguration=new CorsConfiguration();
                    // Разрешаются CORS-запрoca c
                    globalCorsConfiguration.addAllowedOrigin("http://localhost:8080");
                    //с нестандартными заголовками
                    globalCorsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
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
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

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
