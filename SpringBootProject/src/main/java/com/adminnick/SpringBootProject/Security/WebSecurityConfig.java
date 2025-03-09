package com.adminnick.SpringBootProject.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
    private static final String[] WHITELIST = {
        "/",
        "/login",
        "/register",
        "/forgot-password",
        "/profile/**",
        "/uploads/**",
        "/db-console/**",
        "/css/**",
        "/fonts/",
        "/afbeeldingen/**",
        "/js/**",
        "/resources/**",
        "/posts/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITELIST).permitAll()
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")
                .anyRequest()
                .authenticated()  
        )
        .formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login?error")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID", "remember-me")
            .permitAll()
        )
        .rememberMe(rememberMe -> rememberMe
            // .key(null)
            .rememberMeParameter("remember-me")
            // .tokenValiditySeconds(14 * 24 * 60 *60) // equals 14 days 
        )
        .httpBasic(httpBasic -> httpBasic.disable()
        );

        // Remove after upgrading the database
        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()) // Allows frames from the same origin
        );

        return http.build();
    }
}
