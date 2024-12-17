package ru.khav.NewsPaper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    JWTFilter jwtFilter(){return new JWTFilter();}



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
                )
                /* .formLogin(
                         httpSecurityFormLoginConfigurer ->
                         httpSecurityFormLoginConfigurer.loginPage("/login")
                                 .loginProcessingUrl("/loginpr")
                                 .defaultSuccessUrl("/hello",true)
                                 .failureUrl("/error"))
                 .logout(logoutConfigurer->
                         logoutConfigurer.logoutUrl("/logout")
                                 .logoutSuccessUrl("/login"))*/
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
