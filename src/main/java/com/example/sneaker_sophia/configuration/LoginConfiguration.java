package com.example.sneaker_sophia.configuration;


import com.example.sneaker_sophia.security.AuthenticationSuccessEventListener;
import com.example.sneaker_sophia.security.CustomSuccessHandler;
import com.example.sneaker_sophia.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class LoginConfiguration {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login/home")
                        .loginProcessingUrl("/login/login")
                        .successHandler(new CustomSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/home")
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/login/home")
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/home"))
                );

        return http.build();
    }


    @Bean
    public AuthenticationSuccessEventListener customAuthenticationSuccessEventListener() {
        return new AuthenticationSuccessEventListener();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

