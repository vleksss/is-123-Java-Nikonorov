package com.auction.config;

import com.auction.security.AuctionUserDetailsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuctionUserDetailsService auctionUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/login",
                                "/register",
                                "/thymeleaf/login",
                                "/thymeleaf/register",
                                "/mustache/login",
                                "/mustache/register",
                                "/freemarker/login",
                                "/freemarker/register",
                                "/api/public/**"
                        ).permitAll()
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/thymeleaf/admin/**", "/mustache/admin/**", "/freemarker/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        .requestMatchers("/thymeleaf/owner/**", "/mustache/owner/**", "/freemarker/owner/**").hasRole("OWNER")
                        .requestMatchers(
                                "/",
                                "/profile",
                                "/owner/dashboard",
                                "/owner/auctions/**",
                                "/thymeleaf/**",
                                "/mustache/**",
                                "/freemarker/**",
                                "/bids"
                        ).authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession(false);
                            String engine = session != null ? (String) session.getAttribute("LOGIN_ENGINE") : null;
                            if (engine == null || engine.isBlank()) {
                                engine = "thymeleaf";
                            }
                            response.sendRedirect("/" + engine + "/auctions");
                        })
                        .failureHandler((request, response, exception) -> {
                            HttpSession session = request.getSession(false);
                            String engine = session != null ? (String) session.getAttribute("LOGIN_ENGINE") : null;
                            if (engine == null || engine.isBlank()) {
                                engine = "thymeleaf";
                            }
                            response.sendRedirect("/" + engine + "/login?error");
                        })
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(auctionUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
