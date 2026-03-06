package com.joao.tibia_scrapper.config;

import com.joao.tibia_scrapper.repository.UsuarioRepository;
import com.joao.tibia_scrapper.service.TotpService;
import com.joao.tibia_scrapper.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private TotpService totpService;

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider(totpService,
                                usuarioRepository);
                authProvider.setUserDetailsService(usuarioService);
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authenticationProvider(authenticationProvider())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/home", "/css/**", "/scripts/**", "/images/**",
                                                                "/json/**", "/cadastro",
                                                                "/login", "/enciclopedia/**", "/api/enciclopedia/**",
                                                                "/buscar-party/**",
                                                                "/comunidade/**", "/builder/**", "/hunts/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .authenticationDetailsSource(authenticationDetailsSource)
                                                .successHandler(successHandler())
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.sendRedirect("/");
                                                }));

                return http.build();
        }

        @Bean
        public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
                return new SavedRequestAwareAuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
                                String targetUrl = (String) request.getSession().getAttribute("url_prior_login");

                                if (targetUrl != null) {
                                        request.getSession().removeAttribute("url_prior_login");
                                        this.setDefaultTargetUrl(targetUrl);
                                } else {
                                        this.setDefaultTargetUrl("/");
                                }

                                super.onAuthenticationSuccess(request, response, authentication);
                        }
                };
        }
}